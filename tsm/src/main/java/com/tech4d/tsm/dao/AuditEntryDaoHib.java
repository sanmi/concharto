package com.tech4d.tsm.dao;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.SessionFactory;
import org.springframework.transaction.annotation.Transactional;

import com.tech4d.tsm.model.Auditable;
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
    private static final String FIELD_CLASS_NAME = "className";
    protected final Log logger = LogFactory.getLog(getClass());
    private SessionFactory sessionFactory;

    private static final String AUDIT_ENTRIES_HQL = 
        "from AuditEntry auditEntry " +
        "where auditEntry.entityClass = :className " +
        "and auditEntry.entityId = :id ";
        
    private static final String ALL_AUDIT_ENTRIES_HQL = 
        "select auditEntry " + AUDIT_ENTRIES_HQL + "order by auditEntry.version desc";
    private static final String COUNT_AUDIT_ENTRIES_HQL = 
        "select count(auditEntry) " + AUDIT_ENTRIES_HQL;  
        
    /*
     * (non-Javadoc)
     * 
     * @see com.tech4d.tsm.lab.AuditLogDao#setSessionFactory(org.hibernate.SessionFactory)
     */
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @SuppressWarnings("unchecked")
    public List<AuditEntry> getAuditEntries(Auditable auditable, int firstResult, int maxResults) {
        LapTimer timer = new LapTimer(this.logger);
        String className = ClassName.getClassName(auditable);
        List auditEntries = this.sessionFactory.getCurrentSession()
            .createQuery(ALL_AUDIT_ENTRIES_HQL)
            .setString(FIELD_CLASS_NAME, className)
            .setLong(FIELD_ID, auditable.getId())
            .setFirstResult(firstResult)
            .setMaxResults(maxResults)
            .list();
        timer.timeIt("search").logDebugTime();
        return auditEntries;
    }
    
    @SuppressWarnings("unchecked")
	public Long getAuditEntriesCount(Auditable auditable) {
        LapTimer timer = new LapTimer(this.logger);
        String className = ClassName.getClassName(auditable);
        List auditEntries = this.sessionFactory.getCurrentSession()
            .createQuery(COUNT_AUDIT_ENTRIES_HQL)
            .setString(FIELD_CLASS_NAME, className)
            .setLong(FIELD_ID, auditable.getId())
            .list();
        timer.timeIt("count").logDebugTime();
        return (Long) auditEntries.get(0);
    }

	public AuditFieldChange getAuditFieldChange(Long id) {		
		return (AuditFieldChange) this.sessionFactory.getCurrentSession().get(AuditFieldChange.class, id);
	}
}
