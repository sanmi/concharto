package com.tech4d.tsm.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.junit.Test;

import com.tech4d.tsm.model.time.SimpleTimeRange;
import com.tech4d.tsm.model.time.TimeRange;

public class TestTimeRangeFormat {
    //private TimeRangePropertyEditor timeRangePropertyEditor = new TimeRangePropertyEditor();   
    
    @Test public void debug () throws ParseException {
    }

    @Test public void setValueAsText() throws ParseException {

        //Test date formats
        assertEquals(makeDate(1,1,2007), getBegin("2007"));
        assertEquals(makeDate(3,1,2007), getBegin("2007, March"));
        assertEquals(makeDate(3,1,2007), getBegin("2007,   March"));
        assertEquals(makeDate(3,1,2007), getBegin("March, 2007"));
        assertEquals(makeDate(3,1,2007), getBegin("March 07"));
        assertEquals(makeDate(5,22,2007), getBegin("May 22, 2007"));
        assertEquals(makeDate(5,22,2007), getBegin("May 22,2007"));
        assertEquals(makeDate(5,22,2007), getBegin("May 22, 07"));
        assertEquals(makeDate(12,22,2007), getBegin("Dec 22, 07"));
        assertEquals(makeDate(12,22,2007), getBegin("December 22, 07"));
        assertEquals(makeDate(5,22,2007), getBegin("2007, 22 May"));
        assertEquals(makeDate(5,22,2007), getBegin("2007,22 May"));
        assertEquals(makeDate(5,22,2007), getBegin("May 22, 07"));
        assertEquals(makeDate(5,22,2007), getBegin("5/22/07"));
        
        //test date ranges
        //full year fanciness
        assertEquivalent(makeDayRange(1,1,1941, 1,1,1942), parseTimeRange("1941"));
        assertEquivalent(makeDayRange(12,7,1941, 12,8,1941), parseTimeRange("December 7, 1941"));
        assertEquivalent(makeDayRange(12,1,1941, 1,1,1942), parseTimeRange("December, 1941"));
        assertEquivalent(makeDayRange(12,7,1941, 12,8,1941), parseTimeRange("Dec 7, 1941"));
        assertEquivalent(makeDayRange(12,31,1941, 1,1,1942), parseTimeRange("Dec 31, 1941"));

        assertEquivalent(makeDayRange(12,7,1941,10,0,0, 12,7,1941,11,0,0), parseTimeRange("Dec 7,1941,10:00AM"));
        assertEquivalent(makeDayRange(12,7,1941,10,0,0, 12,7,1941,11,0,0), parseTimeRange("Dec 7, 1941 10:00AM"));
        assertEquivalent(makeDayRange(12,7,1941,10,0,0, 12,7,1941,11,0,0), parseTimeRange("Dec 7, 1941, 10AM"));
        assertEquivalent(makeDayRange(12,7,1941,10,1,0, 12,7,1941,10,2,0), parseTimeRange("Dec 7, 1941, 10:01AM"));
        assertEquivalent(makeDayRange(12,7,1941,10,1,1, 12,7,1941,10,1,2), parseTimeRange("Dec 7, 1941, 10:01:01AM"));
        
        //TODO should we allow negative ranges? Isn't that really a data validation issue?
        assertException("1948 - 1947");
        assertException("Dec 7, 1942 - 12/7/1941");

        //Ranges
        assertEquivalent(makeDayRange(1,1,1941, 1,1,1943), parseTimeRange("1941 - 1942"));
        assertEquivalent(makeDayRange(12,7,1941, 12,11,1941), parseTimeRange("Dec 7, 1941 - Dec 10, 1941"));
        assertEquivalent(makeDayRange(12,7,1941, 12,8,1942), parseTimeRange("12/7/1941 - 12/7/1942"));
        assertEquivalent(makeDayRange(12,7,1941, 12,8,1942), parseTimeRange("Dec 7, 1941 - 12/7/1942"));

        assertEquivalent(makeDayRange(12,7,1941,10,0,0, 12,7,1941,12,0,0), parseTimeRange("Dec 7, 1941 10am - Dec 7, 1941 11am"));
        assertEquivalent(makeDayRange(12,7,1941,10,0,0, 12,7,1941,10,2,0), parseTimeRange("Dec 7, 1941 10:00am - Dec 7, 1941 10:01am"));
        assertEquivalent(makeDayRange(12,7,1941,10,0,0, 12,7,1941,10,0,2), parseTimeRange("Dec 7, 1941 10:00:00am - Dec 7, 1941 10:00:01am"));
}

    @Test public void getValueAsText() {

        assertEquals("1941", formatTimeRange(1,1,1941,1,1,1942));
        assertEquals("1941 - 1942", formatTimeRange(1,1,1941,1,1,1943));
        assertEquals("1941 - 1944", formatTimeRange(1,1,1941,1,1,1945));

        //if 12/1/1941 00:00 - 1/1/1942 00:00 = December 1941
        assertEquals("December 1941", formatTimeRange(12,1,1941,1,1,1942));
        //if 11/1/1941 00:00 - 1/1/1942 00:00 = November 1941 - December 1941 (subtract)
        assertEquals("November 1941 - December 1941", formatTimeRange(11,1,1941,1,1,1942));
        //if 12/1/1941 00:00 - 12/1/1942 00:00 = December 1941 - December 1942
        assertEquals("December 1941 - December 1942", formatTimeRange(12,1,1941,12,1,1942));

        //1) if 12/7/1941 00:00 - 12/8/1941 00:00 = December 7, 1941
        assertEquals("December 07, 1941", formatTimeRange(12,7,1941,12,8,1941));
        //2) if 12/7/1941 00:00 - 12/9/1941 00:00 = December 7, 1941, December 8, 1941 (subtract)
        assertEquals("December 07, 1941 - December 08, 1941", formatTimeRange(12,7,1941,12,9,1941));
        //3) if 12/7/1941 00:00 - 12/7/1942 00:00 = December 7, 1941, December 7, 1941
        assertEquals("December 07, 1941 - December 07, 1942", formatTimeRange(12,7,1941,12,7,1942));
        assertEquals("January 01, 1941 - December 30, 1942", formatTimeRange(1,1,1941,12,31,1942));
        assertEquals("December 07, 1941 - December 10, 1941", formatTimeRange(12,7,1941,12,11,1941));
        
        //if 12/7/1941 10:00 - 12/7/1941 11:00 = December 7, 1941, 10am
        assertEquals("December 07, 1941, 10AM", transformToText(12,7,1941,10, 0, 0, 12,7,1941, 11, 0, 0));
        //if 12/7/1941 10:00 - 12/7/1942 12:00 = December 7, 1941, 10am - December 7, 1941, 12am
        assertEquals("December 07, 1941, 10AM - December 07, 1941, 11AM", transformToText(12,7,1941,10, 0, 0, 12,7,1941, 12, 0, 0));
        //if 12/7/1941 10:00 - 12/8/1942 10:00 = December 7, 1941, 10am - December 8, 1941, 10am
        assertEquals("December 07, 1941, 10AM - December 08, 1941, 10AM", transformToText(12,7,1941,10, 0, 0, 12,8,1941, 10, 0, 0));

        //if 12/7/1941 10:20 - 12/7/1941 10:21 = December 7, 1941, 10:20am
        assertEquals("December 07, 1941, 10:20AM", transformToText(12,7,1941,10, 20, 0, 12,7,1941, 10, 21, 0));
        //if 12/7/1941 10:20 - 12/7/1941 10:22 = December 7, 1941, 10:20am - December 7, 1941, 10:21am
        assertEquals("December 07, 1941, 10:20AM - December 07, 1941, 10:21AM", transformToText(12,7,1941,10, 20, 0, 12,7,1941, 10, 22, 0));
        //if 12/7/1941 10:20 - 12/8/1941 10:20 = December 7, 1941, 10:20am - December 8, 1941, 10:20am
        assertEquals("December 07, 1941, 10:20AM - December 08, 1941, 10:20AM", transformToText(12,7,1941,10, 20, 0, 12,8,1941, 10, 20, 0));
        assertEquals("December 07, 1941, 10:20AM - December 07, 1942, 10:20AM", transformToText(12,7,1941,10, 20, 0, 12,7,1942, 10, 20, 0));

        assertEquals("December 07, 1941, 10:20:02AM", transformToText(12,7,1941,10, 20, 2, 12,7,1941, 10, 20, 3));
        assertEquals("December 07, 1941, 10:20:02AM - December 07, 1941, 10:20:03AM", transformToText(12,7,1941,10, 20, 2, 12,7,1941, 10, 20, 4));
        assertEquals("December 07, 1941, 10:20:02AM - December 07, 1942, 10:20:02AM", transformToText(12,7,1941,10, 20, 2, 12,7,1942, 10, 20, 2));

    }
    
    private Object formatTimeRange(int m1, int d1, int y1, int m2, int d2, int y2) {
        SimpleTimeRange timeRange = makeDayRange(m1, d1, y1, m2, d2, y2);
        return TimeRangeFormat.format(timeRange);
    }

    private Object transformToText(int m1, int d1, int y1, int hh1, int mm1, int ss1, 
            int m2, int d2, int y2, int hh2, int mm2, int ss2) {
        com.tech4d.tsm.model.time.SimpleTimeRange timeRange = makeDayRange(m1, d1, y1, hh1, mm1, ss1, m2, d2, y2, hh2, mm2, ss2);
        return TimeRangeFormat.format(timeRange);
    }

    private void assertException(String text) {
        try {
            TimeRangeFormat.parse(text);           
            fail("should have thrown an exception");
        } catch (ParseException e) {
        } 
    }

    private com.tech4d.tsm.model.time.SimpleTimeRange parseTimeRange(String text) throws ParseException {
        return TimeRangeFormat.parse(text);
    }

    private Date getBegin(String text) throws ParseException {
        return TimeRangeFormat.parse(text).getBegin();
    }

    private SimpleTimeRange makeDayRange(int m1, int d1, int y1, int m2, int d2, int y2) {
        return new TimeRange(makeDate(m1, d1, y1), makeDate(m2, d2, y2));
    }
    
    public TimeRange makeDayRange(int m1, int d1, int y1, int hh1, int mm1, int ss1, 
            int m2, int d2, int y2, int hh2, int mm2, int ss2) {
        return new TimeRange(makeDate(m1, d1, y1, hh1, mm1, ss1), makeDate(m2, d2, y2, hh2, mm2, ss2));
    }

    private void assertEquivalent(SimpleTimeRange expected, com.tech4d.tsm.model.time.SimpleTimeRange actual) {
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
