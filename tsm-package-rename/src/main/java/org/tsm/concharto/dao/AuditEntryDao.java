/*******************************************************************************
 * ***** BEGIN LICENSE BLOCK *****
 * Version: MPL 1.1
 * 
 * The contents of this file are subject to the Mozilla Public License Version 
 * 1.1 (the "License"); you may not use this file except in compliance with 
 * the License. You may obtain a copy of the License at 
 * http://www.mozilla.org/MPL/
 * 
 * Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 * for the specific language governing rights and limitations under the
 * License.
 * 
 * The Original Code is Concharto.
 * 
 * The Initial Developer of the Original Code is
 * Time Space Map, LLC
 * Portions created by the Initial Developer are Copyright (C) 2007 - 2009
 * the Initial Developer. All Rights Reserved.
 * 
 * Contributor(s):
 * Time Space Map, LLC
 * 
 * ***** END LICENSE BLOCK *****
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
     * @param user username
     * @param clazz class of auditable object (e.g. Event.class)
     * @param firstResult record to start the results at 
     * @param maxResults max results to return
     * @return list of AuditEntry objects ordered by most recent version
     */
    public List<AuditUserChange> getAuditEntries(String user, Class<?> clazz, int firstResult, int maxResults);

    /**
     * Get the latest changes
     * @param clazz class of auditable object (e.g. Event.class)
     * @param firstResult record to start the results at 
     * @param maxResults max results to return
     * @return list of AuditEntry objects ordered by most recent version
     */
    public List<AuditUserChange> getLatestAuditEntries(Class<?> clazz, int firstResult, int maxResults);

	/**
	 * Get the latest changes
	 * @param catalog the catalog for the Event
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
     * @param user username
     * @param clazz class of auditable object (e.g. Event.class)
     * @return total number of available AuditEntry objects 
     */
    public Long getAuditEntriesCount(String user, Class<?> clazz);

    /**
     * Get the total number of AuditEntry objects available for a given user, Auditable
     * @param clazz class of auditable object (e.g. Event.class)
     * @return total number of available AuditEntry objects 
     */
    public Long getAuditEntriesCount(Class<?> clazz);

    public AuditFieldChange getAuditFieldChange(Long id);

	public void update(AuditEntry auditEntry);

}
