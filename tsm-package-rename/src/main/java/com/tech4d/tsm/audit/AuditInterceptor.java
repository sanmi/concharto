/*******************************************************************************
 * ***** BEGIN LICENSE BLOCK *****
 * Version: MPL 1.1
 * 
 * The contents of this file are subject to the Mozilla Public License Version 
 * 1.1 (the "License"); you may not use this file except in compliance with 
 * the License. You may obtain a copy of the License at 
 * http://www.mozilla.org/MPL/
 * 
 * Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 * for the specific language governing rights and limitations under the
 * License.
 * 
 * The Original Code is Concharto.
 * 
 * The Initial Developer of the Original Code is
 * Time Space Map, LLC
 * Portions created by the Initial Developer are Copyright (C) 2007 - 2009
 * the Initial Developer. All Rights Reserved.
 * 
 * Contributor(s):
 * Time Space Map, LLC
 * 
 * ***** END LICENSE BLOCK *****
 ******************************************************************************/
package com.tech4d.tsm.audit;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.hibernate.CallbackException;
import org.hibernate.EmptyInterceptor;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.type.Type;

import com.tech4d.tsm.auth.AuthHelper;
import com.tech4d.tsm.dao.AuditLogWriter;
import com.tech4d.tsm.model.Auditable;
import com.tech4d.tsm.model.audit.AuditEntry;

/**
 * hibernate interceptor to create AuditEntry records for all classes implementing the 
 * Auditable interface.  It will record insertions, updates, and deletions.
 */
public class AuditInterceptor extends EmptyInterceptor {
    private static final long serialVersionUID = 1L;
    private ThreadLocalSets stateSets = new ThreadLocalSets();   // this is thread-safe
    private AuditLogWriter auditLogWriter;
    private SessionFactory sessionFactory;
    private Set<AuditFieldChangeFormatter> auditFormatters = new HashSet<AuditFieldChangeFormatter>();
    

    /**
     * A list of class names of classes that implement AuditFieldChangeFormatter.  We use
     * these to create the audit field change record
     * @param auditFieldChangeFormatters AuditFieldChangeFormatter 
     * @throws InstantiationException e
     * @throws IllegalAccessException e
     * @throws ClassNotFoundException e
     */
    public void setAuditFieldChangeFormatters(Set<String> auditFieldChangeFormatters) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        for (String className : auditFieldChangeFormatters) {
            auditFormatters.add((AuditFieldChangeFormatter) Class.forName(className).newInstance());
        }
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public void setAuditLogWriter(AuditLogWriter auditLogWriter) {
        this.auditLogWriter = auditLogWriter;
    }

    public boolean onFlushDirty(Object entity, Serializable id, Object[] currentState, Object[] previousState,
                                String[] propertyNames, Type[] types) throws CallbackException {
        boolean updated = false;
        if (entity instanceof Auditable) {
            //log.debug("onFlushDirty");
            Session session = sessionFactory.openSession();
            try {
                Class<? extends Object> objectClass = entity.getClass();

                //update the lastModified field
                for ( int i=0; i < propertyNames.length; i++ ) {
                    if ( "lastModified".equals( propertyNames[i] ) ) {
                        currentState[i] = new Date();
                        updated = true;
                    } else if ("version".equals(propertyNames[i] ) ) {
                        Long version =((Long)(currentState[i]));
                        if (version == null) {
                            currentState[i] = 0L;
                        } else {
                            currentState[i] = ++version;
                        }
                        updated = true;
                    }
                }
                
                // Use the id and class to get the pre-update state from the
                // database
                Serializable persistedObjectId = ((Auditable) entity).getId();
                Object previousInstance = session.get(objectClass, persistedObjectId);
                refresh((Auditable) entity);
                refresh((Auditable) previousInstance);
                UpdateState state = new UpdateState((Auditable) entity, (Auditable) previousInstance);
                stateSets.getUpdates().add(state);

            } catch (HibernateException e) {
                clearStateSets();
                throw new CallbackException(e);
            } finally {
                session.close();
            }           
        }
        return updated;
    }

    private void refresh(Auditable auditable) {
        AuditFieldChangeFormatter formatter = getFormatter(auditable);
        if (formatter != null) {
            formatter.refresh(auditable);
        }
    }
    
    public boolean onSave(Object o, Serializable serializable, Object[] state, String[] propertyNames, Type[] types)
            throws CallbackException {
        boolean updated = false;
        if (o instanceof Auditable) {
            //log.debug("onSave");
            stateSets.getInserts().add(o);
            for ( int i=0; i<propertyNames.length; i++ ) {
                if ( "created".equals( propertyNames[i] ) ) {
                    state[i] = new Date();
                    updated = true;
                } else if ( "lastModified".equals( propertyNames[i] ) ) {
                    state[i] = new Date();
                    updated = true;
                } else if ("version".equals(propertyNames[i] ) ) {
                    state[i] = 0L;
                    updated = true;
                }
            }
        }
        return updated;
    }

    public void onDelete(Object o, Serializable serializable, Object[] objects, String[] strings, Type[] types)
            throws CallbackException {
        if (o instanceof Auditable) {
            stateSets.getDeletes().add(o);
        }
    }

    @SuppressWarnings("unchecked")
	public void postFlush(Iterator iterator) throws CallbackException {
        
        try {
            Collection<AuditEntry> auditEntries = new HashSet<AuditEntry>();
            AuditFieldChangeFormatter formatter;
            Date dateCreated = new Date();
            String user = getUsername();
            for (Object o : stateSets.getInserts()) {
                Auditable auditable = (Auditable) o;
                if (null != (formatter = getFormatter(auditable))) {
                    AuditEntry auditEntry = formatter.createInsertAuditItems(auditable);
                    updateAuditEntry(auditEntry, dateCreated, user);
                    auditEntries.add(auditEntry);
                }
            }
            for (UpdateState state : stateSets.getUpdates()) {
                if (null != (formatter = getFormatter(state.currentState))) {
                    AuditEntry auditEntry = formatter.createUpdateAuditItems(state.currentState, state.previousState);
                    /* TODO maybe we should consider leaving this here
                    //Only make an audit entry if there are changes
                    if (auditEntry.getAuditEntryFieldChange().size() > 0) {
                        updateAuditEntry(auditEntry, dateCreated, user);
                        auditEntries.add(auditEntry);
                    } */
                    updateAuditEntry(auditEntry, dateCreated, user);
                    auditEntries.add(auditEntry);
                }
            }
            for (Object o1 : stateSets.getDeletes()) {
                Auditable auditable = (Auditable) o1;
                if (null != (formatter = getFormatter(auditable))) {
                    AuditEntry auditEntry = formatter.createDeleteAuditItems(auditable);
                    updateAuditEntry(auditEntry, dateCreated, user);
                    auditEntries.add(auditEntry);
                }
            }
            auditLogWriter.save(auditEntries);
        } catch (HibernateException e) {
            clearStateSets();
            throw new RuntimeException(e);
        } finally {
            clearStateSets();
        }
    }

    private void updateAuditEntry(AuditEntry auditEntry, Date dateCreated, String user) {
            auditEntry.setDateCreated(dateCreated);
            auditEntry.setUser(user);
            //new version
            auditEntry.setVersion(auditEntry.getVersion());
    }

    private AuditFieldChangeFormatter getFormatter(Auditable auditable) {
        for (AuditFieldChangeFormatter formatter : auditFormatters) {
            if (formatter.supports(auditable.getClass())) {
                return formatter;
            }
        }
        return null;
    }

    /**
     * TODO get the logged in user id from the thread local user context
     *
     * @return user id
     */
    private String getUsername() {
        return AuthHelper.getUsername();
    }

    /**
     * clear the state sets
     */
    private void clearStateSets() {
        stateSets.getInserts().clear();
        stateSets.getUpdates().clear();
        stateSets.getDeletes().clear();
    }

 
    /**
     * thread-local class to manage 3 sets (for inserts, updates, and deletes)
     */
    @SuppressWarnings("unchecked")
	private static class ThreadLocalSets extends ThreadLocal {
        static int INSERT = 0;
        static int UPDATE = 1;
        static int DELETE = 2;

        public Object initialValue() {
            Set[] sets = new HashSet[3];
            sets[INSERT] = new HashSet<Object>();
            sets[UPDATE] = new HashSet<UpdateState>();
            sets[DELETE] = new HashSet<Object>();
            return sets;
        }

        @SuppressWarnings("unchecked")
        public HashSet<Object> getInserts() {
            return ((HashSet<Object>[]) super.get())[INSERT];
        }

        @SuppressWarnings("unchecked")
        public HashSet<UpdateState> getUpdates() {
            return ((HashSet<UpdateState>[]) super.get())[UPDATE];
        }

        @SuppressWarnings("unchecked")
        public HashSet<Object> getDeletes() {
            return ((HashSet<Object>[]) super.get())[DELETE];
        }
    }

    /**
     * class to encapsulate the states of an entity that has been updated
     */
    public class UpdateState {
        Auditable currentState;
        Auditable previousState;

        public UpdateState(Auditable currentState, Auditable previousState) {
            this.currentState = currentState;
            this.previousState = previousState;
        }
    }

}
