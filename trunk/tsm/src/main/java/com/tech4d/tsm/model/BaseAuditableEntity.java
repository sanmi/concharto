package com.tech4d.tsm.model;

import java.util.Date;

import javax.persistence.MappedSuperclass;

@MappedSuperclass
public class BaseAuditableEntity extends BaseEntity implements Auditable {
    private Date lastModified;
    private Date created;

    /* (non-Javadoc)
     * @see com.tech4d.tsm.model.Auditable#getCreated()
     */
    public Date getCreated() {
        return created;
    }

    /* (non-Javadoc)
     * @see com.tech4d.tsm.model.Auditable#setCreated(java.util.Date)
     */
    public void setCreated(Date created) {
        this.created = created;
    }

    /* (non-Javadoc)
     * @see com.tech4d.tsm.model.Auditable#getLastModified()
     */
    public Date getLastModified() {
        return lastModified;
    }

    /* (non-Javadoc)
     * @see com.tech4d.tsm.model.Auditable#setLastModified(java.util.Date)
     */
    public void setLastModified(Date lastModified) {
        this.lastModified = lastModified;
    }
    
}
