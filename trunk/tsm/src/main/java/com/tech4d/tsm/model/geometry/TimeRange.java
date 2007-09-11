package com.tech4d.tsm.model.geometry;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class TimeRange extends TimePrimitive {
    private Date begin;

    private Date end;

    public TimeRange() {
        super();
    }

    public TimeRange(Date begin, Date end) {
        super();
        this.begin = begin;
        this.end = end;
    }

    @Temporal(TemporalType.TIMESTAMP)
    public Date getBegin() {
        return begin;
    }

    public void setBegin(Date begin) {
        this.begin = begin;
    }

    @Temporal(TemporalType.TIMESTAMP)
    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

}
