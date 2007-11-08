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
    private String formatWithEra;
    private String[] precisionTestFormat;
    
    public int getEmptyValue() {
        return emptyValue;
    }
    public CalendarPrecision(int rank, int calendarField, int emptyValue, 
    		String format, String formatWithEra, String[] precisionTestFormat) {
        this.rank = rank;
        this.calendarField = calendarField;
        this.format = format;
        this.formatWithEra = formatWithEra;
        this.emptyValue = emptyValue;
        this.precisionTestFormat = precisionTestFormat;
    }
    public int getCalendarField() {
        return calendarField;
    }
    public String getFormat() {
        return format;
    }
    public String getFormatWithEra() {
        return formatWithEra;
    }
    public int getRank() {
        return rank;
    }
	public String[] getPrecisionTestFormat() {
		return precisionTestFormat;
	}
}