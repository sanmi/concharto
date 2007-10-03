package com.tech4d.tsm.model.audit;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.tech4d.tsm.model.BaseEntity;

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
