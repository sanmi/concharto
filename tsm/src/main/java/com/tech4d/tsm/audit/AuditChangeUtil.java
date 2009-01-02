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

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.tech4d.tsm.model.audit.AuditEntry;
import com.tech4d.tsm.model.audit.AuditFieldChange;

/**
 * Utility for reviewing diffs between revisions
 * @author frank
 *
 */
public class AuditChangeUtil {

	
	/**
	 * Get changes between revisions
	 * @param rev0 a revision number
	 * @param rev1 a second revision number
	 * @param auditEntries list of AuditEntry objects sorted by newest first.    
	 * @return A Map, keyed by auditFieldChange.getPropertyName() of changed values (as strings)
	 */
	public static Map<Integer, String> getChanges(int rev0, int rev1, List<AuditEntry> auditEntries) {
		//get things straight.  We assume the list is sorted newest first
		int young, old;
		if (rev0 < rev1) {
			young = rev0;
			old = rev1;
		} else {
			young = rev1;
			old = rev0;
		}
		// the zeroth element is the "create" record so we are really counting from the 
		// second record (this -2) audit entries are by most recent so we go backwards
		old = auditEntries.size()-old-1;
		young = auditEntries.size()-young-2;
		//Find out if there were changes between the most current rev and this one
		Map<Integer, String> revertList = new HashMap<Integer, String>();
		for (int i = young; i>=old; i--) {
			AuditEntry auditEntry = auditEntries.get(i);
			Collection<AuditFieldChange> changes = auditEntry.getAuditEntryFieldChange();
			for (AuditFieldChange auditFieldChange : changes) {
				if (!revertList.containsKey(auditFieldChange.getPropertyName())) {
					revertList.put(auditFieldChange.getPropertyName(), auditFieldChange.getOldValue());
					//System.out.println("Field: " + auditFieldChange.getPropertyName() + "=" + auditFieldChange.getOldValue());
				}
			}
		}
		return revertList;
	}

	public static Map<Integer, String> getChanges(List<AuditEntry> auditEntries) {
		//Find out if there were changes between the most current rev and this one
		Map<Integer, String> revertList = new HashMap<Integer, String>();
		for (int i = auditEntries.size()-1; i>=0; i--) {
			AuditEntry auditEntry = auditEntries.get(i);
			Collection<AuditFieldChange> changes = auditEntry.getAuditEntryFieldChange();
			for (AuditFieldChange auditFieldChange : changes) {
				if (!revertList.containsKey(auditFieldChange.getPropertyName())) {
					revertList.put(auditFieldChange.getPropertyName(), auditFieldChange.getOldValue());
					//System.out.println("Field: " + auditFieldChange.getPropertyName() + "=" + auditFieldChange.getOldValue());
				}
			}
		}
		return revertList;
	}

}
