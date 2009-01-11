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
package com.tech4d.tsm.audit;

import java.util.Map;

import com.tech4d.tsm.model.Auditable;
import com.tech4d.tsm.model.audit.AuditEntry;
import com.tech4d.tsm.model.wiki.WikiText;

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
