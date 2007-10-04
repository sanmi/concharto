package com.tech4d.tsm.dao;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.SessionFactory;
import org.springframework.transaction.annotation.Transactional;

import com.tech4d.tsm.model.Auditable;
import com.tech4d.tsm.model.audit.AuditEntry;
import com.tech4d.tsm.util.ClassName;
import com.tech4d.tsm.util.LapTimer;

/**
 * class to persist audit log entities
 */
@Transactional
public class AuditEntryDaoHib implements AuditEntryDao {
    protected final Log logger = LogFactory.getLog(getClass());
    private SessionFactory sessionFactory;

    /*
     * (non-Javadoc)
     * 
     * @see com.tech4d.tsm.lab.AuditLogDao#setSessionFactory(org.hibernate.SessionFactory)
     */
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    private static final String GET_AUDIT_ENTRIES_SQL = 
        "select auditEntry from AuditEntry auditEntry " +
        "where auditEntry.entityClass = :className " +
        "and auditEntry.entityId = :id " +
        "order by auditEntry.version desc";
    @SuppressWarnings("unchecked")
    public List<AuditEntry> getAuditEntries(Auditable auditable, int firstResult, int maxResults) {
        LapTimer timer = new LapTimer(this.logger);
        String className = ClassName.getClassName(auditable);
        List auditEntries = this.sessionFactory.getCurrentSession()
            .createQuery(GET_AUDIT_ENTRIES_SQL)
            .setString("className", className)
            .setLong("id", auditable.getId())
            .setFirstResult(firstResult)
            .setMaxResults(maxResults)
            .list();
        timer.timeIt("search").logDebugTime();
        return auditEntries;
    }
}
