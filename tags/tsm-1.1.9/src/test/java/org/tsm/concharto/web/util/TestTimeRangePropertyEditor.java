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
package org.tsm.concharto.web.util;

import static org.junit.Assert.assertEquals;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.junit.Test;
import org.tsm.concharto.model.time.SimpleTimeRange;
import org.tsm.concharto.model.time.TimeRange;
import org.tsm.concharto.web.util.TimeRangePropertyEditor;


public class TestTimeRangePropertyEditor {

    private TimeRangePropertyEditor timeRangePropertyEditor = new TimeRangePropertyEditor();
    
    @Test public void setValueAsText() throws ParseException {
        //NOTE the parsing tests happen in TestTimeRangeFormat
        assertEquals(makeDate(1,1,2007), getBegin("2007"));
        assertEquals(makeDate(3,1,2007), getBegin("2007,   March"));

    }
    
    @Test public void getValueAsText() {
        assertEquals("1947", transformToText(1,1,1947,1,1,1948));
        assertEquals("December 07, 1941 - December 07, 1942", transformToText(12,7,1941,12,7,1942));
    }
    
    //TODO refactor this to share with TestTimeRangeFormat
    private Object transformToText(int m1, int d1, int y1, int m2, int d2, int y2) {
        SimpleTimeRange timeRange = makeDayRange(m1, d1, y1, m2, d2, y2);
        timeRangePropertyEditor.setValue(timeRange);
        return timeRangePropertyEditor.getAsText();
    }

    private Date getBegin(String text) {
        timeRangePropertyEditor.setAsText(text);       
        return ((org.tsm.concharto.model.time.SimpleTimeRange)(timeRangePropertyEditor.getValue())).getBegin().getDate();
    }


    private org.tsm.concharto.model.time.SimpleTimeRange makeDayRange(int m1, int d1, int y1, int m2, int d2, int y2) {
        return new TimeRange(makeDate(m1, d1, y1), makeDate(m2, d2, y2));
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
