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
package org.tsm.concharto.audit;

import java.util.ArrayList;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.tsm.concharto.model.Auditable;
import org.tsm.concharto.model.audit.AuditEntry;
import org.tsm.concharto.model.audit.AuditFieldChange;
import org.tsm.concharto.util.ClassName;


public abstract class BaseAuditFieldChangeFormatter implements AuditFieldChangeFormatter {

	@SuppressWarnings("unchecked")
	public abstract boolean supports(Class clazz);
	
    /**
     * We don't need to record what got deleted - its gone and ain't coming back
     * @param auditable Auditable
     */
    public AuditEntry createDeleteAuditItems(Auditable auditable) {
        return makeAuditEntry(auditable, AuditEntry.ACTION_DELETE);
    }

    /**
     * Just record that the object was created and who did it
     * @param auditable Auditable
     */
    public AuditEntry createInsertAuditItems(Auditable auditable) {
        return makeAuditEntry(auditable, AuditEntry.ACTION_INSERT);
    }

	public abstract AuditEntry createUpdateAuditItems(Auditable currentInstance,
			Auditable previousInstance);

	public abstract void refresh(Auditable auditable);

	public abstract Auditable revertEntity(Auditable auditable,
			Map<Integer, String> changeList);

    protected AuditEntry makeAuditEntry(Auditable auditable, Integer action) {
        AuditEntry auditEntry = new AuditEntry();
        auditEntry.setAuditEntryFieldChange(new ArrayList<AuditFieldChange>());
        auditEntry.setAction(action);
        auditEntry.setEntityClass(ClassName.getClassName(auditable));
        auditEntry.setEntityId(auditable.getId());
        Long currVersion = auditable.getVersion();
        if (null == currVersion) {
            currVersion = 0L;
        }
        auditEntry.setVersion(currVersion);
        return auditEntry;
    }

    protected void makeChange(Integer propertyName, String current, String previous, AuditEntry auditEntry ) {
        if (!StringUtils.equals(current, previous)) {
            //concatenate in case it is larger than the space alloted
            current = StringUtils.abbreviate(current, AuditFieldChange.SZ_VALUE);
            previous = StringUtils.abbreviate(previous, AuditFieldChange.SZ_VALUE);

            // create the audit entry for this property
            AuditFieldChange auditFieldChange = new AuditFieldChange();
            auditFieldChange.setPropertyName(propertyName);
            auditFieldChange.setNewValue(current == null ? null : current);
            auditFieldChange.setOldValue(previous == null ? null : previous);
            // hook up relation
            auditFieldChange.setAuditEntry(auditEntry);
            auditEntry.getAuditEntryFieldChange().add(auditFieldChange);
        }
    }

}
