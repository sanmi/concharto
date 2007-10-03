package com.tech4d.tsm.dao;

import java.io.Serializable;
import java.util.Collection;

import org.hibernate.SessionFactory;

import com.tech4d.tsm.model.audit.AuditEntry;

/**
 * Dao to be used exclusively for writing audit log records by the Audit interceptor.  It
 * needs to separate from other dao's because it us used from within an interceptpr.
 * @author frank
 *
 */
public interface AuditLogWriter {

    public void setSessionFactory(SessionFactory sessionFactory);

    public Serializable save(AuditEntry entry);
    
    public void save(Collection<AuditEntry> auditEntries);

}