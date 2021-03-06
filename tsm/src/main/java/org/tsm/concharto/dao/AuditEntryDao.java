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

import java.util.List;

import org.hibernate.SessionFactory;
import org.tsm.concharto.model.Auditable;
import org.tsm.concharto.model.audit.AuditEntry;
import org.tsm.concharto.model.audit.AuditFieldChange;


/**
 * Dao for querying audit records
 * @author frank
 */
public interface AuditEntryDao {

    public void setSessionFactory(SessionFactory sessionFactory);

    /**
     * Get audit entries for a given auditable (e.g. Event).  Paging fields allow returning
     * a subset of the results
     * @param auditable Auditable object - must have at a minimum the id present
     * @param firstResult record to start the results at 
     * @param maxResults max results to return
     * @return list of AuditEntry objects ordered by most recent version
     */
    public List<AuditEntry> getAuditEntries(Auditable auditable, int firstResult, int maxResults);

    /**
     * Get audit entries for a given user and auditable.  Paging fields allow returning
     * a subset of the results
     * @param catalog the catalog for the entity 
     * @param user username
     * @param clazz class of auditable object (e.g. Event.class)
     * @param firstResult record to start the results at 
     * @param maxResults max results to return
     * @return list of AuditEntry objects ordered by most recent version
     */
    public List<AuditUserChange> getAuditEntries(String catalog, String user, Class<?> clazz, int firstResult, int maxResults);

    /**
     * Get the latest changes
     * @param catalog the catalog for the entity 
     * @param clazz class of auditable object (e.g. Event.class)
     * @param firstResult record to start the results at 
     * @param maxResults max results to return
     * @return list of AuditEntry objects ordered by most recent version
     */
    public List<AuditUserChange> getLatestAuditEntries(String catalog, Class<?> clazz, int firstResult, int maxResults);

	/**
	 * Get the latest changes
	 * @param catalog the catalog for the entity 
     * @param firstResult record to start the results at 
     * @param maxResults max results to return
     * @return list of AuditEntry objects ordered by most recent version
	 */
    public List<AuditUserChange> getLatestEventEntries(String catalog, int firstResult, int maxResults);

    /**
     * Get the total number of AuditEntry objects available for a given Auditable
     * @param auditable Auditable object - must have at a minimum the id present
     * @return total number of available AuditEntry objects 
     */
    public Long getAuditEntriesCount(Auditable auditable);

    /**
     * Get the total number of AuditEntry objects available for a given user, Auditable
     * @param catalog the catalog for the entity 
     * @param user username
     * @param clazz class of auditable object (e.g. Event.class)
     * @return total number of available AuditEntry objects 
     */
    public Long getAuditEntriesCount(String catalog, String user, Class<?> clazz);

    /**
     * Get the total number of AuditEntry objects available for a given user, Auditable
     * @param catalog the catalog for the entity 
     * @param clazz class of auditable object (e.g. Event.class)
     * @return total number of available AuditEntry objects 
     */
    public Long getAuditEntriesCount(String catalog, Class<?> clazz);

    public AuditFieldChange getAuditFieldChange(Long id);

	public void update(AuditEntry auditEntry);

}
