package com.tech4d.tsm.dao;

import java.io.Serializable;
import java.util.Collection;

import org.hibernate.SessionFactory;
import org.springframework.transaction.annotation.Transactional;

import com.tech4d.tsm.model.audit.AuditEntry;

/**
 * class to persist audit log entities
 */
@Transactional
public class AuditLogWriterHib implements AuditLogWriter {
    private SessionFactory sessionFactory;

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
     * 
     * @see com.tech4d.tsm.lab.AuditLogDao#save(com.tech4d.tsm.model.audit.AuditEntry)
     */
    public Serializable save(AuditEntry entry) {
        return this.sessionFactory.getCurrentSession().save(entry);
    }

    public void save(Collection<AuditEntry> auditEntries) {
        for (AuditEntry entry : auditEntries) {
            save(entry);
        }
    }

}
