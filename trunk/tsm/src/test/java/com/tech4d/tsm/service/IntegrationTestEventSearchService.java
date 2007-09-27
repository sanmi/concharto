package com.tech4d.tsm.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;

import com.tech4d.tsm.dao.StyleUtil;
import com.tech4d.tsm.dao.TsEventDao;
import com.tech4d.tsm.dao.TsEventUtil;
import com.tech4d.tsm.model.TsEvent;
import com.tech4d.tsm.model.geometry.TimeRange;
import com.tech4d.tsm.util.ContextUtil;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;
import com.vividsolutions.jts.util.GeometricShapeFactory;

public class IntegrationTestEventSearchService {
    private static EventSearchService eventSearchService;

    private static TsEventDao tsEventDao;

    private static TsEventUtil tsEventUtil;
    private static Polygon searchBox = makeBoundingRectangle(300, 300);
    private static TimeRange searchTimeRange = new TimeRange(makeDate(2005, 2, 22), makeDate(2007, 2, 22));
    private static String[] searchStrings = { "description problem", "description", "small hand" };
    private static Polygon failBox = makeBoundingRectangle(3000, 3000);
    private static TimeRange failTimeRange = new TimeRange(makeDate(1005, 2, 22), makeDate(1007, 2, 22));
    private static String[] failStrings = { "the a is", "is", "sdfgsdfg" };
    private static Geometry insideTheBox;
    private static Geometry outsideTheBox;
    private static String[] shouldMatch = { "this is a small description of the problem at hand",
            "description of this is a small the problem at hand" };
    private static String shouldNotMatch = "there is nothing to match here";
    private static TimeRange insideTimeRange = new TimeRange(makeDate(2006, 2, 22), makeDate(2006, 9, 22));
    private static TimeRange halfwayOutsideTimeRange = new TimeRange(makeDate(2000, 2, 22), makeDate(2007, 2,
            22));
    private static TimeRange outsideTimeRange = new TimeRange(makeDate(2000, 2, 22), makeDate(2002, 2, 22));
    private static TsEvent actual;

    // search parameters
    private static int MAX_RESULTS = 3;

    @BeforeClass
    public static void setUpClass() throws ParseException {
        ApplicationContext appCtx = ContextUtil.getCtx();
        eventSearchService = (EventSearchService) appCtx.getBean("eventSearchService");
        tsEventDao = (TsEventDao) appCtx.getBean("tsEventDao");
        tsEventUtil = new TsEventUtil(eventSearchService.getSessionFactory());
        tsEventDao.deleteAll();
        StyleUtil.setupStyle();

        insideTheBox = new WKTReader().read("POINT (330 330)");
        outsideTheBox = new WKTReader().read("POINT (130 130)");

        // sample data
        // these two pass with all parameters
        makeSearchTsEvent(insideTheBox, insideTimeRange, shouldMatch[0]);
        actual = makeSearchTsEvent(insideTheBox, insideTimeRange, shouldMatch[1]);
        // the rest have at least one thing out of bounds
        makeSearchTsEvent(outsideTheBox, insideTimeRange, shouldMatch[0]);
        makeSearchTsEvent(insideTheBox, outsideTimeRange, shouldMatch[0]);
        makeSearchTsEvent(insideTheBox, halfwayOutsideTimeRange, shouldMatch[0]);
        makeSearchTsEvent(insideTheBox, insideTimeRange, shouldNotMatch);
        makeSearchTsEvent(outsideTheBox, insideTimeRange, shouldNotMatch);
        makeSearchTsEvent(outsideTheBox, outsideTimeRange, shouldNotMatch);
}

    /**
     * 
     * @throws ParseException
     *             When parsing
     */
    @Test
    public void testFindWithinGeometry() throws ParseException {

        // search bounding box
        Polygon rect = makeBoundingRectangle(800, 800);

        // inside the box
        String insideWKT = "POINT (830 830)";
        TsEvent eventInsideTheBox = tsEventUtil.createTsEvent(new WKTReader().read(insideWKT),
                new TimeRange(tsEventUtil.getBegin(), tsEventUtil.getEnd()));
        tsEventDao.save(eventInsideTheBox);

        // outside of the box
        TsEvent tsEvent = tsEventUtil.createTsEvent(new WKTReader().read("POINT (130 130)"),
                new TimeRange(tsEventUtil.getBegin(), tsEventUtil.getEnd()));
        tsEventDao.save(tsEvent);

        List<TsEvent> tsEvents = eventSearchService.findWithinGeometry(rect);
        assertEquals(1, tsEvents.size());
        TsEvent returned = tsEvents.get(0);

        assertEquals(insideWKT, (returned.getTsGeometry()).getGeometry().toText());

        // check the rest of the object
        tsEventUtil.assertEquivalent(tsEvent, returned);
    }

    @Test
    public void testSearchReturnsSome() throws ParseException {

        for (String searchString : searchStrings) {
            List<TsEvent> tsEvents = eventSearchService.search(MAX_RESULTS, searchString,
                    searchTimeRange, searchBox);
            assertEquals("only one should match", 2, tsEvents.size());
            TsEvent returned = tsEvents.get(0);
            assertEquals(insideTheBox.toText(), (returned.getTsGeometry()).getGeometry().toText());
            //it should be one of the description strings
            boolean failDescriptionMatch = true;
            for (String descr : shouldMatch) {
                if (descr.equals(returned.getDescription())) {
                    failDescriptionMatch = false;
                }
            }
            if (failDescriptionMatch) {
                fail("descriptions didn't match");
            }
            assertEquals(insideTimeRange.getBegin(), ((TimeRange) (returned.getWhen()))
                    .getBegin());
            // Make sure we can get everything
            tsEventUtil.assertEquivalent(actual, tsEvents.get(0));
        }
    }
    
    @Test public void noneInBox() {
        // now search in a bounding box that is out
        assertEquals("none should match", 0, eventSearchService.search(MAX_RESULTS,
                searchStrings[0], searchTimeRange, failBox).size());
    }

    @Test public void noTextMach() {
        // now search strings that are don't count words
        for (String failString : failStrings) {
            assertEquals("none should match", 0, eventSearchService.search(MAX_RESULTS, failString,
                    searchTimeRange, searchBox).size());
        }
    }
    
    @Test public void noTimeRangeMatch() {
        // now search timeframes that are out
        assertEquals("none should match", 0, eventSearchService.search(MAX_RESULTS,
                searchStrings[0], failTimeRange, searchBox).size());
    }
    
    @Test public void checkMaxReturn() {
        // now set the max return threshold to below the number of possible results
        assertEquals("only one should match", 1, eventSearchService.search(1,
                searchStrings[0], searchTimeRange, searchBox).size());
    }
    
    @Test public void checkNullSearchText() {
        assertEquals("three should match", 3, eventSearchService.search(MAX_RESULTS,
                null, searchTimeRange, searchBox).size());
    }

    @Test public void checkCount() {
        assertEquals("two should match", 2, eventSearchService.search(MAX_RESULTS,
                searchStrings[0], searchTimeRange, searchBox).size());
        assertEquals("two should match", 2L, (long) eventSearchService.getCount(
                searchStrings[0], searchTimeRange, searchBox));
    }

    private static Polygon makeBoundingRectangle(int x, int y) {
        GeometricShapeFactory gsf = new GeometricShapeFactory();
        gsf.setSize(100);
        gsf.setNumPoints(4);
        gsf.setBase(new Coordinate(x, y)); // pretend these are lat/longs
        return gsf.createRectangle();
    }

    private static TsEvent makeSearchTsEvent(Geometry geometry, TimeRange timeRange, String description) {
        TsEvent tsEvent = tsEventUtil.createTsEvent(geometry, timeRange, description);
        tsEventDao.save(tsEvent);
        return tsEvent;
    }

    private static Date makeDate(int year, int month, int day) {
        return new GregorianCalendar(year, month, day).getTime();
    }

}
