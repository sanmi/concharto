package com.tech4d.tsm.audit;

import java.util.Map;

import com.tech4d.tsm.model.Auditable;
import com.tech4d.tsm.model.WikiText;
import com.tech4d.tsm.model.audit.AuditEntry;

/**
 * WikiRender chnage formatter.  TODO empty for now.
 */
public class WikiTextChangeFormatter implements AuditFieldChangeFormatter {

	public AuditEntry createDeleteAuditItems(Auditable auditable) {
		// TODO Auto-generated method stub
		return new AuditEntry();
	}

	public AuditEntry createInsertAuditItems(Auditable auditable) {
		// TODO Auto-generated method stub
		return new AuditEntry();
	}

	public AuditEntry createUpdateAuditItems(Auditable currentInstance,
			Auditable previousInstance) {
			return new AuditEntry();
	}

	public void refresh(Auditable auditable) {
		// TODO Auto-generated method stub
		
	}

	public Auditable revertEntity(Auditable auditable,
			Map<Integer, String> changeList) {
		// TODO Auto-generated method stub
		return null;
	}

	@SuppressWarnings("unchecked")
	public boolean supports(Class clazz) {
		
		return clazz == WikiText.class;
	}

}
