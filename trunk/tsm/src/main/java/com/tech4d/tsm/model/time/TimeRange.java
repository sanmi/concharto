package com.tech4d.tsm.model.time;

import com.tech4d.tsm.util.TimeRangeFormat;
import org.hibernate.annotations.Index;
import org.hibernate.annotations.Type;

import javax.persistence.Entity;
import javax.persistence.Transient;
import java.util.Date;

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
    @Type(type = "com.tech4d.tsm.model.time.UtcDateTimeType")
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
    @Type(type = "com.tech4d.tsm.model.time.UtcDateTimeType")
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
     * @return text
     */
    @Transient
    public String getAsText() {
        return TimeRangeFormat.format(this);
    }

}
