package com.tech4d.tsm.model.audit;

import java.util.Collection;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

import org.hibernate.annotations.ForeignKey;

import com.tech4d.tsm.model.BaseEntity;

@Entity
public class AuditEntry extends BaseEntity {
    public static final int ACTION_INSERT = 0;
    public static final int ACTION_UPDATE = 1;
    public static final int ACTION_DELETE = 2;
    public static final int ACTION_REVERT = 3;

    private Collection<AuditFieldChange> auditEntryFieldChange;
    private Date dateCreated;
    private String user;
    private Integer action;
    private String entityClass;
    private long entityId;
    private long version;


    @OneToMany(mappedBy="auditEntry", cascade={CascadeType.ALL})
    @ForeignKey(name="FK_AUDITENTR_AUDITFLD")
    public Collection<AuditFieldChange> getAuditEntryFieldChange() {
        return auditEntryFieldChange;
    }
    public void setAuditEntryFieldChange(Collection<AuditFieldChange> components) {
        this.auditEntryFieldChange = components;
    }
    public Date getDateCreated() {
        return dateCreated;
    }
    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }
    public String getUser() {
        return user;
    }
    public void setUser(String user) {
        this.user = user;
    }
    public Integer getAction() {
        return action;
    }
    public void setAction(Integer action) {
        this.action = action;
    }
    public String getEntityClass() {
        return entityClass;
    }
    public void setEntityClass(String entityClass) {
        this.entityClass = entityClass;
    }
    public long getEntityId() {
        return entityId;
    }
    public void setEntityId(long entityId) {
        this.entityId = entityId;
    }
    public long getVersion() {
        return version;
    }
    public void setVersion(long version) {
        this.version = version;
    }
    
}
