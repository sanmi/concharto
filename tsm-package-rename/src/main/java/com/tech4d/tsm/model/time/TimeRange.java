/*******************************************************************************
 * ***** BEGIN LICENSE BLOCK *****
 * Version: MPL 1.1
 * 
 * The contents of this file are subject to the Mozilla Public License Version 
 * 1.1 (the "License"); you may not use this file except in compliance with 
 * the License. You may obtain a copy of the License at 
 * http://www.mozilla.org/MPL/
 * 
 * Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 * for the specific language governing rights and limitations under the
 * License.
 * 
 * The Original Code is Concharto.
 * 
 * The Initial Developer of the Original Code is
 * Time Space Map, LLC
 * Portions created by the Initial Developer are Copyright (C) 2007 - 2009
 * the Initial Developer. All Rights Reserved.
 * 
 * Contributor(s):
 * Time Space Map, LLC
 * 
 * ***** END LICENSE BLOCK *****
 ******************************************************************************/
package com.tech4d.tsm.model.time;

import com.tech4d.tsm.util.TimeRangeFormat;
import org.hibernate.annotations.Index;

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
     * @see com.tech4d.tsm.model.time.SimpleTimeRange#getBegin()
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
     * @see com.tech4d.tsm.model.time.SimpleTimeRange#setBegin(java.util.Date)
     */
    public void setBegin(VariablePrecisionDate begin) {
        this.begin = begin;
    }

    /* (non-Javadoc)
     * @see com.tech4d.tsm.model.time.SimpleTimeRange#getEnd()
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
     * @see com.tech4d.tsm.model.time.SimpleTimeRange#setEnd(java.util.Date)
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
