package com.tech4d.tsm.dao;

import com.tech4d.tsm.model.TsEvent;
import com.tech4d.tsm.model.geometry.Style;
import com.tech4d.tsm.model.geometry.StyleSelector;
import com.tech4d.tsm.model.geometry.TimeRange;
import com.tech4d.tsm.util.ContextUtil;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;
import com.vividsolutions.jts.util.GeometricShapeFactory;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.SessionFactoryUtils;

import java.io.Serializable;
import java.util.*;

public class IntegrationTestTsEventDao {
    private static TsEventDao tsEventDao;

    private static boolean initialized;

    private Date begin;

    private Date end;

    @Before
    public void setUp() {
        Calendar cal = new GregorianCalendar(107 + 1900, 8, 22, 12, 22, 3);
        cal.set(Calendar.MILLISECOND, 750);
        begin = cal.getTime();
        cal.set(Calendar.SECOND, 35);
        end = cal.getTime();
    }

    @BeforeClass
    public static void setUpClass() {
        ApplicationContext appCtx = ContextUtil.getCtx();
        tsEventDao = (TsEventDao) appCtx.getBean("tsEventDao");

        if (!initialized) {
            initialized = true;
            tsEventDao.deleteAll();
            StyleUtil.setupStyle();
        }
    }

    /**
     * Runs the first time
     */
    @Test
    public void testInitFindAll() {
        tsEventDao.deleteAll();
        Collection<TsEvent> tsEvents = tsEventDao.findAll();
        assertEquals(0, tsEvents.size());
    }

    @Test
    public void testSaveAndFindById() throws ParseException {
        TsEvent tsEvent = TsEventUtil.createTsEvent(begin, end);
        Serializable id = tsEventDao.save(tsEvent);
        assertNotNull(id);
        TsEvent returned = tsEventDao.findById((Long) id);
        if (returned.getTsGeometry().getGeometry() instanceof Point) {
            Point point = (Point) returned.getTsGeometry().getGeometry();
            assertTrue((tsEvent.getTsGeometry()).getGeometry().equals(point));

        } else {
            fail("should have been a point");
        }

        // NOTE: MySQL doesn't store dates with millisecond precision, so we
        // need to strip out
        // the msec in order to compare
        Calendar cal = Calendar.getInstance();
        cal.setTime(begin);
        cal.set(Calendar.MILLISECOND, 0);

        Date correctedBegin = cal.getTime();
        Date returnedBegin = ((TimeRange) returned.getTimePrimitive()).getBegin();
        assertEquals(correctedBegin, returnedBegin);

        StyleSelector styleS = returned.getStyleSelector();
        Style style = (Style) styleS;
        assertEquals(style.getBaloonStyle().getBgColor(), style.getBaloonStyle().getBgColor());

        SessionFactory sessionFactory = tsEventDao.getSessionFactory();

        // We refresh the session to handle lazy loaded collections. This is
        // just
        // for testing we use OpenSessionInViewInterceptor for the real stuff
        Session session = SessionFactoryUtils.getSession(sessionFactory, true);
        session.refresh(tsEvent);
        assertEquals(2, tsEvent.getContributors().size());
        assertEquals(3, tsEvent.getUserTags().size());
        session.close();
    }

    public void findAll() throws ParseException {
        tsEventDao.save(TsEventUtil.createTsEvent());
        Collection<TsEvent> tsEvents = tsEventDao.findAll();
        assertTrue(tsEvents.size() >= 1);
    }

    @Test
    public void testSaveOrUpdate() {
    }

    @Test
    public void testDelete() throws ParseException {
        TsEvent tsEvent = TsEventUtil.createTsEvent();
        Serializable id = tsEventDao.save(tsEvent);
        tsEventDao.delete(tsEvent);
        assertNull(tsEventDao.findById((Long) id));
    }

    @Test
    public void testDeleteById() throws ParseException {
        Serializable id = tsEventDao.save(TsEventUtil.createTsEvent());
        tsEventDao.delete((Long) id);
        TsEvent tsEvent = tsEventDao.findById((Long) id);
        assertNull(tsEvent);
    }

    /**
     * 
     * @throws ParseException
     *             When parsing
     */
    @Test
    public void testFindWithinGeometry() throws ParseException {
        // search bounding box
        Polygon rect = makeBoundingRectangle(300, 300);

        // inside the box
        String insideWKT = "POINT (330 330)";
        TsEvent insideTheBox = TsEventUtil.createTsEvent(new WKTReader().read(insideWKT),
                new TimeRange(begin, end));
        tsEventDao.save(insideTheBox);

        // outside of the box
        tsEventDao.save(TsEventUtil.createTsEvent(new WKTReader().read("POINT (130 130)"),
                new TimeRange(begin, end)));

        List<TsEvent> tsEvents = tsEventDao.findWithinGeometry(rect);
        assertEquals(1, tsEvents.size());
        TsEvent returned = tsEvents.get(0);

        assertEquals(insideWKT, (returned.getTsGeometry()).getGeometry().toText());
    }

    @Test
    public void testSearch() throws ParseException {

        //search parameters
        Polygon searchBox = makeBoundingRectangle(300, 300);
        TimeRange searchTimeRange = new TimeRange(makeDate(2005,2,22), makeDate(2007,2,22));
        String[] searchStrings = {"description problem","description","small hand"};
        Polygon failBox = makeBoundingRectangle(3000, 3000);
        TimeRange failTimeRange = new TimeRange(makeDate(1005,2,22), makeDate(1007,2,22));
        String[] failStrings = {"the a is", "is", "sdfgsdfg"};
        
        //sample data
        Geometry insideTheBox = new WKTReader().read("POINT (330 330)");
        Geometry outsideTheBox = new WKTReader().read("POINT (130 130)");
        String shouldMatch = "this is a small description of the problem at hand";
        String shouldNotMatch = "there is nothing to match here";
        TimeRange insideTimeRange = new TimeRange(makeDate(2006,2,22), makeDate(2006,9,22));
        TimeRange halfwayOutsideTimeRange = new TimeRange(makeDate(2000,2,22), makeDate(2007,2,22));    
        TimeRange outsideTimeRange = new TimeRange(makeDate(2000,2,22), makeDate(2002,2,22));    
        //this one passes
        makeSearchTsEvent(insideTheBox, insideTimeRange, shouldMatch);
        //the rest fail
        makeSearchTsEvent(outsideTheBox, insideTimeRange, shouldMatch);
        makeSearchTsEvent(insideTheBox, outsideTimeRange, shouldMatch);
        makeSearchTsEvent(insideTheBox, halfwayOutsideTimeRange, shouldMatch);
        makeSearchTsEvent(insideTheBox, insideTimeRange, shouldNotMatch);
        makeSearchTsEvent(outsideTheBox, insideTimeRange, shouldNotMatch);
        makeSearchTsEvent(outsideTheBox, outsideTimeRange, shouldNotMatch);

        for (String searchString : searchStrings) {
            List<TsEvent> tsEvents = tsEventDao.search(searchString, searchTimeRange, searchBox);
            assertEquals("only one should match", 1, tsEvents.size());
            TsEvent returned = tsEvents.get(0);
            assertEquals(insideTheBox.toText(), (returned.getTsGeometry()).getGeometry().toText());
            assertEquals(shouldMatch, returned.getDescription());
            assertEquals(insideTimeRange.getBegin(), ((TimeRange) (returned.getTimePrimitive())).getBegin());
        }

        //now search in a bounding box that is out 
        assertEquals("none should match", 0, tsEventDao.search(searchStrings[0], searchTimeRange, failBox).size());            

        //now search strings that are don't count words 
        for (String failString : failStrings) {
            assertEquals("none should match", 0, tsEventDao.search(failString, searchTimeRange, searchBox).size());
        }

        //now search timeframes that are out 
        assertEquals("none should match", 0, tsEventDao.search(searchStrings[0], failTimeRange, searchBox).size());            

    }
    
    private Polygon makeBoundingRectangle(int x, int y) {
        GeometricShapeFactory gsf = new GeometricShapeFactory();
        gsf.setSize(100);
        gsf.setNumPoints(4);
        gsf.setBase(new Coordinate(x, y)); // pretend these are lat/longs
        return gsf.createRectangle();
    }

    private void makeSearchTsEvent(Geometry geometry, TimeRange timeRange, String description) {
        //TsEventUtil.createTsEvent(geometry, new TimeRange(begin, end), description);
        tsEventDao.save(TsEventUtil.createTsEvent(geometry, timeRange, description));
    }
    
    private Date makeDate(int year, int month, int day) {
        return new GregorianCalendar(year,month,day).getTime();
    }

}
