package com.tech4d.tsm.audit;

import java.util.Map;

import com.tech4d.tsm.model.Auditable;
import com.tech4d.tsm.model.audit.AuditEntry;

public interface AuditFieldChangeFormatter {
    @SuppressWarnings("unchecked")
	public boolean supports(Class clazz);
    public void refresh(Auditable auditable);
    public AuditEntry createInsertAuditItems(Auditable auditable);
    public AuditEntry createUpdateAuditItems(Auditable currentInstance, Auditable previousInstance);
    public AuditEntry createDeleteAuditItems(Auditable auditable);
    public Auditable revertEntity(Auditable auditable, Map<Integer, String> changeList);

}
