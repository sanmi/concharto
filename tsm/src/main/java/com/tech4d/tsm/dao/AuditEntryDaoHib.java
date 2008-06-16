package com.tech4d.tsm.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.transaction.annotation.Transactional;

import com.tech4d.tsm.model.Auditable;
import com.tech4d.tsm.model.Event;
import com.tech4d.tsm.model.audit.AuditEntry;
import com.tech4d.tsm.model.audit.AuditFieldChange;
import com.tech4d.tsm.util.ClassName;
import com.tech4d.tsm.util.LapTimer;

/**
 * class to persist audit log entities
 */
@Transactional
public class AuditEntryDaoHib implements AuditEntryDao {
    private static final String FIELD_ID = "id";
    private static final String FIELD_USER = "user";
    private static final String FIELD_CLASS_NAME = "className";
    private static final String FIELD_CATALOG = "catalog";
    protected final Log log = LogFactory.getLog(getClass());
    private SessionFactory sessionFactory;

    private static final String SUB_AUDIT_ENTRIES_HQL = 
    	"from AuditEntry auditEntry " +
    	"where auditEntry.entityClass = :className ";

    private static final String SUB_AUDIT_ENTRIES_FOR_ID_HQL = 
    	SUB_AUDIT_ENTRIES_HQL +
        "and auditEntry.entityId = :id ";
   
    private static final String AUDIT_ENTRIES_HQL = 
    	"select auditEntry " + SUB_AUDIT_ENTRIES_FOR_ID_HQL + " order by auditEntry.version desc";
    
    private static final String SUB_AUDIT_ENTRIES_FOR_USER_HQL = 
    	SUB_AUDIT_ENTRIES_HQL +
    	"and auditEntry.user = :user ";

	private static final String ENTITY_TABLE = "[ENTITY_TABLE]";
	private static final String CATALOG_PLACEHOLDER = "[CATALOG_PLACEHOLDER]";

    private static final String SUB_RECENT_AUDIT_ENTRIES_SQL = 
    	"select a.*, e.* from AuditEntry a " +
    	"left join " + ENTITY_TABLE + " e ON a.entityId = e.id " +
    	"where a.entityClass = :className " + CATALOG_PLACEHOLDER; 

    private static final String SUB_CLAUSE_CATALOG = 
    	" and e.catalog = :catalog ";
    
    private static final String RECENT_AUDIT_ENTRIES_BY_USER_SQL = 
		SUB_RECENT_AUDIT_ENTRIES_SQL +
    	"and a.User = :user " + 
    	"order by a.dateCreated desc"; 

    private static final String RECENT_AUDIT_ENTRIES_SQL = 
    	SUB_RECENT_AUDIT_ENTRIES_SQL +
    	"order by a.dateCreated desc"; 
    
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
     * 
     * @see com.tech4d.tsm.lab.AuditLogDao#setSessionFactory(org.hibernate.SessionFactory)
     */
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    /*
     * (non-Javadoc)
     * @see com.tech4d.tsm.dao.AuditEntryDao#getAuditEntries(com.tech4d.tsm.model.Auditable, int, int)
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


		public AuditEntryQueryHandler(SessionFactory session, String user,
				Class<?> clazz, int firstResult, int maxResults, String catalog) {
			super();
			this.session = session;
			this.user = user;
			this.clazz = clazz;
			this.firstResult = firstResult;
			this.maxResults = maxResults;
			this.catalog = catalog;
		}

		public AuditEntryQueryHandler(SessionFactory sessionFactory, String user, 
				Class<?> clazz, int firstResult, int maxResults){
			this.session = sessionFactory;
			this.user = user;
			this.clazz = clazz;
			this.firstResult = firstResult;
			this.maxResults = maxResults;
		}

		/**
		 * Template method which does the work
		 * 
		 * @param sql query sql to use, including some substitution text
		 * @return
		 */
		@SuppressWarnings("unchecked")
		public List<AuditUserChange> getAuditEntriesAndLogTimingInfo(String sql){
			LapTimer timer = new LapTimer(log);
			
			//The auditable table could join with a number of entities, based on the entity Class
			//so we have to do some substitution in the select clause in order to set up the query
			sql = StringUtils.replace(sql, ENTITY_TABLE, clazz.getSimpleName());
			//if catalog was specified
			if (catalog != null) {
				sql = StringUtils.replace(sql, CATALOG_PLACEHOLDER, SUB_CLAUSE_CATALOG);
			} else {
				sql = StringUtils.replace(sql, CATALOG_PLACEHOLDER, "");
			}
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
		 * Method to be implemented by anonymous inner class the does the specifics
		 * @return List of audit records
		 */
		abstract protected Query getAuditEntriesQuery(Session session, String sql);
	}
	
	/*
	 * @see com.tech4d.tsm.dao.AuditEntryDao#getAuditEntries(java.lang.String, java.lang.Class, int, int)
	 */
	public List<AuditUserChange> getAuditEntries(
			String user, Class<?> clazz, int firstResult, int maxResults ) {

		AuditEntryQueryHandler handler = new AuditEntryQueryHandler(sessionFactory, user, clazz, firstResult, maxResults) {
			@SuppressWarnings("unchecked")
			public Query getAuditEntriesQuery(Session session, String sql) {
				return session.createSQLQuery(sql)
	        	.addEntity("a", AuditEntry.class)
	        	.addEntity("e", Event.class)
	        	.setString(FIELD_CLASS_NAME, clazz.getSimpleName())
	            .setString(FIELD_USER, user)
	            .setFirstResult(firstResult)
	            .setMaxResults(maxResults);
			}
		};
		
		return handler.getAuditEntriesAndLogTimingInfo(RECENT_AUDIT_ENTRIES_BY_USER_SQL);
	}

	/*
	 * @see com.tech4d.tsm.dao.AuditEntryDao#getLatestAuditEntries(java.lang.Class, int, int)
	 */
	public List<AuditUserChange> getLatestAuditEntries(Class<?> clazz,
			int firstResult, int maxResults) {

		AuditEntryQueryHandler handler = new AuditEntryQueryHandler(sessionFactory, null, clazz, firstResult, maxResults) {
			@SuppressWarnings("unchecked")
			public Query getAuditEntriesQuery(Session session, String sql) {		
				return session.createSQLQuery(sql)
	        	.addEntity("a", AuditEntry.class)
	        	.addEntity("e", clazz)
	        	.setString(FIELD_CLASS_NAME, clazz.getSimpleName())
	            .setFirstResult(firstResult)
	            .setMaxResults(maxResults);
			}
		};
			
		return handler.getAuditEntriesAndLogTimingInfo(RECENT_AUDIT_ENTRIES_SQL);
	}

	public List<AuditUserChange> getLatestEventEntries(String catalog, int firstResult, int maxResults) {

		AuditEntryQueryHandler handler = 
			new AuditEntryQueryHandler(sessionFactory, null, Event.class, firstResult, maxResults, catalog) {
			@SuppressWarnings("unchecked")
			public Query getAuditEntriesQuery(Session session, String sql) {		
				Query query = session.createSQLQuery(sql)
	        	.addEntity("a", AuditEntry.class)
	        	.addEntity("e", clazz)
	        	.setString(FIELD_CLASS_NAME, clazz.getSimpleName())
	            .setFirstResult(firstResult)
	            .setMaxResults(maxResults);
				if (catalog != null) {
					query.setString(FIELD_CATALOG, catalog);
				}
				return query;
			}
		};
			
		return handler.getAuditEntriesAndLogTimingInfo(RECENT_AUDIT_ENTRIES_SQL);
	}

	/*
	 * @see com.tech4d.tsm.dao.AuditEntryDao#getAuditEntriesCount(com.tech4d.tsm.model.Auditable)
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
	 * @see com.tech4d.tsm.dao.AuditEntryDao#getAuditEntriesCount(java.lang.String, java.lang.Class)
	 */
	@SuppressWarnings("unchecked")
	public Long getAuditEntriesCount(String user, Class<?> clazz) {
        LapTimer timer = new LapTimer(this.log);
        List auditEntries = this.sessionFactory.getCurrentSession()
            .createQuery(getCountHQL(SUB_AUDIT_ENTRIES_FOR_USER_HQL))
            .setString(FIELD_CLASS_NAME, clazz.getSimpleName())
            .setString(FIELD_USER, user)
            .list();
        timer.timeIt("count").logDebugTime();
        return (Long) auditEntries.get(0);
	}

	/*
	 * @see com.tech4d.tsm.dao.AuditEntryDao#getAuditEntriesCount(java.lang.Class)
	 */
	@SuppressWarnings("unchecked")
	public Long getAuditEntriesCount(Class<?> clazz) {
		LapTimer timer = new LapTimer(this.log);
		List auditEntries = this.sessionFactory.getCurrentSession()
		.createQuery(getCountHQL(SUB_AUDIT_ENTRIES_HQL))
		.setString(FIELD_CLASS_NAME, clazz.getSimpleName())
		.list();
		timer.timeIt("count").logDebugTime();
		return (Long) auditEntries.get(0);
	}

	/*
	 * @see com.tech4d.tsm.dao.AuditEntryDao#getAuditFieldChange(java.lang.Long)
	 */
	public AuditFieldChange getAuditFieldChange(Long id) {		
		return (AuditFieldChange) this.sessionFactory.getCurrentSession().get(AuditFieldChange.class, id);
	}

	/*
	 * @see com.tech4d.tsm.dao.AuditEntryDao#update(com.tech4d.tsm.model.audit.AuditEntry)
	 */
	public void update(AuditEntry auditEntry) {
		this.sessionFactory.getCurrentSession().update(auditEntry);
	}
}
