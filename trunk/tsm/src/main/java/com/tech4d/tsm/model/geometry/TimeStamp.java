package com.tech4d.tsm.model.geometry;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class TimeStamp extends TimePrimitive {
    private Date when;

    
    public TimeStamp() {
    }

    public TimeStamp(Date date) {
        this.setWhen(date);
    }

    @Column (name="time")
    @Temporal(TemporalType.TIMESTAMP)
    public Date getWhen() {
        return when;
    }

    public void setWhen(Date when) {
        this.when = when;
    }    
}
