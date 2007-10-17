package com.tech4d.tsm.util;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;

import com.tech4d.tsm.model.time.SimpleTimeRange;
import com.tech4d.tsm.model.time.TimeRange;

/**
 * Converts string to TimeRange and vica versa. Supports many human readable
 * formats.
 * 
 * Some examples:
 * <pre>
 * Parsing:
 *   1941 = 1/1/1941 00:00:00 to 1/1/1942 00:00:00
 *   December 1, 1941 - December 2, 1941 =  12/1/1941 00:00:00 to 12/3/1941 00:00:00
 *   
 * Formatting:
 *   if 12/7/1941 00:00 - 12/8/1941 00:00 = December 7, 1941
 *   if 12/7/1941 00:00 - 12/9/1941 00:00 = December 7, 1941, December 8, 1941 (subtract)
 *   if 12/7/1941 00:00 - 12/7/1942 00:00 = December 7, 1941, December 7, 1941 (don't subtract)
 * </pre>
 */
public class TimeRangeFormat  {

    public static final int PRECISION_SECOND = 0;
    public static final int PRECISION_MINUTE = 1;
    public static final int PRECISION_HOUR = 2;
    public static final int PRECISION_DAY = 3;
    public static final int PRECISION_MONTH = 4;
    public static final int PRECISION_YEAR = 5;
    
    private static final String FMT_TO_YEAR = "yyyy";
    private static final String FMT_TO_MONTH = "MMMM yyyy";
    private static final String FMT_TO_DAY = "MMMM dd, yyyy";
    private static final String FMT_TO_HOUR = "MMMM dd, yyyy, hha";
    private static final String FMT_TO_MINUTE = "MMMM dd, yyyy, hh:mma";
    private static final String FMT_TO_SECOND = "MMMM dd, yyyy, hh:mm:ssa";
    
  
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
    private static List<CalendarPrecision> calendarPrecisions = new ArrayList<CalendarPrecision>();
    static {
        List<String> tmpPatterns = new ArrayList<String>();
        //the solo year patterns
        addPatterns(dayPatterns, tmpPatterns);
        addPatterns(timePatterns, tmpPatterns);
        addPatterns(yearPatterns, tmpPatterns);

        //combined format patterns
        for (String dayPattern : dayPatterns) {
            for (String timePattern : timePatterns) {
                tmpPatterns.add(dayPattern + timePattern);
                tmpPatterns.add(new StringBuffer(dayPattern).append(',').append(timePattern).toString());
            }
        }
        patterns = tmpPatterns.toArray(new String[tmpPatterns.size()]);
        
        //for calculating the date precision 
        calendarPrecisions.add(new CalendarPrecision(PRECISION_SECOND, Calendar.SECOND, 0, FMT_TO_SECOND));
        calendarPrecisions.add(new CalendarPrecision(PRECISION_MINUTE, Calendar.MINUTE, 0, FMT_TO_MINUTE));
        calendarPrecisions.add(new CalendarPrecision(PRECISION_HOUR, Calendar.HOUR, 0, FMT_TO_HOUR));
        calendarPrecisions.add(new CalendarPrecision(PRECISION_DAY, Calendar.DAY_OF_MONTH, 1, FMT_TO_DAY));
        calendarPrecisions.add(new CalendarPrecision(PRECISION_MONTH, Calendar.MONTH, Calendar.JANUARY, FMT_TO_MONTH));
        calendarPrecisions.add(new CalendarPrecision(PRECISION_YEAR, Calendar.YEAR, -1, FMT_TO_YEAR));  //no empty value
    }
    
    private static void addPatterns(String[] patterns, List<String> list) {
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
    public static TimeRange parse(String text) throws ParseException {
        TimeRange tr = null;
        if (!StringUtils.isEmpty(text)) {
            // first separate the begin and the end
            // we will try the '-' first
            String[] split = StringUtils.split(text, '-');
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

    /**
     * Format a time range to a string.  
     * @param timeRange TimeRange
     * @return TimeRange formatted as a string
     */
    public static String format(SimpleTimeRange timeRange) {
        if (timeRange == null) {
            return null;
        }
        
        //1) if 12/7/1941 00:00 - 12/8/1941 00:00 = December 7, 1941
        //2) if 12/7/1941 00:00 - 12/9/1941 00:00 = December 7, 1941, December 8, 1941 (subtract)
        //3) if 12/7/1941 00:00 - 12/7/1942 00:00 = December 7, 1941, December 7, 1941 (don't subtract)
        CalendarPrecision cp = getPrecision(timeRange);
        if (isOneApart(cp.getCalendarField(), timeRange)) { //case 1
            return dateFormat(timeRange.getBegin(), cp.getFormat()); 
        } else if (isEqual(cp.getCalendarField(), timeRange)){ //case 2
            return rangeFormat(timeRange, cp.getFormat()); 
        } else { //case 3
            SimpleTimeRange adjusted = subtractOneFromEnd(cp.getCalendarField(), timeRange);
            return rangeFormat(adjusted, cp.getFormat()); 
        }
    }

    private static SimpleTimeRange subtractOneFromEnd(int calendarField, SimpleTimeRange timeRange) {
        TimeRange adjusted = new TimeRange(timeRange.getBegin(), timeRange.getEnd());
        Calendar end = getCalendar(timeRange.getEnd());
        end.add(calendarField, -1);
        adjusted.setEnd(end.getTime());
        return adjusted;
    }

    private static boolean isOneApart(int calendarField, SimpleTimeRange tr) {
        return isSeparatedBy(calendarField, 1, tr);
    }
    
    private static boolean isEqual(int calendarField, SimpleTimeRange tr) {
        return isSeparatedBy(calendarField, 0, tr);
    }

    /**
     * evaluates whether the separation between the begin and end is equal to 'separation' parameter
     * @param calendarField calendar field (e.g. Calendar.MONTH)
     * @param separation number of places of separation
     * @param tr SimpleTimeRange
     * @return true if begin-end = separation for the given calendar field (e.g. Calendar.MONTH)
     */
    private static boolean isSeparatedBy(int calendarField, int separation, SimpleTimeRange tr) {
        Calendar begin = getCalendar(tr.getBegin());
        Calendar end = getCalendar(tr.getEnd());
        //roll begin by the separation ammount (takes into account boundaries e.g. month 12 + 1 = month 1) 
        begin.roll(calendarField, separation); 
        return (0 == (end.get(calendarField) - begin.get(calendarField)));        
    }

    private static Calendar getCalendar(Date date) {
        Calendar cal = new GregorianCalendar();
        cal.setTime(date);
        return cal;
    }

    /**
     * @param tr SimpleTimeRange
     * @param format format string
     * @return a string with both beginning and end in the format fmt - fmt
     *         (e.g. yyyy - yyyy or MMM yyyy - MMM yyyy)
     */
    private static String rangeFormat(SimpleTimeRange tr, String format ) {
        StringBuffer range = new StringBuffer(dateFormat(tr.getBegin(), format));
        range.append(" - ");
        range.append(dateFormat(tr.getEnd(), format));
        return range.toString();
    }

    /** 
     * format a date
     * @param date date
     * @param format string (see SimpleDateFormat)
     * @return String formatted string
     */
    private static String dateFormat(Date date, String format) {
        return DateFormatUtils.format(date, format);
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
     * @param text text to parse
     * @return TimeRange a time range
     */
    private static TimeRange parseSingleDate(String text) {

        text = StringUtils.trimToEmpty(text);
        try {
            
            Date begin = parseDate(text);
            
            CalendarPrecision cp = getPrecision(begin);
            //add 1 to the end at the given precision (e.g. when someone says 
            //December 1 to December 2 they mean 12/1 00:00:00 to 12/3 00:00:00) 
            Calendar cal = getCalendar(begin);
            cal.add(cp.getCalendarField(), 1);
            Date end = cal.getTime();
            
            return new TimeRange(begin, end);
            
        } catch (ParseException e) {
            throw new IllegalArgumentException(e);
        }
    }

    /**
     * Parses two strings into a SimpleTimeRange
     *  
     * @param split an arr
     * @return SimpleTimeRange new time range
     * @throws ParseException exception if there is a parsing problem
     */
    private static TimeRange parseRange(String[] split) throws ParseException {

            Date begin = parseDate(split[0]);
            Date end = parseDate(split[1]);
            // make sure begin is before end
            if (begin.compareTo(end) > 0) {
                StringBuffer sb = new StringBuffer(split[0])
                .append(" ").append(split[1]);
                throw new ParseException(sb.toString() ,0); 
                // TODO add error string or localization code
            }
            CalendarPrecision cp = getPrecision(end);
            //add 1 to the end at the given precision (e.g. when someone says 
            //December 1 to December 2 they mean 12/1 00:00:00 to 12/3 00:00:00) 
            Calendar cal = getCalendar(end);
            cal.add(cp.getCalendarField(), 1);
            end = cal.getTime();
            
            return new TimeRange(begin, end);
    }

    /**
     * Parses dates from a very wide variety of formats and precisions
     * @param text text to parse
     * @return Date date
     * @throws ParseException if there is a parsing problem
     */
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

    /**
     * Converts strings in the form "a,b" to "a, b"
     * @param text text to normalize
     * @return converted text
     */
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

    /**
     * Calculate the precision for this date by looking at 
     * the pattern of default empty values.  E.g. 12/1/2007 00:00:00 
     * has a precision of "month" and 1/1/2007 00:00:00 has a precision
     * of "year" 
     * @param date Date to check
     * @return CalendarPrecision precision
     */
    private static CalendarPrecision getPrecision(Date date) {
        Calendar cal = new GregorianCalendar();
        cal.setTime(date);
        CalendarPrecision precision = null;
        //start with second and check until we find a non-empty value
        for (CalendarPrecision cp : calendarPrecisions) {
            if (cal.get(cp.getCalendarField()) != cp.getEmptyValue()) {
                precision = cp;
                break;
            } 
        }
        return precision;
    }

    /**
     * Get the least precicse precision of begin and end for this time range
     * @param timeRange TimeRange to check
     * @return CalendarPrecision precision
     */
    private static CalendarPrecision getPrecision(SimpleTimeRange timeRange) {
        CalendarPrecision beginCp = getPrecision(timeRange.getBegin());
        CalendarPrecision endCp = getPrecision(timeRange.getEnd());
        //order is specified by its position in the array
        if (beginCp.getRank() < endCp.getRank()) {
            return beginCp;
        } else {
            return endCp;
        }
    }
}
