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
import com.tech4d.tsm.util.LatLngBounds;
import com.tech4d.tsm.util.TimeRangeFormat;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Point;
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
    
    private void assertSearchMatch(int overlapMatchesExpected, int nonOverlapMatchesExpected, String dateText) throws java.text.ParseException {
    	assertSearchMatch(overlapMatchesExpected, dateText, true);
    	assertSearchMatch(nonOverlapMatchesExpected, dateText, false);
    }
    private void assertSearchMatch(int matchesExpected, String dateText, boolean includeOverlaps) throws java.text.ParseException {
        List<Event> events = eventSearchService.search(MAX_RESULTS, 0, null, 
        		new SearchParams(null, TimeRangeFormat.parse(dateText), Visibility.NORMAL, includeOverlaps, null));
        assertEquals(matchesExpected, events.size());        
    }

    @Test public void testInternationalDateLine() throws ParseException, java.text.ParseException {
    	Geometry pearlHarbor = new WKTReader().read("POINT (-157.95 21.366667)");
    	Geometry taipei = new WKTReader().read("POINT (121.5028 25.0419)");
    	Geometry newYorkCity= new WKTReader().read("POINT ( -74 40.71)"); //way out the box
    	Geometry sanDiego= new WKTReader().read("POINT (  -117.15 32.78)");  //same lat as the box
    	makeSearchEvent(pearlHarbor, TimeRangeFormat.parse("1941"),"summary","description" );
    	makeSearchEvent(taipei, TimeRangeFormat.parse("1941"),"summary","description" );
    	makeSearchEvent(newYorkCity, TimeRangeFormat.parse("1941"),"summary","description" );
    	makeSearchEvent(sanDiego, TimeRangeFormat.parse("1941"),"summary","description" );
    	Point boundingBoxSW =  (Point) new WKTReader().read("POINT (120 20)");
    	Point boundingBoxNE =  (Point) new WKTReader().read("POINT (-156 34)");
    	LatLngBounds bounds = new LatLngBounds(boundingBoxSW, boundingBoxNE);
    	assertEquals(2, eventSearchService.search(10, 0, bounds, new SearchParams(null, null, null, true, null)).size());
    }
    
    @Test public void testTimeBoundaries() throws java.text.ParseException {
        makeSearchEvent(insideTheBox, TimeRangeFormat.parse("1522-1527"), "Stuff", null);
        assertSearchMatch(0, 0, "1528");
        assertSearchMatch(0, 0, "1521");
        assertSearchMatch(0, 0, "1528 - 1900");
        assertSearchMatch(0, 0, "1519 - 1521");
        assertSearchMatch(1, 0, "1522");
        assertSearchMatch(1, 0, "1527");
        assertSearchMatch(1, 1, "1522-1527");
        assertSearchMatch(1, 0, "1526 - 1527");
        assertSearchMatch(1, 1, "1522-1540");
        assertSearchMatch(1, 1, "1520-1540");
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
        assertEquals(1, eventSearchService.search(MAX_RESULTS, 0, null, new SearchParams(null, null, Visibility.NORMAL, true, null)).size());        
        
        //now test showing only invisible
        assertEquals(1, eventSearchService.search(MAX_RESULTS, 0, null, new SearchParams(null, null, Visibility.HIDDEN, true, null)).size());        
        
    }
    
    @Test public void testFlagged() throws java.text.ParseException {
    	//first one is unflagged
        Event event = makeSearchEvent(insideTheBox, TimeRangeFormat.parse("1522-1527"), "Stuff", null);
        //second one is flagged
        event = makeSearchEvent(insideTheBox, TimeRangeFormat.parse("1522-1527"), "Stuff", null);
        event.setHasUnresolvedFlag(true);
        eventDao.saveOrUpdate(event);
        //should see both
        assertEquals(2, eventSearchService.search(MAX_RESULTS, 0, null, new SearchParams( null, null, Visibility.NORMAL, true, null)).size());        
        
        //now test showing only invisible
        assertEquals(1, eventSearchService.search(MAX_RESULTS, 0, null, new SearchParams(null, null, Visibility.FLAGGED, true, null)).size());        
    	
    }
    
    private static Event makeSearchEvent(Geometry geometry, TimeRange timeRange, String summary, String description) {
        Event event = eventUtil.createEvent(geometry, timeRange, summary, description);
        eventDao.save(event);
        return event;
    }

    
    @Test
    public void testGetCount() throws ParseException, java.text.ParseException {
        makeSearchEvent(insideTheBox, TimeRangeFormat.parse("1522-1527"), "Stuff", null);
        makeSearchEvent(insideTheBox, TimeRangeFormat.parse("1522-1527"), "Stuff", null);
        assertEquals((Integer)2, eventSearchService.getTotalCount());
        makeSearchEvent(insideTheBox, TimeRangeFormat.parse("1522-1527"), "Stuff", null);
        assertEquals((Integer)3, eventSearchService.getTotalCount());
    	
    }
    
    @Test 
    public void testUserTagSearch() throws ParseException, java.text.ParseException {
    	//first one has our tag
        String tag1 = "san_francisco";
        String tag2 = "sdf";
		makeTaggedEvent(tag1 + ", a, b, c");
		makeTaggedEvent(tag1 + ", d, e, f");
		makeTaggedEvent(tag2 + ", h, i");
        checkUserTagSearch(2, tag1);
        checkUserTagSearch(2, "San_Francisco");
        checkUserTagSearch(2, " San_Francisco ");
        checkUserTagSearch(0, "San Francisco"); //missing underbar
        checkUserTagSearch(0, "San");  //three letters are excluded
        checkUserTagSearch(2, "SAN_Francisco ");
        checkUserTagSearch(1, tag2);
        checkUserTagSearch(0, "3453fsdf");
        checkUserTagSearch(3, null);
        assertEquals(3, eventSearchService.search(MAX_RESULTS, 0, null, 
        		new SearchParams( null, null, Visibility.NORMAL, true, null)).size());        
    }
    
    @Test
    public void testSortOrder() throws java.text.ParseException  {
        String summary1 = "Stuff 1";
        String summary2 = "Stuff 2";
        String summary3 = "Stuff 3";
        Event e2 = makeSearchEvent(insideTheBox, TimeRangeFormat.parse("1522"), summary3, null);
		Event e0 = makeSearchEvent(insideTheBox, TimeRangeFormat.parse("1522"), summary1, null);
        Event e1 = makeSearchEvent(insideTheBox, TimeRangeFormat.parse("1522"), summary2, null);
		Event e3 = makeSearchEvent(insideTheBox, TimeRangeFormat.parse("1529"), summary3, null);
		List<Event> events = eventSearchService.search(MAX_RESULTS, 0, null, new SearchParams( null, null, Visibility.NORMAL, true, null));
		assertEquals(e0.getId(), events.get(0).getId());
		assertEquals(e1.getId(), events.get(1).getId());
		assertEquals(e2.getId(), events.get(2).getId());
		assertEquals(e3.getId(), events.get(3).getId());
    }
    
    private void checkUserTagSearch(int count, String tag) {
        assertEquals(count, eventSearchService.search(MAX_RESULTS, 0, null, 
        		new SearchParams( null, null, Visibility.NORMAL, true, tag)).size());        
	}

	private Event makeTaggedEvent(String tags) throws ParseException, java.text.ParseException {
        Event event = makeSearchEvent(insideTheBox, TimeRangeFormat.parse("1522-1527"), "tag summary", "tag description");
        
        event.setUserTagsAsString(tags);
        eventDao.saveOrUpdate(event);
        return event;
    }

}
