package com.tech4d.tsm.model.time;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;

import org.hibernate.annotations.Type;

@Entity
public class TimeStamp extends TimePrimitive {
    private Date when;

    
    public TimeStamp() {
    }

    public TimeStamp(Date date) {
        this.setWhen(date);
    }

    @Column (name="time")
    @Type(type = "com.tech4d.tsm.model.time.UtcDateTimeType")
    public Date getWhen() {
        return when;
    }

    public void setWhen(Date when) {
        this.when = when;
    }    
}
