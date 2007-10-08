package com.tech4d.tsm.audit;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.CallbackException;
import org.hibernate.EmptyInterceptor;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.type.Type;

import com.tech4d.tsm.dao.AuditLogWriter;
import com.tech4d.tsm.model.Auditable;
import com.tech4d.tsm.model.audit.AuditEntry;

/**
 * hibernate interceptor to create AuditEntry records for all classes implementing the Auditable interface.  It will
 * record insertions, updates, and deletions.
 */
public class AuditInterceptor extends EmptyInterceptor {
    private static final long serialVersionUID = 1L;
    private Log log = LogFactory.getLog(this.getClass());
    private ThreadLocalSets stateSets = new ThreadLocalSets();   // this is thread-safe
    private AuditLogWriter auditLogWriter;
    private SessionFactory sessionFactory;
    Set<AuditFieldChangeFormatter> auditFormatters = new HashSet<AuditFieldChangeFormatter>();
    

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

    public void postFlush(Iterator iterator) throws CallbackException {
        
        try {
            Collection<AuditEntry> auditEntries = new HashSet<AuditEntry>();
            AuditFieldChangeFormatter formatter;
            Date dateCreated = new Date();
            String user = getUserLoggedInUserId();
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
                    //Only make an audit entry if there are changes
                    if (auditEntry.getAuditEntryFieldChange().size() > 0) {
                        updateAuditEntry(auditEntry, dateCreated, user);
                        auditEntries.add(auditEntry);
                    }
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
    private String getUserLoggedInUserId() {
//        UserContext userContext = ThreadLocalUserContext.getUserContext();
//        return userContext.getUserId();
        return null;
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