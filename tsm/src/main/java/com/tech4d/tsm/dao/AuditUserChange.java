package com.tech4d.tsm.dao;

import com.tech4d.tsm.model.Auditable;
import com.tech4d.tsm.model.audit.AuditEntry;

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
