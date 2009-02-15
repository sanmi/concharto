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
import org.tsm.concharto.model.audit.AuditEntry;


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
