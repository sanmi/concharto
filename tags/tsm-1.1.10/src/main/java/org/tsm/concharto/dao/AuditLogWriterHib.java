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

import java.io.Serializable;
import java.util.Collection;

import org.hibernate.SessionFactory;
import org.springframework.transaction.annotation.Transactional;
import org.tsm.concharto.model.audit.AuditEntry;


/**
 * class to persist audit log entities
 */
@Transactional
public class AuditLogWriterHib implements AuditLogWriter {
    private SessionFactory sessionFactory;

    /*
     * (non-Javadoc)
     * 
     * @see org.tsm.concharto.lab.AuditLogDao#setSessionFactory(org.hibernate.SessionFactory)
     */
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.tsm.concharto.lab.AuditLogDao#save(org.tsm.concharto.model.audit.AuditEntry)
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
