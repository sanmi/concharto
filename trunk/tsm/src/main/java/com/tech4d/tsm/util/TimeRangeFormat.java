package com.tech4d.tsm.util;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;

import com.tech4d.tsm.model.geometry.SimpleTimeRange;
import com.tech4d.tsm.model.geometry.TimeRange;

/**
 * Converts string to TimeRange and vica versa. Supports many human readable
 * formats. TODO INCOMPLETE 9-25-07
 */
public class TimeRangeFormat  {

    private static final String FMT_TO_YEAR = "yyyy";
    private static final String FMT_TO_MONTH = "MMMM yyyy";
    private static final String FMT_TO_DAY = "MMMM dd, yyyy";
    private static final String FMT_TO_HOUR = "MMMM dd, yyyy, hha";
    private static final String FMT_TO_MINUTES = "MMMM dd, yyyy, hh:mma";
    private static final String FMT_TO_SECONDS = "MMMM dd, yyyy, hh:mm:ssa";
    
  
    private final static String[] yearPatterns = {
        "yyyy", 
        "yyyyG"
    };

    private final static String[] dayPatterns = {
        "MM/dd/yy", 
        "yyyy/MM/dd", 
        "MMM dd, yy", 
        "MMM dd, yyyy", 
        "yyyy, dd MMM", 
        "MMM yy", 
        "MMM, yy", 
        "yyyy, MMM", 
    };
    private final static String[] timePatterns = {
        " hha", 
        " hh:mma", 
        " hh:mm:ssa", 
        " HH", 
        " HH:mm", 
        " HH:mm:ss", 
    };
    
    private static String[] patterns;
    static {
        ArrayList<String> tmpPatterns = new ArrayList<String>();
        //the solo year patterns
        addPatterns(dayPatterns, tmpPatterns);
        addPatterns(timePatterns, tmpPatterns);
        addPatterns(yearPatterns, tmpPatterns);

        //combined patterns
        for (String dayPattern : dayPatterns) {
            for (String timePattern : timePatterns) {
                tmpPatterns.add(dayPattern + timePattern);
                tmpPatterns.add(new StringBuffer(dayPattern).append(',').append(timePattern).toString());
            }
        }
        patterns = tmpPatterns.toArray(new String[tmpPatterns.size()]);
    }
    
    private static void addPatterns(String[] patterns, ArrayList<String> list) {
        for (String pattern : patterns) {
            list.add(pattern);
        }        
    }

    /**
     * Parse a time range from a wide variety of formats.  Some examples:
     * '1941', 'May 2006', '1948 - 1950', 'Jan 23, 2002 10:23 - Feb 2005'
     * @param text to parse
     * @return TimeRange 
     * @throws ParseException if parsing failed
     */
    public static SimpleTimeRange parse(String text) throws ParseException {
        SimpleTimeRange tr = null;
        if (!StringUtils.isEmpty(text)) {
            // first separate the begin and the end
            // we will try the '-' first
            String[] split = StringUtils.split(text, '-');
            // now we will try the phrase 'to'
            if (split.length <= 1) {
                split = StringUtils.split(text, "to");
            }
            boolean isRange;
            if (split.length == 1) {
                isRange = false;
            } else if (split.length == 2) {
                isRange = true;
            } else {
                throw new ParseException(text,0);
            }

            // if there are two dates, then we parse each one
            if (isRange) {
                tr = parseRange(split);
            } else {
                // turn this single date into a range
                tr = parseSingleDate(text);
            }
        } 
        return tr;
    }
    
    public static String format(SimpleTimeRange timeRange) {
        if (timeRange == null) {
            return null;
        }
        boolean secondOmmitted = isBothValue(0, Calendar.SECOND, timeRange);
        boolean minuteOmmitted = isBothValue(0, Calendar.MINUTE, timeRange);
        boolean hourOmmitted = isBothValue(0, Calendar.HOUR, timeRange);
        boolean dayOmmitted = isBothValue(1, Calendar.DAY_OF_MONTH, timeRange);
        boolean monthOmmitted = isBothValue(Calendar.JANUARY, Calendar.MONTH, timeRange);

        //if 1/1/1941 00:00 - 1/1/1942 00:00 = 1941
        //if 1/1/1941 00:00 - 1/1/1943 00:00 = 1941 - 1942
        if (isBothFirstDayOfYear(timeRange)) {  
            if (isOneApart(Calendar.YEAR, timeRange)) { 
                return rangeFormat(timeRange.getBegin(), FMT_TO_YEAR);  
            } else {
                SimpleTimeRange adjusted = subtractOneFromEnd(Calendar.YEAR, timeRange);
                return rangeFormat(adjusted, FMT_TO_YEAR);  //e.g 1941 - 1942
            }
        } 
        //if 12/1/1941 00:00 - 1/1/1942 00:00 = December 1941
        //if 11/1/1941 00:00 - 1/1/1942 00:00 = November 1941 - December 1941 (subtract)
        //if 12/1/1941 00:00 - 12/1/1942 00:00 = December 1941 - December 1942 (don't subtract)
        else if (minuteOmmitted && hourOmmitted && dayOmmitted && !monthOmmitted) { 
            if (isOneApart(Calendar.MONTH, timeRange)) {
                return rangeFormat(timeRange.getBegin(), FMT_TO_MONTH); 
            } else if (isEqual(Calendar.MONTH, timeRange)){ //case 2
                return rangeFormat(timeRange, FMT_TO_MONTH); 
            } else {
                SimpleTimeRange adjusted = subtractOneFromEnd(Calendar.MONTH, timeRange);
                return rangeFormat(adjusted, FMT_TO_MONTH); 
                
            }
        }
        //1) if 12/7/1941 00:00 - 12/8/1941 00:00 = December 7, 1941
        //2) if 12/7/1941 00:00 - 12/9/1941 00:00 = December 7, 1941, December 8, 1941 (subtract)
        //3) if 12/7/1941 00:00 - 12/7/1942 00:00 = December 7, 1941, December 7, 1941 (don't subtract)
        else if (minuteOmmitted && hourOmmitted && !dayOmmitted ) { 
            if (isOneApart(Calendar.DAY_OF_MONTH, timeRange)) { //case 1
                return rangeFormat(timeRange.getBegin(), FMT_TO_DAY); 
            } else if (isEqual(Calendar.DAY_OF_MONTH, timeRange)){ //case 2
                return rangeFormat(timeRange, FMT_TO_DAY); 
            } else { //case 3
                SimpleTimeRange adjusted = subtractOneFromEnd(Calendar.DAY_OF_MONTH, timeRange);
                return rangeFormat(adjusted, FMT_TO_DAY); 
            }
        }
        //if 12/7/1941 10:00 - 12/7/1941 11:00 = December 7, 1941, 10am
        //if 12/7/1941 10:00 - 12/7/1942 12:00 = December 7, 1941, 10am - December 7, 1941, 12am (subtract!)
        //if 12/7/1941 10:00 - 12/8/1942 10:00 = December 7, 1941, 10am - December 8, 1941, 10am (don't subtract)
        else if (minuteOmmitted && !hourOmmitted) { 
            if (isOneApart(Calendar.HOUR, timeRange)) {
                return rangeFormat(timeRange.getBegin(), FMT_TO_HOUR); 
            } else if (isEqual(Calendar.HOUR, timeRange)){ //case 2
                return rangeFormat(timeRange, FMT_TO_HOUR); 
            } else { 
                SimpleTimeRange adjusted = subtractOneFromEnd(Calendar.HOUR, timeRange);
                return rangeFormat(adjusted, FMT_TO_HOUR); 
            }
        } 
        //if 12/7/1941 10:20 - 12/7/1941 10:21 = December 7, 1941, 10:20am
        //if 12/7/1941 10:20 - 12/7/1941 10:22 = December 7, 1941, 10:20am - December 7, 1941, 10:21am
        else if (secondOmmitted && !minuteOmmitted ) {
            if (isOneApart(Calendar.MINUTE, timeRange)) {
                return rangeFormat(timeRange.getBegin(), FMT_TO_MINUTES); 
            } else if (isEqual(Calendar.MINUTE, timeRange)){ //case 2
                return rangeFormat(timeRange, FMT_TO_MINUTES); 
            } else {
                SimpleTimeRange adjusted = subtractOneFromEnd(Calendar.SECOND, timeRange);
                return rangeFormat(adjusted, FMT_TO_MINUTES); 
            }
        } else {
            if (isOneApart(Calendar.SECOND, timeRange)) {
                return rangeFormat(timeRange.getBegin(), FMT_TO_SECONDS); 
            } else if (isEqual(Calendar.SECOND, timeRange)){ //case 2
                return rangeFormat(timeRange, FMT_TO_SECONDS); 
            } else {
                SimpleTimeRange adjusted = subtractOneFromEnd(Calendar.MILLISECOND, timeRange);
                return rangeFormat(adjusted, FMT_TO_SECONDS); 
            }
        }
    }

    private static SimpleTimeRange subtractOneFromEnd(int calendarField, SimpleTimeRange timeRange) {
        Calendar end = getCalendar(timeRange.getEnd());
        end.add(calendarField, -1);
        timeRange.setEnd(end.getTime());
        return timeRange;
    }

    private static boolean isOneApart(int calendarField, SimpleTimeRange tr) {
        return isSeparatedBy(calendarField, 1, tr);
    }
    
    private static boolean isEqual(int calendarField, SimpleTimeRange tr) {
        return isSeparatedBy(calendarField, 0, tr);
    }

    private static boolean isSeparatedBy(int calendarField, int separation, SimpleTimeRange tr) {
        Calendar begin = getCalendar(tr.getBegin());
        Calendar end = getCalendar(tr.getEnd());
        //roll begin by the separation ammount (takes into account boundaries e.g. month 12 + 1 = month 1) 
        begin.roll(calendarField, separation); 
        return (0 == (end.get(calendarField) - begin.get(calendarField)));        
    }

    /**
     * If both begin and end have no hours, minutes or seconds and are both Jan 1, then we 
     * can assume this is a range of 1 year and print it out as such
     * @param tr
     * @return
     */
    private static boolean isBothFirstDayOfYear(SimpleTimeRange tr) {
        return (isBothValue(0, Calendar.SECOND, tr) && isBothValue(0, Calendar.MINUTE, tr) && 
                isBothValue(0, Calendar.HOUR, tr) && isBothValue(1, Calendar.DAY_OF_MONTH, tr) &&
                isBothValue(Calendar.JANUARY, Calendar.MONTH, tr) 
                );
    }

    private static Calendar getCalendar(Date date) {
        Calendar cal = new GregorianCalendar();
        cal.setTime(date);
        return cal;
    }

    /**
     * @param format
     * @return a string with both beginning and end in the format fmt - fmt
     *         (e.g. yyyy - yyyy or MMM yyyy - MMM yyyy)
     */
    private static String rangeFormat(SimpleTimeRange tr, String format ) {
        StringBuffer range = new StringBuffer(rangeFormat(tr.getBegin(), format));
        range.append(" - ");
        range.append(rangeFormat(tr.getEnd(), format));
        return range.toString();
    }

    private static String rangeFormat(Date date, String format) {
        return DateFormatUtils.format(date, format);
    }

    /**
     * 
     * @param value
     * @param calendarField
     * @return true if the calendar field (e.g. MONTH) is equalt to 'value' for both begin
     *         and end
     */
    private static boolean isBothValue(int value, int calendarField, SimpleTimeRange tr) {
        return isValue(value, calendarField, tr.getBegin())
                && isValue(value, calendarField, tr.getEnd());
    }
    
    /**
     * 
     * @param value
     * @param calendarField
     * @param date
     * @return true if the calendarField (e.g. MONTH) is equal to 'value' for this date
     */
    private static boolean isValue(int value, int calendarField, Date date) {
        Calendar cal = new GregorianCalendar();
        cal.setTime(date);
        return value == cal.get(calendarField);
    }


    /**
     * set the end date = the next tick of whatever precision has been
     * specified.
     * 
     * <pre>
     *   
     *   If begin = 1941, then end = 1942
     *   if begin = Sept 30, 1941, then end is Oct 1, 1941
     * </pre>
     * 
     * @param text
     * @return
     */
    private static SimpleTimeRange parseSingleDate(String text) {

        text = StringUtils.trimToEmpty(text);
        try {
            Date begin = parseDate(text);
            GregorianCalendar cal = new GregorianCalendar();
            cal.setTime(begin);

            if (emptyAfter(Calendar.YEAR, cal)) { //e.g. 1941 
                cal.add(Calendar.YEAR, 1); //end = start + 1 year
            }else if (emptyAfter(Calendar.MONTH, cal)) { //e.g. 1941, December 
                cal.add(Calendar.MONTH, 1); //end = start + 1 month
            } else if (emptyAfter(Calendar.DAY_OF_MONTH, cal)) { //e.g. 1941, Dec 7 
                cal.add(Calendar.DAY_OF_MONTH, 1); //end = start + 1 day
            } else if (emptyAfter(Calendar.HOUR, cal)) { //e.g. 1941, Dec 7, 12pm 
                cal.add(Calendar.HOUR, 1); //end = start + 1 hour
            } else if (emptyAfter(Calendar.MINUTE, cal)) { //e.g. 1941, Dec 7, 12:01pm 
                cal.add(Calendar.MINUTE, 1); //end = start + 1 hour
            } else if (emptyAfter(Calendar.SECOND, cal)) { //e.g. 1941, Dec 7, 12:01pm 
                cal.add(Calendar.SECOND, 1); //end = start + 1 hour
            } 
            return new TimeRange(begin, cal.getTime());
        } catch (ParseException e) {
            throw new IllegalArgumentException(e);
        }
    }

    private static boolean emptyAfter(int calendarField, GregorianCalendar cal) {
        boolean emptyAfter = true;

        // 1941, Dec 7, 12:01:01pm
        if (cal.get(Calendar.MILLISECOND) > 0) emptyAfter = false;
        if (calendarField == Calendar.SECOND) return emptyAfter;
        
        // 1941, Dec 7, 12:01pm
        if (cal.get(Calendar.SECOND) > 0) emptyAfter = false;
        if (calendarField == Calendar.MINUTE) return emptyAfter;
        
        // 1941, Dec 7, 12pm
        if (cal.get(Calendar.MINUTE) > 0) emptyAfter = false;
        if (calendarField == Calendar.HOUR) return emptyAfter;

        // 1941, Dec 7
        if (cal.get(Calendar.HOUR) > 0) emptyAfter = false;
        if (calendarField == Calendar.DAY_OF_MONTH) return emptyAfter;
        
        // 1941, December
        if (cal.get(Calendar.DAY_OF_MONTH) > 1) emptyAfter = false;
        if (calendarField == Calendar.MONTH) return emptyAfter;

        // 1941
        if (cal.get(Calendar.MONTH) > Calendar.JANUARY) emptyAfter = false;
        if (calendarField == Calendar.YEAR) return emptyAfter;

        return emptyAfter;
    }

    private static SimpleTimeRange parseRange(String[] split) throws ParseException {

            Date begin = parseDate(split[0]);
            Date end = parseDate(split[1]);
            // make sure begin is before end
            if (begin.compareTo(end) > 0) {
                StringBuffer sb = new StringBuffer(split[0])
                .append(" ").append(split[1]);
                throw new ParseException(sb.toString() ,0); 
                // TODO add error string or localization code
            }
            SimpleTimeRange newTimeRange = new TimeRange(begin, end);
            //Special Case, 1993 - 1995 means Jan 1, 1993 to Jan 1 1996
            if (isBothFirstDayOfYear(newTimeRange)) {
                Calendar cal = getCalendar(newTimeRange.getEnd());
                cal.add(Calendar.YEAR, 1);
                newTimeRange.setEnd(cal.getTime());
            }
            return newTimeRange;
 
    }

    private static Date parseDate(String text) throws ParseException {
        // first clean the spaces from front and back
        text = StringUtils.trimToEmpty(text);
        // convert double and tripple space to single space
        text = StringUtils.replace(text, "    ", " ");
        text = StringUtils.replace(text, "   ", " ");
        text = StringUtils.replace(text, "  ", " ");
        text = normalizeCommas(text);
        return DateUtils.parseDate(text, patterns);
    }

    private static String normalizeCommas(String text) {
        StringBuffer normalized = new StringBuffer();
        byte[] b = text.getBytes();
        for (int i = 0; i < b.length; i++) {
            normalized.append((char)b[i]);
            if (b[i] == ',') {
                if ((i != (b.length-1)) &&  //not at end
                    (b[i+1] != ' ')) {     //missing a trailing space
                    //add a trailing space
                    normalized.append(' ');
                }
            }
            
        }
        return normalized.toString();
    }

}
