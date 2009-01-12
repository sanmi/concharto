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
package org.tsm.concharto.model.audit;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.tsm.concharto.model.BaseEntity;


@Entity
public class AuditFieldChange extends BaseEntity {
    public static final int SZ_VALUE = 3999;

    private AuditEntry auditEntry;
    private String newValue;
    private String oldValue;
    private Integer propertyName;

    
    @ManyToOne
    @JoinColumn(name="auditEntry_id")
    public AuditEntry getAuditEntry() {
        return auditEntry;
    }
    public void setAuditEntry(AuditEntry auditEntry) {
        this.auditEntry = auditEntry;
    }
    @Column(length=SZ_VALUE)
    public String getNewValue() {
        return newValue;
    }
    public void setNewValue(String newValue) {
        this.newValue = newValue;
    }
    @Column(length=SZ_VALUE)
    public String getOldValue() {
        return oldValue;
    }
    public void setOldValue(String oldValue) {
        this.oldValue = oldValue;
    }
    /**
     * To save DB space, we encode the property name (e.g. description) as an integer.  It
     * is someone else's responsibility (usually the AuditFieldChangeFormatter) to provide 
     * the field name mapping. 
     * @return
     */
    public Integer getPropertyName() {
        return propertyName;
    }
    public void setPropertyName(Integer propertyName) {
        this.propertyName = propertyName;
    }
}
