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

import org.tsm.concharto.model.Auditable;
import org.tsm.concharto.model.audit.AuditEntry;

/**
 * Value object class for audit search results
 */
public class AuditUserChange {
	private AuditEntry auditEntry;
	private Auditable auditable;

	public AuditUserChange() {
		super();
	}
	public AuditUserChange(AuditEntry auditEntry, Auditable auditable) {
		super();
		this.auditEntry = auditEntry;
		this.auditable = auditable;
	}
	public AuditEntry getAuditEntry() {
		return auditEntry;
	}
	public void setAuditEntry(AuditEntry auditEntry) {
		this.auditEntry = auditEntry;
	}
	public Auditable getAuditable() {
		return auditable;
	}
	public void setAuditable(Auditable auditable) {
		this.auditable = auditable;
	}
}
