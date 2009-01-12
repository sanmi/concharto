/*******************************************************************************
 * Copyright 2009 Time Space Map, LLC
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package org.tsm.concharto.model.time;

import org.hibernate.annotations.Index;
import org.tsm.concharto.util.TimeRangeFormat;

import javax.persistence.*;
import java.util.Date;

@Entity
public class TimeRange extends TimePrimitive implements SimpleTimeRange {
    private VariablePrecisionDate begin;
    private VariablePrecisionDate end;

    public TimeRange() {
        super();
    }

    public TimeRange(Date begin, Date end) {
        super();
        this.begin = new VariablePrecisionDate(begin);
        this.end = new VariablePrecisionDate(end);
    }
    
    public TimeRange(VariablePrecisionDate begin, VariablePrecisionDate end) {
    	super();
    	this.begin = begin;
    	this.end = end;
    }
    
    public TimeRange(Date begin, Integer beginPrecision, Date end, Integer endPrecision) {
    	super();
    	this.begin = new VariablePrecisionDate(begin, beginPrecision);
    	this.end = new VariablePrecisionDate(end, endPrecision);
    }

    /* (non-Javadoc)
     * @see org.tsm.concharto.model.time.SimpleTimeRange#getBegin()
     */
    @Embedded
    @AttributeOverrides( {
        @AttributeOverride(name="date", column =  @Column(name="begin") ),
        @AttributeOverride(name="precision", column = @Column(name="beginPrecision") )
    } )
    @Index(name="beginindex")
    public VariablePrecisionDate getBegin() {
        return begin;
    }

    /* (non-Javadoc)
     * @see org.tsm.concharto.model.time.SimpleTimeRange#setBegin(java.util.Date)
     */
    public void setBegin(VariablePrecisionDate begin) {
        this.begin = begin;
    }

    /* (non-Javadoc)
     * @see org.tsm.concharto.model.time.SimpleTimeRange#getEnd()
     */
    @Embedded
    @AttributeOverrides( {
        @AttributeOverride(name="date", column =  @Column(name="end") ),
        @AttributeOverride(name="precision", column = @Column(name="endPrecision") )
    } )
    @Index(name="endindex")
    public VariablePrecisionDate getEnd() {
        return end;
    }

    /* (non-Javadoc)
     * @see org.tsm.concharto.model.time.SimpleTimeRange#setEnd(java.util.Date)
     */
    public void setEnd(VariablePrecisionDate end) {
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
