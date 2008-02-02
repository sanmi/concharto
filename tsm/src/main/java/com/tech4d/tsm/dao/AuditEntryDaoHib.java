package com.tech4d.tsm.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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

    private static final String SUB_RECENT_AUDIT_ENTRIES_SQL = 
    	"select a.*, e.* from AuditEntry a " +
    	"left join " + ENTITY_TABLE + " e ON a.entityId = e.id " +
    	"where a.entityClass = :className " ;
    
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
	 * Anonymous inner class for processing the sql query 
	 */
	interface AuditEntryQueryHandler {
		public List<Object[]> getAuditEntries(String user, Class<?> clazz, int firstResult, int maxResults,
				SessionFactory sessionFactory, String sql);
	}

	/**
	 * Search method uses a query handler for providing the exact query params
	 * @param handler an implementation of AuditEntryQueryHandler
	 * @param sql query sql to use, including some substitution text
     * @param user username
     * @param clazz class of auditable object (e.g. Event.class)
     * @param firstResult record to start the results at 
     * @param maxResults max results to return
	 * @return list of AuditEntry objects ordered by most recent version
	 */
	@SuppressWarnings("unchecked")
	private List<AuditUserChange> getAuditEntries(
			AuditEntryQueryHandler handler, String sql, String user, Class<?> clazz, int firstResult, int maxResults) {
		LapTimer timer = new LapTimer(this.log);
		
		//The auditable table could join with a number of entities, based on the entity Class
		//so we have to do some substitution in the select clause in order to set up the query
		sql = StringUtils.replace(sql, ENTITY_TABLE, clazz.getSimpleName());
		
		List<Object[]> results = handler.getAuditEntries(user, clazz, firstResult, maxResults, sessionFactory, sql);
		
		List<AuditUserChange> auditUserChanges = new ArrayList();
		for (Object[] pair : results) {
			AuditUserChange auditUserChange = 
				new AuditUserChange((AuditEntry)pair[0],(Auditable)pair[1]);
			auditUserChanges.add(auditUserChange);
		}
		timer.timeIt("changes").logDebugTime();
		return auditUserChanges;
	}
	
	/*
	 * @see com.tech4d.tsm.dao.AuditEntryDao#getAuditEntries(java.lang.String, java.lang.Class, int, int)
	 */
	public List<AuditUserChange> getAuditEntries(
			String user, Class<?> clazz, int firstResult, int maxResults ) {

		return getAuditEntries(
				//anonymous inner class
				new AuditEntryQueryHandler() {
					@SuppressWarnings("unchecked")
					public List<Object[]> getAuditEntries(String user, Class<?> clazz, int firstResult, 
							int maxResults, SessionFactory sessionFactory, String sql) {
						
						return sessionFactory.getCurrentSession()
			        	.createSQLQuery(sql)
			        	.addEntity("a", AuditEntry.class)
			        	.addEntity("e", Event.class)
			        	.setString(FIELD_CLASS_NAME, clazz.getSimpleName())
			            .setString(FIELD_USER, user)
			            .setFirstResult(firstResult)
			            .setMaxResults(maxResults)
			        	.list();
					}
					
				}
				,RECENT_AUDIT_ENTRIES_BY_USER_SQL, user, clazz, firstResult, maxResults);
	}

	/*
	 * @see com.tech4d.tsm.dao.AuditEntryDao#getLatestAuditEntries(java.lang.Class, int, int)
	 */
	public List<AuditUserChange> getLatestAuditEntries(Class<?> clazz,
			int firstResult, int maxResults) {
		return getAuditEntries(
				//anonymous inner class
				new AuditEntryQueryHandler() {
					@SuppressWarnings("unchecked")
					public List<Object[]> getAuditEntries(String user, Class<?> clazz, int firstResult, 
							int maxResults, SessionFactory sessionFactory, String sql) {
						
						return sessionFactory.getCurrentSession()
			        	.createSQLQuery(sql)
			        	.addEntity("a", AuditEntry.class)
			        	.addEntity("e", clazz)
			        	.setString(FIELD_CLASS_NAME, clazz.getSimpleName())
			            .setFirstResult(firstResult)
			            .setMaxResults(maxResults)
			        	.list();
					}
					
				}
				,RECENT_AUDIT_ENTRIES_SQL, null, clazz, firstResult, maxResults);
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
