package com.tech4d.tsm.web.util;

import static org.junit.Assert.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.junit.Test;

import com.tech4d.tsm.model.geometry.TimeRange;

public class TestTimeRangePropertyEditor {

    private TimeRangePropertyEditor timeRangePropertyEditor = new TimeRangePropertyEditor();
    
    
    @Test public void setValueAsText() throws ParseException {

        assertEquals(makeDate(1,1,2007), getBegin("2007"));
        assertEquals(makeDate(3,1,2007), getBegin("2007,   March"));
        assertEquals(makeDate(3,1,2007), getBegin("2007, March"));
        assertEquals(makeDate(3,1,2007), getBegin("March, 2007"));
        assertEquals(makeDate(3,1,2007), getBegin("March 07"));
        assertEquals(makeDate(5,22,2007), getBegin("May 22, 2007"));
        assertEquals(makeDate(5,22,2007), getBegin("May 22, 07"));
        assertEquals(makeDate(12,22,2007), getBegin("Dec 22, 07"));
        assertEquals(makeDate(12,22,2007), getBegin("December 22, 07"));
        assertEquals(makeDate(5,22,2007), getBegin("22 May, 2007"));
        assertEquals(makeDate(5,22,2007), getBegin("22 May, 07"));
        assertEquals(makeDate(5,22,2007), getBegin("2007, 22 May"));
        assertEquals(makeDate(5,22,2007), getBegin("07, 22 May"));
        assertEquals(makeDate(5,22,2007), getBegin("5/22/07"));
        //full year fanciness
        assertEquivalent(makeDayRange(1,1,1941, 1,1,1942), transformTimeRange("1941"));
        assertEquivalent(makeDayRange(1,1,1941, 1,1,1943), transformTimeRange("1941 - 1942"));
        assertEquivalent(makeDayRange(12,7,1941, 12,7,1942), transformTimeRange("12/7/1941 - 12/7/1942"));
        assertEquivalent(makeDayRange(12,7,1941, 12,7,1942), transformTimeRange("Dec 7, 1941 - 12/7/1942"));
        assertEquivalent(makeDayRange(12,7,1941, 12,8,1941), transformTimeRange("Dec 7, 1941"));
        assertEquivalent(makeDayRange(12,31,1941, 1,1,1942), transformTimeRange("Dec 31, 1941"));
        assertException("1948 - 1947");
        assertException("Dec 7, 1942 - 12/7/1941");
    }
    
    @Test public void getValueAsText() {
        //full year fanciness
        assertEquals("1941", transformToText(1,1,1941,1,1,1942));
        assertEquals("1941 - 1942", transformToText(1,1,1941,1,1,1943));
        assertEquals("1941 - 1944", transformToText(1,1,1941,1,1,1945));

        assertEquals("1947", transformToText(1,1,1947,1,1,1948));
        assertEquals("December 07, 1941 - December 07, 1942", transformToText(12,7,1941,12,7,1942));
        assertEquals("December 01, 1941 - December 01, 1942", transformToText(12,1,1941,12,1,1942));
        assertEquals("January 01, 1941 - December 31, 1942", transformToText(1,1,1941,12,31,1942));
    }
    
    private Object transformToText(int m1, int d1, int y1, int m2, int d2, int y2) {
        TimeRange timeRange = makeDayRange(m1, d1, y1, m2, d2, y2);
        timeRangePropertyEditor.setValue(timeRange);
        return timeRangePropertyEditor.getAsText();
    }

    private void assertException(String text) {
        try {
            transformTimeRange(text);           
            fail("should have thrown an exception");
        } catch (IllegalArgumentException e) {
        } 
        
    }


    private TimeRange transformTimeRange(String text) {
        timeRangePropertyEditor.setAsText(text);
        return (TimeRange) timeRangePropertyEditor.getValue();
    }


    private Date getBegin(String text) {
        timeRangePropertyEditor.setAsText(text);       
        return ((TimeRange)(timeRangePropertyEditor.getValue())).getBegin();
    }


    private TimeRange makeDayRange(int m1, int d1, int y1, int m2, int d2, int y2) {
        return new TimeRange(makeDate(m1, d1, y1), makeDate(m2, d2, y2));
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
    
    /**
     * Set the calendar (correct the month so it reads like 5/22/2007 = may/22/2007
     * @param month
     * @param day
     * @param year
     * @param hour
     * @param minute
     * @param second
     * @return
     */
    private Date makeDate(int month, int day, int year, int hour, int minute, int second) {
        Calendar cal = new GregorianCalendar();
        cal.set(year, month - 1, day, hour, minute, second);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    private Date makeDate(int month, int day, int year) {
        return makeDate(month, day, year, 0, 0, 0);
    }
}
