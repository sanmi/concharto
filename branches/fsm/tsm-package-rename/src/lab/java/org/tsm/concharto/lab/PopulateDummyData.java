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
package org.tsm.concharto.lab;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.SessionFactory;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;

import com.tech4d.tsm.dao.EventTesterDao;
import com.tech4d.tsm.dao.EventUtil;
import com.tech4d.tsm.model.Event;
import com.tech4d.tsm.model.time.TimeRange;
import com.tech4d.tsm.model.time.VariablePrecisionDate;
import com.tech4d.tsm.util.ContextUtil;
import com.tech4d.tsm.util.LapTimer;
import com.tech4d.tsm.util.TimeRangeFormat;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;

public class PopulateDummyData {
    
    private static final int SZ_TAGS = 70;
    private static final int SZ_WHERE = 80;
    private static final int SZ_SOURCE = 100;
    private static final int SZ_SUMMARY = 50;
    private static final int SZ_DESCRIPTION = 240;
    private static final String TEXT_FILE = "src/lab/data/a-tale-of-two-cities.txt";
    private static EventTesterDao eventTesterDao;
    private static EventUtil eventUtil;
    private GeometryFactory gf = new GeometryFactory();
    protected static final Log logger = LogFactory.getLog(PopulateDummyData.class);

    private static final Double LONGITUDE_180 = 180d;
    private static final Double LATITUDE_90 = 90d;
    private Random rand = new Random(System.currentTimeMillis());
    private static BufferedReader textFileReader;

    @BeforeClass
    public static void setUpClass() throws FileNotFoundException {
        ApplicationContext appCtx = ContextUtil.getCtx();
        
        eventTesterDao = (EventTesterDao) appCtx.getBean("eventTesterDao");
        SessionFactory sessionFactory = (SessionFactory) appCtx.getBean("myTestDbSessionFactory");
        //replace the default with the test db
        eventTesterDao.setSessionFactory(sessionFactory);

        eventUtil = new EventUtil(eventTesterDao.getSessionFactory());
        logger.debug("deleting");
        LapTimer timer = new LapTimer(logger);
        eventTesterDao.deleteEvents();
        timer.timeIt("delete").logDebugTime();
        
        openTextFileReader();
    }
    
    private static void openTextFileReader() throws FileNotFoundException {
        File textFile = new File(TEXT_FILE);
        textFileReader = new BufferedReader(new FileReader(textFile));
    }

    @AfterClass
    public static void tearDown() throws IOException {
        if (textFileReader != null) {
            textFileReader.close();
        }
    }
    
    private static final int NUM_EVENTS = 10000;
    private static final int COLLECTION_SIZE = 1000;
    @Test
    public void makeData() throws ParseException, IOException {
        Set<Event> events = new HashSet<Event>();
        LapTimer timer = new LapTimer(this); 
        for (int i = 0; i < NUM_EVENTS; i++) {
            Event event = eventUtil.createEvent( null, getNextPoint(), getNextTimeRange(), null,
                    getNextText(SZ_SUMMARY), getNextText(SZ_DESCRIPTION));
            event.setSnippet(null);
            event.setSource(getNextText(SZ_SOURCE));
            event.setWhere(getNextText(SZ_WHERE));
            event.setUserTagsAsString(getNextText(SZ_TAGS));
            events.add(event);
            
            if (i % COLLECTION_SIZE == 0) {
                timer.timeIt("create " + i);
                eventTesterDao.save(events);
                timer.timeIt("save").logDebugTime();
                events.clear();
                timer.init();
            }
        }
        timer.timeIt("create");
        eventTesterDao.save(events);
        timer.timeIt("save").logDebugTime();

        //System.out.println(minLng +", " + minLat + ", " + maxLng + ", " + maxLat );
    }

    private String getNextText(int fieldSize) throws IOException {
        int summarySize = rand.nextInt(fieldSize);
        char[] chars = new char[summarySize];
        int b;
        for (int i=0; i<summarySize; i++) {
            if (-1 != (b = textFileReader.read())) {
                chars[i] = (char)b ;            
            } else {
                System.out.println("end of file reached.  Starting over again");
                textFileReader.close();
                openTextFileReader();
            }
        }
        return String.valueOf(chars);
    }

    private TimeRange getNextTimeRange() throws ParseException {
        //first decide which precision to use
        int precision = rand.nextInt(100);
        if (precision <35) {
            return makeSingleTimeRange(VariablePrecisionDate.PRECISION_YEAR);
        } else if (precision <50){
            //two date/times within the same year betweeo 1000 and 2000
            //noinspection UnnecessaryLocalVariable
            TimeRange timeRange = makeDayRange(
                    1,1,nextYear(),0,0,0, 
                    1,1,nextYear(),0,0,0 
                   );
            return timeRange;
        } else if (precision <65) { 
            return makeSingleTimeRange(VariablePrecisionDate.PRECISION_MONTH);
        } else if (precision <75) { 
            return makeSingleTimeRange(VariablePrecisionDate.PRECISION_DAY);
        } else if (precision <85) { 
            return makeSingleTimeRange(VariablePrecisionDate.PRECISION_HOUR);
        } else if (precision <95){
            //two date/times within the same year betweeo 1000 and 2000
            int year = rand.nextInt(YEAR) + YEAR;
            return makeDayRange(
                     rand.nextInt(MONTH),1, year,0,0,0, 
                     rand.nextInt(MONTH),1, year,0,0,0 
                    );
        } else if (precision <98){
            //two date/times within the same year betweeo 1000 and 2000
            int year = rand.nextInt(YEAR) + YEAR;
            return makeDayRange(
                     rand.nextInt(MONTH), rand.nextInt(DAY+1),year,0,0,0, 
                     rand.nextInt(MONTH), rand.nextInt(DAY+1),year,0,0,0 
                    );
        } else {
            //two date/times within the same year betweeo 1000 and 2000
            int year = rand.nextInt(YEAR) + YEAR;
            return makeDayRange(
                     rand.nextInt(MONTH), //month 
                     rand.nextInt(DAY+1),//d1
                     year,              //y1
                     rand.nextInt(HOUR), //hh1 
                     rand.nextInt(MIN), //mm1, 
                     rand.nextInt(SEC), //ss1, 
                     rand.nextInt(MONTH), //m2 
                     rand.nextInt(DAY+1),//d2
                     year,              //y2
                     rand.nextInt(HOUR), //hh2 
                     rand.nextInt(MIN), //mm2, 
                     rand.nextInt(SEC)  //ss2, 
                    );
        }
        
    }
    
    int nextYear() {
        return rand.nextInt(YEAR) + YEAR;
    }
    
    private static final int YEAR = 1000;
    private static final int MONTH = 12;
    private static final int HOUR = 24;
    private static final int MIN = 60;
    private static final int SEC = 60;
    private static final int DAY = 27;
    private TimeRange makeSingleTimeRange(int precision) throws ParseException {
        int year = nextYear();
        if (precision == VariablePrecisionDate.PRECISION_YEAR) {
            return TimeRangeFormat.parse(String.valueOf(year));
        } else if (precision == VariablePrecisionDate.PRECISION_MONTH) {
            return makeSingleTimeRange( rand.nextInt(MONTH),1,year,0,0,0);
        } else if (precision == VariablePrecisionDate.PRECISION_DAY) {
            return makeSingleTimeRange( rand.nextInt(MONTH),rand.nextInt(DAY),year,0,0,0);
        } else if (precision == VariablePrecisionDate.PRECISION_HOUR) {
            return makeSingleTimeRange( rand.nextInt(MONTH),rand.nextInt(DAY),year,
                    rand.nextInt(HOUR),0,0);
        } else if (precision == VariablePrecisionDate.PRECISION_MINUTE) {
            return makeSingleTimeRange( rand.nextInt(MONTH),rand.nextInt(DAY),year,
                    rand.nextInt(HOUR),rand.nextInt(MIN),0);
        } 
        return null;
    }

    //roughly the US region
    private static final double WESTMOST = -127.353515;
    private static final double EASTMOST = -59.765625;
    private static final double NORTHMOST = 57.231502;
    private static final double SOUTHMOST = 23.725011;
    private static double maxLat = -LATITUDE_90;
    private static double maxLng = -LONGITUDE_180;
    private static double minLat = LATITUDE_90;
    private static double minLng = LONGITUDE_180;
    
    private Geometry getNextPoint() {
        double lng = rand.nextDouble() * (EASTMOST-WESTMOST) + WESTMOST;
        double lat = rand.nextDouble() * (NORTHMOST-SOUTHMOST) + SOUTHMOST;
        minLat = checkMin(lat, minLat);
        minLng = checkMin(lng, minLng);
        maxLat = checkMax(lat, maxLat);
        maxLng = checkMax(lng, maxLng);
        return gf.createPoint(new Coordinate(lng, lat));
    }

    private double checkMax(double val, double maxval) {
        if (val > maxval) {
            return val;
        } else {
            return maxval;
        }
    }

    private double checkMin(double val, double minval) {
        if (val < minval) {
            return val;
        } else {
            return minval;
        }
    }

    /* 
     * Make a single date date range (e.g. Dec 12, 1941).  It is easiest to use the TimeRangeFormat
     * parsing logic for this 
     */
    public TimeRange makeSingleTimeRange(int m1, int d1, int y1, int hh1, int mm1, int ss1) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("MMMM dd, yyyy hh:mm:ss");
        String dateStr = sdf.format(makeDate(m1, d1, y1, hh1, mm1, ss1));
        //noinspection UnnecessaryLocalVariable
        TimeRange timeRange = TimeRangeFormat.parse(dateStr);
        //System.out.println(TimeRangeFormat.format(timeRange));
        return timeRange; 
    }

    public TimeRange makeDayRange(int m1, int d1, int y1, int hh1, int mm1, int ss1, 
            int m2, int d2, int y2, int hh2, int mm2, int ss2) {
        Date date1 = makeDate(m1, d1, y1, hh1, mm1, ss1);
        Date date2 = makeDate(m2, d2, y2, hh2, mm2, ss2);
        TimeRange timeRange;
        //switch dates if they are backward
        if (date1.compareTo(date2) >=0) {
            timeRange = new TimeRange(date2, date1);
        } else {
            timeRange = new TimeRange(date1,date2);
        }
        return timeRange;
    }

    private Date makeDate(int month, int day, int year, int hour, int minute, int second) {
        Calendar cal = new GregorianCalendar();
        cal.set(year, month-1, day, hour, minute, second);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

}
