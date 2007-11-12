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
    
    @Test public void differentPrecisions() throws ParseException {

        String dateStr = "January, 1941";
        assertEquivalent(makeDayRange(1,1,1941,0,0,0, 2,1,1941,0,0,0), parseTimeRange(dateStr));
        assertEquals(dateStr, TimeRangeFormat.format(TimeRangeFormat.parse(dateStr)));

        dateStr = "December 07, 1940, 10AM - January, 1941";
        assertEquivalent(makeDayRange(12,7,1940,10,0,0, 2,1,1941,0,0,0), parseTimeRange(dateStr));
        assertEquals(dateStr, TimeRangeFormat.format(TimeRangeFormat.parse(dateStr)));

        dateStr = "November 07, 1940, 10AM - December 31, 1941";
        assertEquivalent(makeDayRange(11,7,1940,10,0,0, 1,1,1942,0,0,0), parseTimeRange(dateStr));
        assertEquals(dateStr, TimeRangeFormat.format(TimeRangeFormat.parse(dateStr)));

        dateStr = "November 07, 1940, 10AM - 1941";  //NOTE the difference between the last one is very slight  
        assertEquivalent(makeDayRange(11,7,1940,10,0,0, 1,1,1942,0,0,0), parseTimeRange(dateStr));
        assertEquals(dateStr, TimeRangeFormat.format(TimeRangeFormat.parse(dateStr)));

        dateStr = "November 07, 1940, 10:00:02AM - January 22, 1941";  //NOTE the difference between the last one is very slight  
        assertEquivalent(makeDayRange(11,7,1940,10,0,2, 1,23,1941,0,0,0), parseTimeRange(dateStr));
        assertEquals(dateStr, TimeRangeFormat.format(TimeRangeFormat.parse(dateStr)));
    }
    

    @Test public void firstUnit() throws ParseException {
        assertEquals("March 01, 1941", formatTimeRange(3,1,1941,3,2,1941));
        LapTimer timer = new LapTimer(this);
        assertEquivalent(makeDayRange(1,1,1941, 1,1,1942), parseTimeRange("1941"));
        assertEquivalent(makeDayRange(1,1,1941, 2,1,1941), parseTimeRange("Jan, 1941"));
        assertEquivalent(makeDayRange(1,1,1941, 1,2,1941), parseTimeRange("Jan 1, 1941"));
        assertEquivalent(makeDayRange(3,2,1941, 3,3,1941), parseTimeRange("March 2, 1941"));
        assertEquivalent(makeDayRange(3,1,1941, 3,2,1941), parseTimeRange("March 1, 1941"));
        assertEquivalent(makeDayRange(3,1,1941, 3,2,1941), parseTimeRange("Mar 1,1941"));
        assertEquivalent(makeDayRange(3,1,1941,10,0,0, 3,1,1941,11,0,0), parseTimeRange("March 1,1941,10AM"));
        assertEquivalent(makeDayRange(3,1,1941,17,0,0, 3,1,1941,18,0,0), parseTimeRange("March 1,1941,17"));
        assertEquivalent(makeDayRange(3,1,1941,10,0,0, 3,1,1941,10,1,0), parseTimeRange("March 1,1941,10:00AM"));
        assertEquivalent(makeDayRange(3,1,1941,10,0,0, 3,1,1941,10,0,1), parseTimeRange("March 1,1941,10:00:00AM"));
        
        timer.timeIt("parse");
        System.out.println(timer);
    	
    }

    @Test public void parse() throws ParseException {

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

        assertEquivalent(makeDayRange(12,7,1941,10,0,0, 12,7,1941,10,1,0), parseTimeRange("Dec 7,1941,10:00AM"));
        assertEquivalent(makeDayRange(12,7,1941,10,0,0, 12,7,1941,10,1,0), parseTimeRange("Dec 7, 1941 10:00AM"));
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
    
    @Test public void parseErrors() {
        try {
            TimeRangeFormat.parse("December 41, 1941").getBegin();
            fail("should have thrown an exception");
        } catch (ParseException e) {
            //expected            
        }
    }
    
    @Test public void parseOldDates() throws ParseException {
        //back and forth
        SimpleTimeRange tr = parseTimeRange("941 BC");
        assertEquals("941 BC", TimeRangeFormat.format(tr));

        //Test date formats
        assertEquals(makeDate(1,1,-2007), getBegin("2007BC"));
        assertEquals(makeDate(1,1,-2007), getBegin("2007 BC"));

        assertEquals(makeDate(3,1,-2007), getBegin("2007 BC, March"));
        assertEquals(makeDate(3,1,-2007), getBegin("March, 2007 BC"));
        assertEquals(makeDate(3,1,-69), getBegin("March, 69 BC"));
        assertEquals(makeDate(5,22,-69), getBegin("5/22/69 BC"));

        //test date ranges
        //full year fanciness
        assertEquivalent(makeDayRange(1,1,-941, 1,1,-940), parseTimeRange("941 BC"));
        assertEquivalent(makeDayRange(12,7,-1941, 12,8,-1941), parseTimeRange("December 7, 1941 BC"));
        assertEquivalent(makeDayRange(12,7,-1941,10,1,0, 12,7,-1941,10,2,0), parseTimeRange("Dec 7, 1941 BC, 10:01AM"));
        assertEquivalent(makeDayRange(12,7,-1941,10,1,1, 12,7,-1941,10,1,2), parseTimeRange("Dec 7, 1941 BC, 10:01:01AM"));
        assertEquivalent(makeDayRange(1,1,-1941, 1,1,-1939), parseTimeRange("1941 BC - 1940 BC"));
        assertEquivalent(makeDayRange(12,7,-1941, 12,8,-1940), parseTimeRange("Dec 7, 1941 BC - 12/7/1940 BC"));

        //TODO should we allow negative ranges? Isn't that really a data validation issue?
        assertException("1948 BC - 1949 BC");
        assertException("Dec 7, 1942 BC - 12/7/1945 BC");
    }

    @Test public void format() {

        assertEquals("1941", formatTimeRange(1,1,1941,1,1,1942));
        assertEquals("1941 - 1942", formatTimeRange(1,1,1941,1,1,1943));
        assertEquals("1941 - 1944", formatTimeRange(1,1,1941,1,1,1945));

        //if 12/1/1941 00:00 - 1/1/1942 00:00 = December 1941
        assertEquals("December, 1941", formatTimeRange(12,1,1941,1,1,1942));
        //if 11/1/1941 00:00 - 1/1/1942 00:00 = November 1941 - December 1941 (subtract)
        assertEquals("November, 1941 - December, 1941", formatTimeRange(11,1,1941,1,1,1942));
        //if 12/1/1941 00:00 - 12/1/1942 00:00 = December 1941 - December 1942
        assertEquals("December, 1941 - December, 1942", formatTimeRange(12,1,1941,12,1,1942));

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
    
    @Test public void formatOldDates() {

        assertEquals("941 AD", formatTimeRange(1,1,941,1,1,942));
        assertEquals("941 AD - 942 AD", formatTimeRange(1,1,941,1,1,943));
        assertEquals("941 AD - 944 AD", formatTimeRange(1,1,941,1,1,945));

        assertEquals("941 BC - 940 BC", formatTimeRange(1,1,-941,1,1,-939));
        assertEquals("56 BC - 100 AD", formatTimeRange(1,1,-56,1,1,101));
        assertEquals("941 BC", formatTimeRange(1,1,-941,1,1,-940));
        
        assertEquals("March, 941 BC", formatTimeRange(3,1,-941,4,1,-941));
        
        /*        assertEquals("941 AD - 942 AD", formatTimeRange(1,1,941,1,1,943));
        assertEquals("941 AD - 944 AD", formatTimeRange(1,1,941,1,1,945));

        //if 12/1/1941 00:00 - 1/1/1942 00:00 = December 1941
        assertEquals("December 1941", formatTimeRange(12,1,1941,1,1,1942));
        //if 11/1/1941 00:00 - 1/1/1942 00:00 = November 1941 - December 1941 (subtract)
        assertEquals("November 1941 - December 1941", formatTimeRange(11,1,1941,1,1,1942));
        //if 12/1/1941 00:00 - 12/1/1942 00:00 = December 1941 - December 1942
        assertEquals("December 1941 - December 1942", formatTimeRange(12,1,1941,12,1,1942));
*/        
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
        return TimeRangeFormat.parse(text).getBegin().getDate();
    }

    private SimpleTimeRange makeDayRange(int m1, int d1, int y1, int m2, int d2, int y2) {
        return new TimeRange(makeDate(m1, d1, y1), makeDate(m2, d2, y2));
    }
    
    public TimeRange makeDayRange(int m1, int d1, int y1, int hh1, int mm1, int ss1, 
            int m2, int d2, int y2, int hh2, int mm2, int ss2) {
        return new TimeRange(makeDate(m1, d1, y1, hh1, mm1, ss1), makeDate(m2, d2, y2, hh2, mm2, ss2));
    }

    private void assertEquivalent(SimpleTimeRange expected, com.tech4d.tsm.model.time.SimpleTimeRange actual) {
        assertEquals(expected.getBegin().getDate(), actual.getBegin().getDate());
        assertEquals(expected.getEnd().getDate(), actual.getEnd().getDate());
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
        if (year<0) {
            year ++; //to account for year '0'
            //cal.set(Calendar.ERA, GregorianCalendar.BC);
        } 
        cal.set(year, month - 1, day, hour, minute, second);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    private Date makeDate(int month, int day, int year) {
        return makeDate(month, day, year, 0, 0, 0);
    }

}
