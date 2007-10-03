package com.tech4d.tsm.dao;

import java.util.List;

import org.hibernate.SessionFactory;

import com.tech4d.tsm.model.Auditable;
import com.tech4d.tsm.model.audit.AuditEntry;

/**
 * Dao for querying audit records
 * @author frank
 */
public interface AuditEntryDao {

    public void setSessionFactory(SessionFactory sessionFactory);

    public List<AuditEntry> getAuditEntries(Auditable auditable, int firstResult, int maxResults);

}