package com.tech4d.tsm.model;

import java.util.Date;

public interface Auditable {

    public Long getId();
    public void setId(Long id);
    
    public Date getCreated();

    public void setCreated(Date created);

    public Date getLastModified();

    public void setLastModified(Date lastModified);
    
    public Long getVersion();

    public void setVersion(Long version);

}