package com.tech4d.tsm.web.util;

import java.beans.PropertyEditorSupport;
import java.text.ParseException;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;

import com.tech4d.tsm.model.geometry.TimeRange;

/**
 * Converts string to TimeRange and vica versa.  Supports many human readable
 * formats.
 * TODO INCOMPLETE 9-25-07
 */
public class TimeRangePropertyEditor extends PropertyEditorSupport {

    private TimeRange timeRange;
    private static final String[] PARSE_PATTERMS = 
    {   //TODO add more
        "yyyy", "yyyy/MM/dd", "yyyy/MM/dd", "yyyy-MM-dd",
        "yy", "yy/MM/dd", "yy/MM/dd", "yy-MM-dd"
    };
    
    @Override
    public void setValue(Object value) {
        this.timeRange = (TimeRange) value;
    }

    @Override
    public Object getValue() {
        return this.timeRange;
    }

    @Override
    public String getAsText() {
        //TODO debug
        if (this.timeRange == null) {
            return null;
        }
        StringBuffer range = new StringBuffer(DateFormatUtils.format(timeRange.getBegin(), "yyyy"));
        range.append(" - ");
        range.append(DateFormatUtils.format(timeRange.getEnd(), "yyyy"));
        return range.toString();
    }

    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        if (text != null) {
            // first separate the begin and the end
            // we will try the '-' first
            String[] split = StringUtils.split(text, '-');
            // now we will try the phrase 'to'
            if (split.length <= 1) {
                split = StringUtils.split(text, "to");
            }
            boolean isRange;
            if (split.length == 1) {
                isRange =  false; 
            } else if (split.length == 2) {
                isRange = true;
            } else {
                throw new IllegalArgumentException();
            }
            
            // if there are two dates, then we parse each one
            if (isRange) {
                try {
                    Date begin = parseDate(split[0]);
                    Date end = parseDate(split[1]);
                    this.timeRange = new TimeRange(begin, end);
                } catch (ParseException e) {
                    throw new IllegalArgumentException(e);
                }
            } else {
                //TODO
                //turn this one date into a range 
                //(e.g. Dec 7, 1941 starts at midnight and ends just before) 
            }
        }
    }
    
    private Date parseDate(String text) throws ParseException {
        //first clean the spaces from front and back
        text = StringUtils.trimToEmpty(text);
        return DateUtils.parseDate(text, PARSE_PATTERMS);   
    }
}
