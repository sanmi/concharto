package com.tech4d.tsm.dao;

import com.tech4d.tsm.model.Auditable;
import com.tech4d.tsm.model.audit.AuditEntry;
import com.tech4d.tsm.model.audit.AuditFieldChange;

import org.hibernate.SessionFactory;

import java.util.List;

/**
 * Dao for querying audit records
 * @author frank
 */
public interface AuditEntryDao {

    public void setSessionFactory(SessionFactory sessionFactory);

    public List<AuditEntry> getAuditEntries(Auditable auditable, int firstResult, int maxResults);

    public Long getAuditEntriesCount(Auditable auditable);

	public AuditFieldChange getAuditFieldChange(Long id);

	public void update(AuditEntry auditEntry);

}