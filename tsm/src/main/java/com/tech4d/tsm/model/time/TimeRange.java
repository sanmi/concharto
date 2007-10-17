package com.tech4d.tsm.model.time;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.Index;

import com.tech4d.tsm.util.TimeRangeFormat;

@Entity
public class TimeRange extends TimePrimitive implements com.tech4d.tsm.model.time.SimpleTimeRange {
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

    /* (non-Javadoc)
     * @see com.tech4d.tsm.model.time.SimpleTimeRange#getBegin()
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Index(name="beginindex")
    public Date getBegin() {
        return begin;
    }

    /* (non-Javadoc)
     * @see com.tech4d.tsm.model.time.SimpleTimeRange#setBegin(java.util.Date)
     */
    public void setBegin(Date begin) {
        this.begin = begin;
    }

    /* (non-Javadoc)
     * @see com.tech4d.tsm.model.time.SimpleTimeRange#getEnd()
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Index(name="endindex")
    public Date getEnd() {
        return end;
    }

    /* (non-Javadoc)
     * @see com.tech4d.tsm.model.time.SimpleTimeRange#setEnd(java.util.Date)
     */
    public void setEnd(Date end) {
        this.end = end;
    }
    
    /**
     * Output a human readable string 
     * @return
     */
    @Transient
    public String getAsText() {
        return TimeRangeFormat.format(this);
    }

}
