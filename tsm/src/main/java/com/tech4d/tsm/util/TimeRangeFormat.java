package com.tech4d.tsm.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;

import com.tech4d.tsm.model.time.SimpleTimeRange;
import com.tech4d.tsm.model.time.TimeRange;
import com.tech4d.tsm.model.time.VariablePrecisionDate;

/**
 * Converts string to TimeRange and vica versa. Supports many human readable
 * formats.  The best way to try to understand this code is to look at the 
 * unit tests.
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

    private static final int DIGITS_IN_YEAR = 4;
    private static final int MAX_YEAR_TO_DISLPAY_ERA = 1000;
    private static final String ERA_BC = "BC";
    private static final String ERA_AD = "AD";
    
    private static final String FMT_TO_YEAR = "yyyy";
    private static final String FMT_TO_MONTH = "MMMM, yyyy";
    private static final String FMT_TO_DAY = "MMMM dd, yyyy";
    private static final String FMT_TO_HOUR = "MMMM dd, yyyy, hha";
    private static final String FMT_TO_MINUTE = "MMMM dd, yyyy, hh:mma";
    private static final String FMT_TO_SECOND = "MMMM dd, yyyy, hh:mm:ssa";

    private static final String FMT_TO_YEAR_ERA = "yyyy G";
    private static final String FMT_TO_MONTH_ERA = "MMMM, yyyy G";
    private static final String FMT_TO_DAY_ERA = "MMMM dd, yyyy G";
    private static final String FMT_TO_HOUR_ERA = "MMMM dd, yyyy G, hha";
    private static final String FMT_TO_MINUTE_ERA = "MMMM dd, yyyy G, hh:mma";
    private static final String FMT_TO_SECOND_ERA = "MMMM dd, yyyy G, hh:mm:ssa";
 
    private final static String[] yearPatterns = {
        "yyyy", 
        "yyyyG",
        "yyyy G"
    };

    private final static String[] monthPatterns = {
        "MMM yy", 
        "MMM, yy", 
        "yyyy, MMM", 
        "MMM, yyyy G", 
        "yyyy G, MMM", 
    };
    
    private final static String[] dayPatterns = {
    	"MM/dd/yy", 
    	"yyyy/MM/dd", 
    	"MMM dd, yy", 
    	"MMM dd, yyyy", 
    	"yyyy, dd MMM", 
    	
    	"MM/dd/yyyy G", 
    	"yyyy G/MM/dd", 
    	"MMM dd, yyyy G", 
    	"yyyy G, dd MMM", 
    };
    private final static String[] hourPatterns = {
        " hha", 
        " HH", 
    };
    private final static String[] minutePatterns = {
    	" hh:mma", 
    	" HH:mm", 
    };
    private final static String[] secondPatterns = {
    	" hh:mm:ssa", 
    	" HH:mm:ss", 
    };
        
    private static String[] patterns;
    private static List<CalendarPrecision> calendarPrecisions = new ArrayList<CalendarPrecision>();
    static {
        List<String> dayMonthPatterns = new ArrayList<String>();
        addPatterns(dayPatterns, dayMonthPatterns);
        addPatterns(monthPatterns, dayMonthPatterns);

        List<String> timePatterns = new ArrayList<String>();
        addPatterns(hourPatterns, timePatterns);
        addPatterns(minutePatterns, timePatterns);
        addPatterns(secondPatterns, timePatterns);
        
        //All patterns.  Order is important here
        List<String> tmpPatterns = new ArrayList<String>();
        addPatterns(dayMonthPatterns, tmpPatterns);
        addPatterns(timePatterns, tmpPatterns);
        addPatterns(yearPatterns, tmpPatterns);

        //combined format patterns
        for (String dayPattern : dayMonthPatterns) {
            for (String timePattern : timePatterns) {
                tmpPatterns.add(dayPattern + timePattern);
                tmpPatterns.add(new StringBuffer(dayPattern).append(',').append(timePattern).toString());
            }
        }
        patterns = tmpPatterns.toArray(new String[tmpPatterns.size()]);
        
        //for calculating the date precision 
        calendarPrecisions.add(new CalendarPrecision
        		(VariablePrecisionDate.PRECISION_SECOND, Calendar.SECOND, 0, FMT_TO_SECOND, FMT_TO_SECOND_ERA, combineTimePatterns(dayMonthPatterns, secondPatterns)));
        calendarPrecisions.add(new CalendarPrecision
        		(VariablePrecisionDate.PRECISION_MINUTE, Calendar.MINUTE, 0, FMT_TO_MINUTE, FMT_TO_MINUTE_ERA, combineTimePatterns(dayMonthPatterns, minutePatterns)));        
        calendarPrecisions.add(new CalendarPrecision
        		(VariablePrecisionDate.PRECISION_HOUR, Calendar.HOUR, 0, FMT_TO_HOUR, FMT_TO_HOUR_ERA, combineTimePatterns(dayMonthPatterns, hourPatterns)));
        calendarPrecisions.add(new CalendarPrecision
        		(VariablePrecisionDate.PRECISION_DAY, Calendar.DAY_OF_MONTH, 1, FMT_TO_DAY, FMT_TO_DAY_ERA, dayPatterns));
        calendarPrecisions.add(new CalendarPrecision
        		(VariablePrecisionDate.PRECISION_MONTH, Calendar.MONTH, Calendar.JANUARY, FMT_TO_MONTH, FMT_TO_MONTH_ERA, monthPatterns));
        calendarPrecisions.add(new CalendarPrecision
        		(VariablePrecisionDate.PRECISION_YEAR, Calendar.YEAR, -1, FMT_TO_YEAR, FMT_TO_YEAR_ERA, yearPatterns));  //no empty value

    }
    
    private static List<String> addPatterns(String[] patterns, List<String> list) {
        for (String pattern : patterns) {
            list.add(pattern);
        }        
        return list;
    }
    
	private static void addPatterns(List<String> patterns, List<String> list) {
    	for (String pattern : patterns) {
    		list.add(pattern);
    	}        
    }

    private static String[] combineTimePatterns(List<String> dayMonthPatterns, String[] timePatterns) {
        List<String> results = new ArrayList<String>();
        for (String dayMonthPattern : dayMonthPatterns) {
            for (String timePattern : timePatterns) {
                results.add(dayMonthPattern + timePattern);
                results.add(new StringBuffer(dayMonthPattern).append(',').append(timePattern).toString());
            }
        }
 		return results.toArray(new String[results.size()]);
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
        //2) if 12/7/1941 00:00 - 12/7/1942 00:00 = December 7, 1941, December 7, 1941 (don't subtract)
        //3) if 12/7/1941 00:00 - 12/9/1941 00:00 = December 7, 1941, December 8, 1941 (subtract)
        //4) if 12/7/1941 10:00 - 2/1/1942 00:00 = December 7, 1941 10AM, January, 1942 (don't subtract)
        CalendarPrecision beginCp = getPrecision(timeRange.getBegin());
        CalendarPrecision endCp = getPrecision(timeRange.getEnd());
        CalendarPrecision cp = getLeastPrecision(beginCp, endCp);
        if (isOneApart(cp.getCalendarField(), timeRange)) { //case 1
            return dateFormat(timeRange.getBegin().getDate(), cp); 
        } else if (isEqual(cp.getCalendarField(), timeRange)){ //case 2
            return rangeFormat(timeRange, cp);
        } else { //case 3, 4        	
            SimpleTimeRange adjusted = subtractOneFromEnd(cp, timeRange);
            //Case 4.  But only if precision has been specified, otherwise we have to guess.
            //This guessing is implemented for backward compatibility
            if ((beginCp.getRank() != endCp.getRank()) 
            		&& timeRange.getBegin().getPrecision() != null 
            		&& timeRange.getEnd().getPrecision() != null) { 
            	return rangeFormat(adjusted.getBegin().getDate(), beginCp, adjusted.getEnd().getDate(), endCp);
            } else { //case 3
                return rangeFormat(adjusted, cp); 
            }
        }
    }

	private static SimpleTimeRange subtractOneFromEnd(CalendarPrecision cp, SimpleTimeRange timeRange) {
        TimeRange adjusted = new TimeRange(timeRange.getBegin(), timeRange.getEnd());
        Calendar end = getCalendar(timeRange.getEnd().getDate());
        int calendarField = cp.getCalendarField();
        end.add(calendarField, -1);
        adjusted.setEnd(new VariablePrecisionDate(end.getTime(), cp.getRank()));
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
        GregorianCalendar begin = getCalendar(tr.getBegin().getDate());
        GregorianCalendar end = getCalendar(tr.getEnd().getDate());
        //roll begin by the separation ammount (takes into account boundaries e.g. month 12 + 1 = month 1) 
        if (calendarField == Calendar.YEAR) {
            if (end.get(Calendar.ERA) == GregorianCalendar.BC) {
                separation = - separation;
            }
        }
        begin.roll(calendarField, separation);
        int endField = end.get(calendarField);
        int beginField = begin.get(calendarField);
        
        return (0 == (endField - beginField));        
    }

    private static GregorianCalendar getCalendar(Date date) {
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(date);
        return cal;
    }

    /**
     * @param tr SimpleTimeRange
     * @param cp CalendarPrecision for getting one of the format strings
     * @return a string with both beginning and end in the format fmt - fmt
     *         (e.g. yyyy - yyyy or MMM yyyy - MMM yyyy)
     */
    private static String rangeFormat(SimpleTimeRange tr, CalendarPrecision cp ) {
    	return rangeFormat(tr.getBegin().getDate(), cp, tr.getEnd().getDate(), cp);
    }

    private static String rangeFormat(Date begin, CalendarPrecision beginCp, Date end, CalendarPrecision endCp) {
        StringBuffer range = new StringBuffer(dateFormat(begin, beginCp));
        range.append(" - ");
        range.append(dateFormat(end, endCp));
        return range.toString();
	}

    /** 
     * format a date
     * @param date date
     * @param cp CalendarPrecision for getting one of the format strings
     * @return String formatted string
     */
    private static String dateFormat(Date date, CalendarPrecision cp) {
        String format;
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(date);
        if (cal.get(Calendar.YEAR) < MAX_YEAR_TO_DISLPAY_ERA) {
            format = cp.getFormatWithEra();
        } else {
            format = cp.getFormat();
        }
        
        String text = DateFormatUtils.format(date, format);
        return stripLeadingZeros(date, text);
    }

    /**
     * Make formatted date more user friendly.  For example, '0092 BC' is converted
     * to '92 BC'
     * 
     * @param date original date
     * @param text formatted text representation of that date
     * @return stripped text
     */
    private static String stripLeadingZeros(Date date, String text) {
        //first we have to find the formatted year.
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(date);
        String year = Integer.toString(cal.get(Calendar.YEAR));
        //find the start pos of the year.  If the year is 0093, we will get
        //year = '93' so the start of year = pos of '93' minus 2.
        int yearPos = text.indexOf(year) - DIGITS_IN_YEAR + year.length();
        StringBuffer adjusted = new StringBuffer();
        if (yearPos != 0) {
            adjusted.append(text.substring(0, yearPos));
        }
        adjusted.append(year);
        adjusted.append(text.substring(yearPos+DIGITS_IN_YEAR, text.length()));

        return adjusted.toString();
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
     * @throws ParseException when text can't be parsed
     */
    private static TimeRange parseSingleDate(String text) throws ParseException {

        text = StringUtils.trimToEmpty(text);
            
        Date begin = parseDate(text);
        
        CalendarPrecision cp = getPrecision(text);
        //add 1 to the end at the given precision (e.g. when someone says 
        //December 1 to December 2 they mean 12/1 00:00:00 to 12/3 00:00:00) 
        Calendar cal = getCalendar(begin);
        cal.add(cp.getCalendarField(), 1);
        Date end = cal.getTime();
        
        return new TimeRange(begin, cp.getRank(), end, cp.getRank());
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
            CalendarPrecision beginCp = getPrecision(split[0]);
            CalendarPrecision endCp = getPrecision(split[1]);
            //add 1 to the end at the given precision/  When someone says 
            //December 1 to December 2 they mean 12/1 00:00:00 to 12/3 00:00:00)
            //OR
            //Dec 7, 1940 10am - Jan, 1941 they mean 12/7/40, 10:00:00 to 2/1/41, 00:00:00 
            Calendar cal = getCalendar(end);
            cal.add(endCp.getCalendarField(), 1);
            end = cal.getTime();
            return new TimeRange(begin, beginCp.getRank(), end, endCp.getRank());
    }

    /**
     * Parses dates from a very wide variety of formats and precisions
     * @param text text to parse
     * @return Date date
     * @throws ParseException if there is a parsing problem
     */
    private static Date parseDate(String text) throws ParseException {
        text = normalizeDateText(text);
        SimpleDateFormat sdf = new SimpleDateFormat();
        sdf.setLenient(false);
        return DateUtils.parseDate(sdf, text, patterns);
    }
    
    /**
     * Removes double and triple spaces, normalizes commas and  
     * cleans up era designators
     * @param text to normalize
     * @return normalize date string
     */
    private static String normalizeDateText(String text) {
        // first clean the spaces from front and back
        text = StringUtils.trimToEmpty(text);
        // convert double and triple space to single space
        text = StringUtils.replace(text, "    ", " ");
        text = StringUtils.replace(text, "   ", " ");
        text = StringUtils.replace(text, "  ", " ");
        text = normalizeCommas(text);
        text = adjustADBC(text);
        return text;
    }

    /**
     * Fix the date if there is a missing space between
     * the year and the ERA designator.  For instance,
     * '5000BC' is converted to '5000 BC' and 'March, 50BC' is 
     * converted to 'March, 50 BC'.  If '5000 BC' is passed in
     * no changes are made
     * 
     * @param text to convert
     * @return converted text if necessary  
     */
    private static String adjustADBC(String text) {
        
        if (StringUtils.contains(text, ERA_AD)) {
            text = padEra(ERA_AD, text);
        } else if (StringUtils.contains(text, ERA_BC)) {
            text = padEra(ERA_BC, text);
        }
        return text;
    }

    /**
     * @see TimeRangeFormat#adjustADBC
     * 
     * @param era 'AD' or 'BC'
     * @param text text to convert
     * @return converted text if necessary
     */
    private static String padEra(String era, String text) {
        //get the character just before the ERA text (e.g. AD or BC)
        String before = StringUtils.substringBefore(text, era);
        
        if (' ' != before.charAt(before.length()-1)) {
            //ok we need to insert a space right here
            text = before + " " + text.subSequence(before.length(), text.length());
        }
        return text;
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
    private static CalendarPrecision getPrecision(VariablePrecisionDate date) {
    	if (date.getPrecision() != null) {
    		return getPrecision(date.getPrecision()); 
    	}
        Calendar cal = new GregorianCalendar();
        cal.setTime(date.getDate());
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
     * Find the CalendarPrecision based on the given rank
     * @see com.tech4d.tsm.model.time.VariablePrecisionDate
     * @param precision  integer value (see VariablePrecisionDate)
     * @return matching CalendarPrecision.  Null if none is found (which should never happen)
     */
    private static CalendarPrecision getPrecision(Integer precision) {
		for (CalendarPrecision cp : calendarPrecisions) {
			if (cp.getRank() == precision) {
				return cp;
			}
		}
		return null;
	}

	/**
	 * Calculate the precision for this date by looking at 
     * what the user entered.  E.g. 2007 has a year precision, Jan 2007 has a 
     * month precision, Jan 1, 2007 has a day precision, etc.  NOTE: We need two ways of 
     * checking precision - one for parsing user input (this one) and one for for formatting
     * time ranges for users.
     * 
     * @param text date string
     * @return CalendarPrecision precision
     */
    private static CalendarPrecision getPrecision(String text) {
    	text = normalizeDateText(text);
    	int rank = VariablePrecisionDate.PRECISION_YEAR;
    	for (CalendarPrecision cp : calendarPrecisions)
            if (isParseable(text, cp.getPrecisionTestFormat())) {
            	if (cp.getRank()<rank) {
            		rank = cp.getRank();
            }
    	}
    	return calendarPrecisions.get(rank);
    }
    
    /**
     * Utility for checking precision of user entered dates 
     * @param text date string
     * @param patterns array of patterns (see SimpleDateFormat)
     * @return true if the date is parseable using the provided pattern
     */
    private static boolean isParseable(String text, String[] patterns) {
    	SimpleDateFormat sdf = new SimpleDateFormat();
    	sdf.setLenient(false);
    	for (String pattern : patterns) {
            try {
            	sdf.applyPattern(pattern);
                sdf.parse(text);
                return true;
            } catch (ParseException e) {
                //no action
            }
    	}
    	return false;
    }
    
    /**
     * Get the least precise precision of begin and end for this time range
     * @param beginCp first CalendarPrecision to compare
     * @param endCp   second CalendarPrecision to compare
     * @return CalendarPrecision precision
     */
    private static CalendarPrecision getLeastPrecision(CalendarPrecision beginCp, CalendarPrecision endCp) {
        //order is specified by its position in the array
        if (beginCp.getRank() < endCp.getRank()) {
            return beginCp;
        } else {
            return endCp;
        }
    }
}
