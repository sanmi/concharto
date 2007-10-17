package com.tech4d.tsm.web.util;

import java.beans.PropertyEditorSupport;
import java.text.ParseException;

import com.tech4d.tsm.util.TimeRangeFormat;

/**
 * Converts string to TimeRange and vica versa. Supports many human readable
 * formats. TODO INCOMPLETE 9-25-07
 */
public class TimeRangePropertyEditor extends PropertyEditorSupport {

    private com.tech4d.tsm.model.time.SimpleTimeRange timeRange;
    @Override
    public void setValue(Object value) {
        this.timeRange = (com.tech4d.tsm.model.time.SimpleTimeRange) value;
    }

    @Override
    public Object getValue() {
        return this.timeRange;
    }

    @Override
    public String getAsText() {
        // TODO debug incomplete!
        if (this.timeRange == null) {
            return null;
        } else {
            return TimeRangeFormat.format(this.timeRange);
        }
        
    }

    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        
        try {
            this.timeRange = TimeRangeFormat.parse(text);
        } catch (ParseException e) {
            throw new IllegalArgumentException();
        }
    }
}
