/*******************************************************************************
 * Copyright 2009 Time Space Map, LLC
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package org.tsm.concharto.dao;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.transaction.annotation.Transactional;
import org.tsm.concharto.model.Auditable;
import org.tsm.concharto.model.Event;
import org.tsm.concharto.model.audit.AuditEntry;
import org.tsm.concharto.model.audit.AuditFieldChange;
import org.tsm.concharto.util.ClassName;
import org.tsm.concharto.util.LapTimer;


/**
 * Class to persist and retturn audit log entities.  Note: the 
 * audit framework should be refactored (too much entity specific
 * behavior, too complex, hard to revert changes to db)  We should
 * probably just do it the way wikipedia does it (fsm 4-25-09)
 */
@Transactional
public class AuditEntryDaoHib implements AuditEntryDao {
    private static final String FIELD_ID = "id";
    private static final String FIELD_USER = "user";
    private static final String FIELD_CLASS_NAME = "className";
    private static final String FIELD_CATALOG = "catalog";
    protected final Log log = LogFactory.getLog(getClass());
    private SessionFactory sessionFactory;

    private static final String CATALOG_PLACEHOLDER = "[CATALOG_PLACEHOLDER]";
    private static final String VISIBLE_PLACEHOLDER = "[VISIBLE_PLACEHOLDER]";

    private static final String SUB_AUDIT_ENTRIES_HQL = 
        "from AuditEntry auditEntry " +
        "where auditEntry.entityClass = :className ";

    private static final String SUB_AUDIT_ENTRIES_FOR_ID_HQL = 
        SUB_AUDIT_ENTRIES_HQL +
        "and auditEntry.entityId = :id ";

    private static final String AUDIT_ENTRIES_HQL = 
        "select auditEntry " + SUB_AUDIT_ENTRIES_FOR_ID_HQL + " order by auditEntry.version desc";
    
    private static final String ENTITY_TABLE = "[ENTITY_TABLE]";

    private static final String SUB_SELECT_SQL_USERCHANGE = 
        "select a.*, e.* from AuditEntry a ";

    private static final String SUB_SELECT_SQL_AUDITENTRY_COUNT = 
        "select count(*) from AuditEntry a ";
    
    private static final String SUB_JOIN_SQL =
        "left join " + ENTITY_TABLE + " e ON a.entityId = e.id ";

    private static final String SUB_CLAUSE_CATALOG = 
        " and e.catalog = :catalog ";

    private static final String SUB_CLAUSE_VISIBLE = 
        " and (e.visible = true or e.visible is null) ";

    private static final String SUB_WHERE_SQL = 
        "where a.entityClass = :className " + CATALOG_PLACEHOLDER + VISIBLE_PLACEHOLDER; 

    private static final String SUB_AUDIT_USERCHANGE_SQL =  
        SUB_SELECT_SQL_USERCHANGE + SUB_JOIN_SQL + SUB_WHERE_SQL;

    private static final String AUDIT_ENTRY_SQL_COUNT =  
        SUB_SELECT_SQL_AUDITENTRY_COUNT + SUB_JOIN_SQL + SUB_WHERE_SQL;

    private static final String AUDIT_ENTRY_SQL_COUNT_BY_USER =  
        SUB_SELECT_SQL_AUDITENTRY_COUNT + SUB_JOIN_SQL + SUB_WHERE_SQL + 
        "and a.User = :user "; 

    private static final String RECENT_AUDIT_USERCHANGE_BY_USER_SQL = 
		SUB_AUDIT_USERCHANGE_SQL +
    	"and a.User = :user " + 
    	"order by a.dateCreated desc"; 

    private static final String RECENT_AUDIT_USERCHANGE_SQL = 
    	SUB_AUDIT_USERCHANGE_SQL +
    	"order by a.dateCreated desc"; 


    /*
     * (non-Javadoc)
     * 
     * @see org.tsm.concharto.lab.AuditLogDao#setSessionFactory(org.hibernate.SessionFactory)
     */
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    /**
     * Construct the count query based on a sub-select phrase 
     * @param subSelect hql phrase
     * @return count
     */
    private String getCountHQL(String subSelect) {
        return "select count(auditEntry) " + subSelect; 
    }

    /*
     * (non-Javadoc)
     * @see org.tsm.concharto.dao.AuditEntryDao#getAuditEntries(org.tsm.concharto.model.Auditable, int, int)
     */
    @SuppressWarnings("unchecked")
    public List<AuditEntry> getAuditEntries(Auditable auditable, int firstResult, int maxResults) {
        LapTimer timer = new LapTimer(this.log);
        String className = ClassName.getClassName(auditable);
        List auditEntries = this.sessionFactory.getCurrentSession()
            .createQuery(AUDIT_ENTRIES_HQL)
            .setString(FIELD_CLASS_NAME, className)
            .setLong(FIELD_ID, auditable.getId())
            .setFirstResult(firstResult)
            .setMaxResults(maxResults)
            .list();
        timer.timeIt("changes").logDebugTime();
        return auditEntries;
    }
    

	/**
	 * Inner class for processing the sql query 
	 *
	 * Search method uses a query handler for providing the exact query params
	 * @param handler an implementation of AuditEntryQueryHandler
     * @param user username
     * @param clazz class of auditable object (e.g. Event.class)
     * @param firstResult record to start the results at 
     * @param maxResults max results to return
	 * @return list of AuditEntry objects ordered by most recent version
	 */
	abstract class AuditEntryQueryHandler {
		SessionFactory session;
		String user;
		Class<?> clazz;
		int firstResult, maxResults;
		String catalog;
		Long auditableId;


		public AuditEntryQueryHandler(SessionFactory session, String catalog,
				String user, Class<?> clazz, int firstResult, int maxResults) {
			super();
			this.session = session;
            this.catalog = catalog;
			this.user = user;
			this.clazz = clazz;
			this.firstResult = firstResult;
			this.maxResults = maxResults;
		}
		public AuditEntryQueryHandler(SessionFactory session, String catalog,
		        String user, Class<?> clazz) {
		    super();
		    this.session = session;
            this.catalog = catalog;
		    this.user = user;
		    this.clazz = clazz;
		}

		/**
		 * Template method which does the work -
		 * 
		 * @param sql query sql to use, including some substitution text
		 * @return a List ofr AuditUserChange objects
		 */
		@SuppressWarnings("unchecked")
		public List<AuditUserChange> getAuditUserChanges(String sql){
			LapTimer timer = new LapTimer(log);
			
			sql = doClauselReplacement(sql);
			List<Object[]> results = getAuditEntriesQuery(sessionFactory.getCurrentSession(), sql).list();
			
			List<AuditUserChange> auditUserChanges = new ArrayList<AuditUserChange>();
			for (Object[] pair : results) {
				AuditUserChange auditUserChange = 
					new AuditUserChange((AuditEntry)pair[0],(Auditable)pair[1]);
				auditUserChanges.add(auditUserChange);
			}
			timer.timeIt("changes").logDebugTime();
			return auditUserChanges;
		}

		/**
		 * Template function that does the work 
		 * @param sql
		 * @return the total count of records
		 */
        @SuppressWarnings("unchecked")
		public Long getAuditableCount(String sql){
		    LapTimer timer = new LapTimer(log);
		    
		    sql = doClauselReplacement(sql);
		    List<BigInteger> results = getAuditEntriesQuery(sessionFactory.getCurrentSession(), sql).list();
		    timer.timeIt("changes").logDebugTime();
		    return results.get(0).longValue();
		}
		
	    /**
	     * The auditable table could join with a number of entities, based on the entity Class
	     * so we have to do some substitution in the select clause in order to set up the query
	     * @param sql
	     * @return sql query with replaced values
	     */
	    private String doClauselReplacement(String sql) {
	        //
	        sql = StringUtils.replace(sql, ENTITY_TABLE, clazz.getSimpleName());
	        //if catalog was specified (at the moment, only Event has a catalog)
	        if ((catalog != null) && clazz.equals(Event.class)) {
	            sql = StringUtils.replace(sql, CATALOG_PLACEHOLDER, SUB_CLAUSE_CATALOG);
	        } else {
	            sql = StringUtils.replace(sql, CATALOG_PLACEHOLDER, "");
	        }
	        if (clazz.equals(Event.class)) {
	            sql = StringUtils.replace(sql, VISIBLE_PLACEHOLDER, SUB_CLAUSE_VISIBLE);
	        } else {
	            sql = StringUtils.replace(sql, VISIBLE_PLACEHOLDER, "");
	        }
	        return sql;
	    }

	    /**
		 * Method to be implemented by anonymous inner class the does the specifics
		 * @return List of audit records
		 */
		abstract protected Query getAuditEntriesQuery(Session session, String sql);
	}

    /*
	 * @see org.tsm.concharto.dao.AuditEntryDao#getAuditEntries(java.lang.String, java.lang.Class, int, int)
	 */
	public List<AuditUserChange> getAuditEntries(
	        String catalog, String user, Class<?> clazz, int firstResult, int maxResults ) {

		AuditEntryQueryHandler handler = new AuditEntryQueryHandler(sessionFactory, catalog, user, clazz, firstResult, maxResults) {
			public Query getAuditEntriesQuery(Session session, String sql) {
				Query query = session.createSQLQuery(sql)
	        	.addEntity("a", AuditEntry.class)
	        	.addEntity("e", Event.class)
	        	.setString(FIELD_CLASS_NAME, clazz.getSimpleName())
	            .setString(FIELD_USER, user)
	            .setFirstResult(firstResult)
	            .setMaxResults(maxResults);
				addCatalog(query, catalog, clazz);
                return query;
			}
		};
		
		return handler.getAuditUserChanges(RECENT_AUDIT_USERCHANGE_BY_USER_SQL);
	}

	/**
	 * Custom query addition for Events only.  One of the reasons, this audit framework
	 * needs to be refactored 
	 * @param query
	 * @param catalog
	 * @param clazz
	 * @return Query with catalog field added, only if the class is an Event
	 */
	@SuppressWarnings("unchecked")
    private Query addCatalog(Query query, String catalog, Class clazz) {
        if ((catalog != null) && (clazz.equals(Event.class))) {
            query.setString(FIELD_CATALOG, catalog);
        }
        return query;
	}
	/*
	 * @see org.tsm.concharto.dao.AuditEntryDao#getLatestAuditEntries(java.lang.Class, int, int)
	 */
	public List<AuditUserChange> getLatestAuditEntries(String catalog, Class<?> clazz,
			int firstResult, int maxResults) {

		AuditEntryQueryHandler handler = new AuditEntryQueryHandler(sessionFactory, catalog, null, clazz, firstResult, maxResults) {
			public Query getAuditEntriesQuery(Session session, String sql) {		
				Query query = session.createSQLQuery(sql)
	        	.addEntity("a", AuditEntry.class)
	        	.addEntity("e", clazz)
	        	.setString(FIELD_CLASS_NAME, clazz.getSimpleName())
	            .setFirstResult(firstResult)
	            .setMaxResults(maxResults);
				addCatalog(query, catalog, clazz);
				return query;
			}
		};
			
		return handler.getAuditUserChanges(RECENT_AUDIT_USERCHANGE_SQL);
	}

	public List<AuditUserChange> getLatestEventEntries(String catalog, int firstResult, int maxResults) {

		AuditEntryQueryHandler handler = 
			new AuditEntryQueryHandler(sessionFactory, catalog, null, Event.class, firstResult, maxResults) {
			public Query getAuditEntriesQuery(Session session, String sql) {		
				Query query = session.createSQLQuery(sql)
	        	.addEntity("a", AuditEntry.class)
	        	.addEntity("e", clazz)
	        	.setString(FIELD_CLASS_NAME, clazz.getSimpleName())
	            .setFirstResult(firstResult)
	            .setMaxResults(maxResults);
				addCatalog(query, catalog, clazz);
				return query;
			}
		};
			
		return handler.getAuditUserChanges(RECENT_AUDIT_USERCHANGE_SQL);
	}

	/*
	 * @see org.tsm.concharto.dao.AuditEntryDao#getAuditEntriesCount(org.tsm.concharto.model.Auditable)
	 */
	@SuppressWarnings("unchecked")
	public Long getAuditEntriesCount(Auditable auditable) {
        LapTimer timer = new LapTimer(this.log);
        String className = ClassName.getClassName(auditable);
        List auditEntries = this.sessionFactory.getCurrentSession()
            .createQuery(getCountHQL(SUB_AUDIT_ENTRIES_FOR_ID_HQL))
            .setString(FIELD_CLASS_NAME, className)
            .setLong(FIELD_ID, auditable.getId())
            .list();
        timer.timeIt("count").logDebugTime();
        return (Long) auditEntries.get(0);
    }

	/*
	 * @see org.tsm.concharto.dao.AuditEntryDao#getAuditEntriesCount(java.lang.String, java.lang.Class)
	 */
	public Long getAuditEntriesCount(String catalog, String user, Class<?> clazz) {

        AuditEntryQueryHandler handler = new AuditEntryQueryHandler(sessionFactory, catalog, user, clazz) {
            public Query getAuditEntriesQuery(Session session, String sql) {        
                Query query = session.createSQLQuery(sql)
                .setString(FIELD_USER, user)
                .setString(FIELD_CLASS_NAME, clazz.getSimpleName());
                addCatalog(query, catalog, clazz);
                return query;
            }
        };
        return handler.getAuditableCount(AUDIT_ENTRY_SQL_COUNT_BY_USER);
	}

	/*
	 * @see org.tsm.concharto.dao.AuditEntryDao#getAuditEntriesCount(java.lang.Class)
	 */
	public Long getAuditEntriesCount(String catalog, Class<?> clazz) {

        AuditEntryQueryHandler handler = new AuditEntryQueryHandler(sessionFactory, catalog, null, clazz) {
            public Query getAuditEntriesQuery(Session session, String sql) {        
                Query query = session.createSQLQuery(sql)
                .setString(FIELD_CLASS_NAME, clazz.getSimpleName());
                addCatalog(query, catalog, clazz);
                return query;
            }
        };
        return handler.getAuditableCount(AUDIT_ENTRY_SQL_COUNT);
	}

	/*
	 * @see org.tsm.concharto.dao.AuditEntryDao#getAuditFieldChange(java.lang.Long)
	 */
	public AuditFieldChange getAuditFieldChange(Long id) {		
		return (AuditFieldChange) this.sessionFactory.getCurrentSession().get(AuditFieldChange.class, id);
	}

	/*
	 * @see org.tsm.concharto.dao.AuditEntryDao#update(org.tsm.concharto.model.audit.AuditEntry)
	 */
	public void update(AuditEntry auditEntry) {
		this.sessionFactory.getCurrentSession().update(auditEntry);
	}
}
