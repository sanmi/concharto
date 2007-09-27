package com.tech4d.tsm.web.util;

import com.tech4d.tsm.model.geometry.TimeRange;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;

import java.beans.PropertyEditorSupport;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Converts string to TimeRange and vica versa. Supports many human readable
 * formats. TODO INCOMPLETE 9-25-07
 */
public class TimeRangePropertyEditor extends PropertyEditorSupport {

    private TimeRange timeRange;

    private static final String[] PARSE_PATTERNS = { // NOTE: whitespace is
            // removed before
            // parsing
            "MM/dd/yy", "yyyy/MM/dd", "MMMyy", "MMM,yy", "yy,MMM", "MMMdd,yy", "ddMMM,yy",
            "yy,ddMMM", "MMMyyyy", "MMM,yyyy", "yyyy,MMM", "MMMdd,yyyy", "ddMMM,yyyy",
            "yyyy,ddMMM", "yyyy" };

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
        // TODO debug incomplete!
        if (this.timeRange == null) {
            return null;
        }
        boolean hourOmmitted = isValue(0, Calendar.HOUR, this.timeRange);
        boolean dayOmmitted = isValue(0, Calendar.DAY_OF_MONTH, this.timeRange);
        boolean monthOmmitted = isValue(0, Calendar.MONTH, this.timeRange);

        //special case for 1/1/yyyy
        if (isBothFirstDayOfYear(this.timeRange)) {
            if (isOneYearApart(this.timeRange)) {
                return rangeFormat(this.timeRange.getBegin(), "yyyy");  //e.g. 1941
            } else {
                //special case for 1/1/2001 - 1/1/2003 = 2001 - 2002
                Calendar end = getCalendar(this.timeRange.getEnd());
                end.add(Calendar.YEAR, -1);
                TimeRange adjusted = new TimeRange(this.timeRange.getBegin(), end.getTime());
                return rangeFormat("yyyy", adjusted);  //e.g 1941 - 1942
            }
        } else if (hourOmmitted && !dayOmmitted) {
            return rangeFormat("MMMM dd, yyyy", this.timeRange);
        } else if (dayOmmitted && !monthOmmitted) {
            return rangeFormat("MMMM yyyy", this.timeRange);
        } else {
            return rangeFormat("yyyy", this.timeRange);
        }

    }

    /**
     * If both begin and end have no hours, minutes or seconds and are both Jan 1, then we 
     * can assume this is a range of 1 year and print it out as such
     * @param tr
     * @return
     */
    private boolean isBothFirstDayOfYear(TimeRange tr) {
        return (isValue(0, Calendar.SECOND, tr) && isValue(0, Calendar.MINUTE, tr) && 
                isValue(0, Calendar.HOUR, tr) && isValue(1, Calendar.DAY_OF_MONTH, tr) &&
                isValue(Calendar.JANUARY, Calendar.MONTH, tr) 
                );
    }

    private boolean isOneYearApart(TimeRange tr) {
        Calendar begin = getCalendar(tr.getBegin());
        Calendar end = getCalendar(tr.getEnd());
        return (1 == (end.get(Calendar.YEAR) - begin.get(Calendar.YEAR)));
    }

    private Calendar getCalendar(Date date) {
        Calendar cal = new GregorianCalendar();
        cal.setTime(date);
        return cal;
    }

    /**
     * @param format
     * @return a string with both beginning and end in the format fmt - fmt
     *         (e.g. yyyy - yyyy or MMM yyyy - MMM yyyy)
     */
    private String rangeFormat(String format, TimeRange tr) {
        StringBuffer range = new StringBuffer(rangeFormat(tr.getBegin(), format));
        range.append(" - ");
        range.append(rangeFormat(tr.getEnd(), format));
        return range.toString();
    }

    private String rangeFormat(Date date, String format) {
        return DateFormatUtils.format(date, format);
    }

    /**
     * 
     * @param value
     * @param calendarField
     * @return true if the calendar field (e.g. MONTH) is equalt to 'value' for both begin
     *         and end
     */
    private boolean isValue(int value, int calendarField, TimeRange tr) {
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
    private boolean isValue(int value, int calendarField, Date date) {
        Calendar cal = new GregorianCalendar();
        cal.setTime(date);
        return value == cal.get(calendarField);
    }

    @Override
    public void setAsText(String text) throws IllegalArgumentException {
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
                throw new IllegalArgumentException();
            }

            // if there are two dates, then we parse each one
            if (isRange) {
                this.timeRange = parseRange(split);
            } else {
                // turn this single date into a range
                this.timeRange = parseSingleDate(text);
            }
        }
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
    private TimeRange parseSingleDate(String text) {

        text = StringUtils.trimToEmpty(text);
        try {
            Date begin = parseDate(text);
            GregorianCalendar cal = new GregorianCalendar();
            cal.setTime(begin);
            int month = cal.get(Calendar.MONTH);
            int day = cal.get(Calendar.DAY_OF_MONTH);
            int hour = cal.get(Calendar.HOUR);
            int minute = cal.get(Calendar.MINUTE);
            int second = cal.get(Calendar.SECOND);

            // minutes but not seconds
            if (second != 0) {
                cal.add(Calendar.SECOND, 1);
            } else if ((minute != 0) && (second == 0)) {
                cal.add(Calendar.MINUTE, 1);
            } else if ((hour != 0) && (minute == 0)) {
                cal.add(Calendar.HOUR, 1);
            } else if ((day != 1) && (hour == 0)) {
                cal.add(Calendar.DAY_OF_MONTH, 1);
            } else if ((month != Calendar.JANUARY) && (day == 0)) {
                cal.add(Calendar.MONTH, 1);
            } else {
                cal.add(Calendar.YEAR, 1);
            }
            return new TimeRange(begin, cal.getTime());
        } catch (ParseException e) {
            throw new IllegalArgumentException(e);
        }
    }

    private TimeRange parseRange(String[] split) {
        try {
            Date begin = parseDate(split[0]);
            Date end = parseDate(split[1]);
            // make sure begin is before end
            if (begin.compareTo(end) > 0) {
                throw new IllegalArgumentException(); // TODO add error string
                // or localization code
            }
            TimeRange newTimeRange = new TimeRange(begin, end);
            //Special Case, 1993 - 1995 means Jan 1, 1993 to Jan 1 1996
            if (isBothFirstDayOfYear(newTimeRange)) {
                Calendar cal = getCalendar(newTimeRange.getEnd());
                cal.add(Calendar.YEAR, 1);
                newTimeRange.setEnd(cal.getTime());
            }
            return newTimeRange;
        } catch (ParseException e) {
            throw new IllegalArgumentException(e);
        }
    }

    private Date parseDate(String text) throws ParseException {
        // first clean the spaces from front and back
        text = StringUtils.trimToEmpty(text);
        // convert empty space to single space
        text = StringUtils.deleteWhitespace(text);

        return DateUtils.parseDate(text, PARSE_PATTERNS);
    }

}
