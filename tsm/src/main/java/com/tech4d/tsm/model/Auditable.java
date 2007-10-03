package com.tech4d.tsm.model;

import java.util.Date;

public interface Auditable {

    public Long getId();
    
    public Date getCreated();

    public void setCreated(Date created);

    public Date getLastModified();

    public void setLastModified(Date lastModified);

}