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
package org.tsm.concharto.audit;

import java.util.Map;

import org.tsm.concharto.model.Auditable;
import org.tsm.concharto.model.audit.AuditEntry;
import org.tsm.concharto.model.wiki.WikiText;


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
