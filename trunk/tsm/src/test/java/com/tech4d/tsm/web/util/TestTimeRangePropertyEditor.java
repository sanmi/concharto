package com.tech4d.tsm.web.util;

import static org.junit.Assert.*;

import java.util.GregorianCalendar;

import org.junit.Test;

import com.tech4d.tsm.model.geometry.TimeRange;

public class TestTimeRangePropertyEditor {

    private TimeRangePropertyEditor timeRangePropertyEditor = new TimeRangePropertyEditor();
    
    
    @Test public void setValueAsText() {
        timeRangePropertyEditor.setAsText("1947 - 1948");
        assertEquivalent(makeYearRange(1947, 1948), (TimeRange)timeRangePropertyEditor.getValue());
    }
    
    
    @Test public void getValueAsText() {
        String range = "1947 - 1948";
        timeRangePropertyEditor.setAsText(range);
        assertEquals(range, timeRangePropertyEditor.getAsText());

        timeRangePropertyEditor.setAsText("1947   -1948");
        assertEquals(range, timeRangePropertyEditor.getAsText());
    }
    
    
    private TimeRange makeYearRange(int begin, int end) {
        return new TimeRange(
                new GregorianCalendar(begin, 0, 1).getTime(),
                new GregorianCalendar(end, 0, 1).getTime());
    }
    
    private void assertEquivalent(TimeRange expected, TimeRange actual) {
        assertEquals(expected.getBegin(), actual.getBegin());
        assertEquals(expected.getEnd(), actual.getEnd());
    }
}
