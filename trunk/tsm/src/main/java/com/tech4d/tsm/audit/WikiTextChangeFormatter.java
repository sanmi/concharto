package com.tech4d.tsm.audit;

import java.util.Map;

import com.tech4d.tsm.model.Auditable;
import com.tech4d.tsm.model.WikiText;
import com.tech4d.tsm.model.audit.AuditEntry;

/**
 * WikiRender change formatter. 
 */
public class WikiTextChangeFormatter extends BaseAuditFieldChangeFormatter {
    public static final int TEXT = 0;

	@SuppressWarnings("unchecked")
	@Override
	public boolean supports(Class clazz) {
		return clazz == WikiText.class;
	}

	@Override
	public AuditEntry createUpdateAuditItems(Auditable currentA,
			Auditable previousA) {
        AuditEntry auditEntry = makeAuditEntry(currentA, AuditEntry.ACTION_UPDATE);
        WikiText current = (WikiText) currentA;
        WikiText previous = (WikiText) previousA;

        makeChange(TEXT,  current.getText(), previous.getText(), auditEntry);
        return auditEntry;
	}

	@Override
	public void refresh(Auditable auditable) {
		// nothing needed
	}

	@Override
	public Auditable revertEntity(Auditable auditable, Map<Integer, String> changeList) {
		WikiText wikiText = (WikiText) auditable;
		for (Integer field : changeList.keySet()) {
			String change = changeList.get(field);
			if (field == TEXT) {
				wikiText.setText(change);
			} 
		}
		return wikiText;
	}

}
