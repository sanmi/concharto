/*******************************************************************************
 * Copyright 2009 Time Space Map, LLC
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package org.tsm.concharto.util;

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
