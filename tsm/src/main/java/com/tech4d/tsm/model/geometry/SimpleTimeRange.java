package com.tech4d.tsm.model.geometry;

import java.util.Date;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;

public interface SimpleTimeRange {

    @Temporal(TemporalType.TIMESTAMP)
    public Date getBegin();

    public void setBegin(Date begin);

    @Temporal(TemporalType.TIMESTAMP)
    public Date getEnd();

    public void setEnd(Date end);

}