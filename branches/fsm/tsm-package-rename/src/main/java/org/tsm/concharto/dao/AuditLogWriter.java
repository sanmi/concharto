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