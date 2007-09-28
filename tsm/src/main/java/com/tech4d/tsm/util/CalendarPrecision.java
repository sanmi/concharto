package com.tech4d.tsm.util;

/**
 * Util class for use by TimeRangeFormat
 * @author frank
 */
public class CalendarPrecision { 
    private int rank;
    private int calendarField;
    private int emptyValue;
    private String format;
    
    public int getEmptyValue() {
        return emptyValue;
    }
    public CalendarPrecision(int rank, int calendarField, int emptyValue, String format) {
        this.rank = rank;
        this.calendarField = calendarField;
        this.format = format;
        this.emptyValue = emptyValue;
    }
    public int getCalendarField() {
        return calendarField;
    }
    public String getFormat() {
        return format;
    }
    public int getRank() {
        return rank;
    }
}