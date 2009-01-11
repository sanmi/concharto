/*******************************************************************************
 * ***** BEGIN LICENSE BLOCK *****
 * Version: MPL 1.1
 * 
 * The contents of this file are subject to the Mozilla Public License Version 
 * 1.1 (the "License"); you may not use this file except in compliance with 
 * the License. You may obtain a copy of the License at 
 * http://www.mozilla.org/MPL/
 * 
 * Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 * for the specific language governing rights and limitations under the
 * License.
 * 
 * The Original Code is Concharto.
 * 
 * The Initial Developer of the Original Code is
 * Time Space Map, LLC
 * Portions created by the Initial Developer are Copyright (C) 2007 - 2009
 * the Initial Developer. All Rights Reserved.
 * 
 * Contributor(s):
 * Time Space Map, LLC
 * 
 * ***** END LICENSE BLOCK *****
 ******************************************************************************/
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
