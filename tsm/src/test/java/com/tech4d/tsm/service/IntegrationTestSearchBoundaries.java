package com.tech4d.tsm.service;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;

import com.tech4d.tsm.dao.EventDao;
import com.tech4d.tsm.dao.EventTesterDao;
import com.tech4d.tsm.dao.EventUtil;
import com.tech4d.tsm.dao.StyleUtil;
import com.tech4d.tsm.model.Event;
import com.tech4d.tsm.model.time.TimeRange;
import com.tech4d.tsm.util.ContextUtil;
import com.tech4d.tsm.util.TimeRangeFormat;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;

public class IntegrationTestSearchBoundaries {
    private static EventSearchService eventSearchService;

    private static EventDao eventDao;
    private static EventTesterDao eventTesterDao;

    private static EventUtil eventUtil;

    private static Geometry insideTheBox;

    private int MAX_RESULTS = 100;
    
    @BeforeClass
    public static void setUpBigSearch() throws ParseException {
        ApplicationContext appCtx = ContextUtil.getCtx();
        eventSearchService = (EventSearchService) appCtx.getBean("eventSearchService");
        eventDao = (EventDao) appCtx.getBean("eventDao");
        eventTesterDao = (EventTesterDao) appCtx.getBean("eventTesterDao");
        eventUtil = new EventUtil(eventSearchService.getSessionFactory());
        eventTesterDao.deleteAll();
        StyleUtil.setupStyle();

        insideTheBox = new WKTReader().read("POINT (330 330)");
    }

    @Before
    public void setUp() throws java.text.ParseException {
        eventTesterDao.deleteAll();
        StyleUtil.setupStyle();
    }
    
    private void assertSearchMatch(int matchesExpected, String dateText) throws java.text.ParseException {
        List<Event> events = eventSearchService.search(MAX_RESULTS, 0, null,
                TimeRangeFormat.parse(dateText), null, Visibility.NORMAL);
        assertEquals(matchesExpected, events.size());        
    }
    
    @Test public void testBoundaries() throws java.text.ParseException {
        makeSearchEvent(insideTheBox, TimeRangeFormat.parse("1522-1527"), "Stuff", null);
        assertSearchMatch(0, "1528");
        assertSearchMatch(0, "1521");
        assertSearchMatch(0, "1528 - 1900");
        assertSearchMatch(0, "1519 - 1521");
        assertSearchMatch(1, "1522");
        assertSearchMatch(1, "1527");
        assertSearchMatch(1, "1522-1527");
        assertSearchMatch(1, "1526 - 1527");
        assertSearchMatch(1, "1522-1540");
        assertSearchMatch(1, "1520-1540");
    }

    @Test public void testInvisible() throws java.text.ParseException {
    	//first one is visible
        Event event = makeSearchEvent(insideTheBox, TimeRangeFormat.parse("1522-1527"), "Stuff", null);
        event.setVisible(true);
        eventDao.saveOrUpdate(event);
        //second one is invisible
        event = makeSearchEvent(insideTheBox, TimeRangeFormat.parse("1522-1527"), "Stuff", null);
        event.setVisible(false);
        eventDao.saveOrUpdate(event);
        //should only see one
        assertEquals(1, eventSearchService.search(MAX_RESULTS, 0, null, null, null, Visibility.NORMAL).size());        
        
        //now test showing only invisible
        assertEquals(1, eventSearchService.search(MAX_RESULTS, 0, null, null, null, Visibility.HIDDEN).size());        
        
    }
    
    
    private static Event makeSearchEvent(Geometry geometry, TimeRange timeRange, String summary, String description) {
        Event event = eventUtil.createEvent(geometry, timeRange, summary, description);
        eventDao.save(event);
        return event;
    }
    

}
