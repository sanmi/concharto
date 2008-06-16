package com.tech4d.tsm.web.eventsearch;

import info.bliki.wiki.model.WikiModel;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.net.URLDecoder;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.propertyeditors.CustomBooleanEditor;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.util.WebUtils;

import com.tech4d.tsm.auth.AuthHelper;
import com.tech4d.tsm.dao.EventDao;
import com.tech4d.tsm.geocode.GAddress;
import com.tech4d.tsm.geocode.GGcoder;
import com.tech4d.tsm.model.Event;
import com.tech4d.tsm.model.geometry.TsGeometry;
import com.tech4d.tsm.model.time.TimeRange;
import com.tech4d.tsm.service.EventSearchService;
import com.tech4d.tsm.service.SearchParams;
import com.tech4d.tsm.service.Visibility;
import com.tech4d.tsm.util.JSONFormat;
import com.tech4d.tsm.util.LatLngBounds;
import com.tech4d.tsm.util.ProximityHelper;
import com.tech4d.tsm.util.SensibleMapDefaults;
import com.tech4d.tsm.util.TimeRangeFormat;
import com.tech4d.tsm.web.util.CatalogUtil;
import com.tech4d.tsm.web.util.DisplayTagHelper;
import com.tech4d.tsm.web.util.GeometryPropertyEditor;
import com.tech4d.tsm.web.util.TimeRangePropertyEditor;
import com.tech4d.tsm.web.wiki.WikiModelFactory;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;

/**
 * Utility class used by search web controllers (e.g EventSearch, EmbeddedMap) 
 * @author frank
 *
 */
public class SearchHelper {
	private static final String MODEL_TITLE = "title";
	public static final String QUERY_ZOOM = "_zoom";
	public static final String QUERY_WHAT = "_what";
	public static final String QUERY_WHEN = "_when";
	public static final String QUERY_WHERE = "_where";
	public static final String QUERY_MAPTYPE = "_maptype";
	public static final String QUERY_USERTAG = "_tag";
	public static final String QUERY_LAT_LNG = "_ll";
	public static final String QUERY_SW = "_sw";
	public static final String QUERY_NE = "_ne";
	public static final String QUERY_ID = "_id";
	public static final String QUERY_WITHIN_MAP_BOUNDS = "_withinMap";
	public static final String QUERY_EXCLUDE_TIMERANGE_OVERLAPS = "_timeoverlaps";
	public static final String QUERY_EMBED = "_embed";
	public static final String QUERY_KML = "_kml";
    public static final String MODEL_EVENTS = "events";
    public static final String MODEL_TOTAL_RESULTS = "totalResults";
    public static final String MODEL_CURRENT_RECORD = "currentRecord";
    public static final String SESSION_DO_SEARCH_ON_SHOW = "doSearch";
	public static final String SESSION_EVENT_SEARCH_FORM = "eventSearchForm";
	public static final String SESSION_EVENT_SEARCH_RESULTS = "searchResults";
    public static final String DISPLAYTAG_TABLE_ID = "event";
    public static final int DISPLAYTAG_PAGESIZE = 25;

	private static final Object MODEL_POSITIONAL_ACCURACIES = "positionalAccuracies";
	private static final Log log = LogFactory.getLog(SearchHelper.class);
	private EventSearchService eventSearchService;
	private EventDao eventDao;

    public void setEventSearchService(EventSearchService eventSearchService) {
		this.eventSearchService = eventSearchService;
	}

	public void setEventDao(EventDao eventDao) {
		this.eventDao = eventDao;
	}


	public void initBinder(HttpServletRequest request, ServletRequestDataBinder binder)
            throws Exception {
        binder.registerCustomEditor(TimeRange.class, new TimeRangePropertyEditor());
        binder.registerCustomEditor(Geometry.class, new GeometryPropertyEditor());
        binder.registerCustomEditor(Boolean.class, new CustomBooleanEditor("true", "false", true));
    }

    /**
     * Populate the form with params from the URL query string
     * @param request servlet request
     * @param eventSearchForm the form to populate
     * @throws ServletRequestBindingException e
     * @throws UnsupportedEncodingException 
     */
	public void bindGetParameters(HttpServletRequest request, EventSearchForm eventSearchForm)
			throws ServletRequestBindingException {
		eventSearchForm.setWhere(ServletRequestUtils.getStringParameter(request, QUERY_WHERE));
		String whenStr = ServletRequestUtils.getStringParameter(request, QUERY_WHEN);
    	try {
    		eventSearchForm.setWhen(TimeRangeFormat.parse(whenStr));
    	} catch (ParseException e) {
    		//no error handling for URL string searches  TODO add error handling
    	}
    	eventSearchForm.setWhat(ServletRequestUtils.getStringParameter(request, QUERY_WHAT));
    	Integer zoom = ServletRequestUtils.getIntParameter(request, QUERY_ZOOM);
    	//check for incorrect zoom
    	if ((zoom != null) && (zoom >0) && (zoom < SensibleMapDefaults.NUM_ZOOM_LEVELS)) {
        	eventSearchForm.setMapZoom(zoom);
        	eventSearchForm.setZoomOverride(true);
    	}
    	//TSM-257 problem with googlebot.  This is a kludge.  It is difficult to figure out
    	//why google isn't following the maptype
    	String mapType = request.getParameter(QUERY_MAPTYPE);
    	try {
        	eventSearchForm.setMapType(new Integer(mapType));
    	} catch (NumberFormatException e) {
    		eventSearchForm.setMapType(SensibleMapDefaults.DEFAULT_MAP_TYPE);
    	}
    	eventSearchForm.setUserTag(getUtf8QueryStringParameter(request, QUERY_USERTAG));
    	eventSearchForm.setLimitWithinMapBounds((ServletRequestUtils.getBooleanParameter(request, QUERY_WITHIN_MAP_BOUNDS)));
    	eventSearchForm.setExcludeTimeRangeOverlaps((ServletRequestUtils.getBooleanParameter(request, QUERY_EXCLUDE_TIMERANGE_OVERLAPS)));
    	eventSearchForm.setEmbed((ServletRequestUtils.getBooleanParameter(request, QUERY_EMBED)));
    	eventSearchForm.setKml((ServletRequestUtils.getBooleanParameter(request, QUERY_KML)));
    	Long eventId = ServletRequestUtils.getLongParameter(request, QUERY_ID);
    	if (null != eventId) {
        	eventSearchForm.setDisplayEventId(eventId);
    	}
    	Point ll = getLatLng(request, QUERY_LAT_LNG);
    	if (null != ll) {
        	eventSearchForm.setMapCenter(ll);
        	//tells the javascript client side code not to "fit" the map to the search results
    		eventSearchForm.setMapCenterOverride(true);
    	}
    	eventSearchForm.setBoundingBoxNE(getLatLng(request, QUERY_NE));
    	eventSearchForm.setBoundingBoxSW(getLatLng(request, QUERY_SW));
    	
    	WebUtils.setSessionAttribute(request, SESSION_DO_SEARCH_ON_SHOW, true);
	}
	

	/**
	 * TODO - There is a wierd bug with Get string character encoding that results in improper
	 * decoding of the query string - when you call request.getCharacterEncoding() it returns UTF-8,
	 * but the parameter is not decoded as UTF-8.  So we have to do it by hand here.
	 *  
	 * @param request
	 * @param paramName
	 */
	private String getUtf8QueryStringParameter(HttpServletRequest request, String paramName) {
		String queryString = request.getQueryString();
		String before = paramName+"=";
    	String tag = StringUtils.substringBetween(queryString, before, "&");
    	if (tag == null) {
    		tag = StringUtils.substringAfter(queryString, before);
    	} else {
	    	try {
				tag = URLDecoder.decode(tag, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				log.error("Couldn't decode query paramter " + tag, e);
			} 
    	}
		return tag;
	}
	
	private Point getLatLng(HttpServletRequest request, String query) throws ServletRequestBindingException {
		String ll = ServletRequestUtils.getStringParameter(request, query);
		String[] lls = StringUtils.split(ll, ',');
		if ((null != lls) && (lls.length == 2)) {
			try {
				Double lat_y = new Double(lls[0]);
				Double lng_x = new Double(lls[1]);
				GeometryFactory gf = new GeometryFactory();
		        return gf.createPoint(new Coordinate(lng_x, lat_y));
			} catch (NumberFormatException e) {
				//do nothing
			}			
		}
		return null;
	}

    /**
     * Geocode and update the eventSearchForm accordingly
     * @param mapKey google maps api key
     * @param request servlet request
     * @param eventSearchForm form to be updated
     * @throws IOException e
     */
	public void geocode(String mapKey, HttpServletRequest request,
			EventSearchForm eventSearchForm) throws IOException {

		if (!StringUtils.isEmpty(eventSearchForm.getWhere())) {
        	//String mapKey = formController.getMessageSourceAccessor().getMessage(makeMapKeyCode(request));
        	GAddress address = GGcoder.geocode(eventSearchForm.getWhere(), mapKey);
        	Point point = address.getPoint();
        	eventSearchForm.setMapCenter(point);
        	if (point == null) {
        		eventSearchForm.setIsGeocodeSuccess(false);
        	}
        	if (null == eventSearchForm.getMapZoom()) {
        		//if they didn't supply the zoom level, we will get it from the address accuracy
            	if (address.getAccuracy() < SensibleMapDefaults.ACCURACY_TO_ZOOM.length) {
            		eventSearchForm.setMapZoom(SensibleMapDefaults.ACCURACY_TO_ZOOM[address.getAccuracy()]);
            	} else {
            		eventSearchForm.setMapZoom(SensibleMapDefaults.ZOOM_COUNTRY);  
            	}
        	}
    	} else {
    		//no location was specified, we will use the default
    		if (eventSearchForm.getMapZoom() == null) {
        		eventSearchForm.setMapZoom(SensibleMapDefaults.ZOOM_WORLD);      			
    		}
    		if (eventSearchForm.getMapCenter() == null) {
    			eventSearchForm.setMapCenter(SensibleMapDefaults.NORTH_ATLANTIC);
    		}
    	}
	}
  

	/**
	 * Search for events and return results in the model.  Some search results are also put in the form
	 * so that the javascript functions may use them.
	 * 
     * @param mapKey google api key
	 * @param request request
	 * @param model model containing search results
	 * @param eventSearchForm form containing search parameters and search results    
	 * @return a list of Event objects
	 */
    @SuppressWarnings("unchecked")
    public List<Event> doSearch(String mapKey, HttpServletRequest request, Map model, EventSearchForm eventSearchForm) {
        /*
         * 1. Get the lat-long bounding box of whatever zoom
         * level we are at. 2. Parse the time field to extract a time range 3.
         * Do a search to find the count of all events within that text filter,
         * time range and bounding box
         * TODO cache the total results if nothing has changed (e.g. pagination)
         */
		List<Event> events; 
		Long totalResults;
		Integer firstRecord;
    	if (eventSearchForm.getDisplayEventId() != null) {
    		Event event = eventSearchService.findById(eventSearchForm.getDisplayEventId());
    		events = new ArrayList<Event>();
    		events.add(event);
			eventSearchForm.setMapCenter(event.getTsGeometry().getGeometry().getCentroid());
			//Don't override the zoom for polylines or polygons no matter what.  Otherwise, there is too
			//much opportunity for confusion.  For example, the centroid of the polyline may be nowhere 
			//near the border in which case you won't see the line at all.
			if (event.getTsGeometry().getGeometry() instanceof Point) {
				eventSearchForm.setMapCenterOverride(true);
	    		eventSearchForm.setZoomOverride(true);				
			} else {
				eventSearchForm.setMapCenterOverride(false);
	    		eventSearchForm.setZoomOverride(false);				
			}
    		eventSearchForm.setMapZoom(event.getZoomLevel());
    		eventSearchForm.setMapType(event.getMapType());
    		totalResults = 1L;
    		firstRecord = 0;
    		//now remove the id from the form - we don't want to get stuck forever showing this event
    		//the browser javascript still needs the event id so it can crate a linkHere url, so we make
    		//a copy of the id
    		eventSearchForm.setLinkHereEventId(eventSearchForm.getDisplayEventId());
    		eventSearchForm.setDisplayEventId(null);
    		
    	} else { 
    		eventSearchForm.setLinkHereEventId(null);
    		eventSearchForm.setDisplayEventId(null);
    		if (eventSearchForm.getMapCenter() == null) {
    	    	//geocode
    	    	try {
					geocode(mapKey, request, eventSearchForm);
				} catch (IOException e) {
					//TODO, perhaps some better error handling here??
					log.info("Exception geocoding location " + eventSearchForm.getWhere() + e);
				}
    		}
    		//we are ok to go if IsGeocodeSuccess is null or True
    		if (!BooleanUtils.isFalse(eventSearchForm.getIsGeocodeSuccess())) {
                firstRecord = DisplayTagHelper.getFirstRecord(request, DISPLAYTAG_TABLE_ID, DISPLAYTAG_PAGESIZE);
                LatLngBounds bounds = getBounds(eventSearchForm);
                SearchParams params = new SearchParams();
                params.setTextFilter(eventSearchForm.getWhat());
                params.setUserTag(eventSearchForm.getUserTag());
                params.setTimeRange(eventSearchForm.getWhen());
                params.setVisibility(getVisibility(eventSearchForm));
                params.setCatalog(CatalogUtil.getCatalog(request));
                //note these are opposites.. a value of null or false = false, true=true
                params.setIncludeTimeRangeOverlaps(!BooleanUtils.isTrue(eventSearchForm.getExcludeTimeRangeOverlaps()));
                events = eventSearchService.search(DISPLAYTAG_PAGESIZE, firstRecord, bounds, params);
                
                //for debugging
                //addDebugBoundingBox(events, bounds);

                totalResults = eventSearchService.getCount(bounds, params);
    		} else {
    			//failed geocode, no points
    			events = new ArrayList<Event>();
        		totalResults = 0L;
        		firstRecord = 0;
    		}    		

    	} 
        
    	prepareModel( model, events, totalResults, firstRecord);
        //NOTE: we are putting the events into the command so that the page javascript
        //functions can properly display them using google's mapping API
    	List<Event> renderedEvents = renderWiki(events, request);
        eventSearchForm.setSearchResults(JSONFormat.toJSON(renderedEvents));
        
        setupPageTitle(request, eventSearchForm, model, events);
        return events;
    }

    /**
     * Creates a string to be displayed in the browser title based on
     * search terms or event id
     * @param request
     * @param eventSearchForm
     * @param model
     * @param events
     */
	@SuppressWarnings("unchecked")
	private void setupPageTitle(HttpServletRequest request, 
				 	EventSearchForm eventSearchForm, Map model, List<Event> events) {
		// if the request contains an id= parameter, it means this is a one-at-a-time listing.
		// Both users and search engines will see this so we need to show the event summary in
		// the title
		String title;
		if (null != request.getParameter("_id")) {
			title = events.get(0).getSummary();
		} else {
			//summary of the search terms
			List<String> terms = new ArrayList<String>();
			addIfNotEmpty(terms, "where: ", eventSearchForm.getWhere());
			if (null != eventSearchForm.getWhen()) {
				addIfNotEmpty(terms, "when: ", eventSearchForm.getWhen().getAsText());
			}
			addIfNotEmpty(terms, "what: ", eventSearchForm.getWhat());
			addIfNotEmpty(terms, "tag: ", eventSearchForm.getUserTag());
			title = StringUtils.join(terms.toArray(), ", ");
		}
		model.put(MODEL_TITLE, title);
	}

	/**
	 * Utility for natural language processing - adding commas between a list of search terms
	 * @param list
	 * @param prefix
	 * @param str
	 */
	private void addIfNotEmpty(List<String> list, String prefix, String str) {
		if (!StringUtils.isEmpty(str)) {
			list.add(prefix + str);
		}
	}

	private List<Event> renderWiki(List<Event> events, HttpServletRequest request) {
		List<Event> renderedEvents = new ArrayList<Event>();
		WikiModel wikiModel = WikiModelFactory.newWikiModel(request);
		for (Event event : events) {
			try {
				Event renderedEvent = (Event) BeanUtils.cloneBean(event);
				renderedEvent.setDescription(wikiModel.render(event.getDescription()));
				renderedEvent.setSource(wikiModel.render(event.getSource()));
				renderedEvents.add(renderedEvent);
			} catch (IllegalAccessException e) {
				log.error(e);
			} catch (InstantiationException e) {
				log.error(e);
			} catch (InvocationTargetException e) {
				log.error(e);
			} catch (NoSuchMethodException e) {
				log.error(e);
			}
		}
		return renderedEvents;
	}

	@SuppressWarnings("unchecked")
	public void prepareModel(Map model, List<Event> events, Long totalResults, Integer currentRecord) {		
		model.put(MODEL_CURRENT_RECORD, currentRecord);
		model.put(MODEL_TOTAL_RESULTS, Math.round(totalResults));
    	model.put(MODEL_EVENTS, events);
		model.put(MODEL_POSITIONAL_ACCURACIES, eventDao.getPositionalAccuracies());
	}
    

    /**
     * Find the search bounding box.  It depends on the map center and zoom level.
     * @param eventSearchForm search form
     * @return LatLngBounds search bounding box
     */
	private LatLngBounds getBounds(EventSearchForm eventSearchForm) {
		//if we are below a certain zoom level, we will still search a wider area
		LatLngBounds bounds = null;
		if ((StringUtils.isEmpty(eventSearchForm.getWhere())) && 
				BooleanUtils.isFalse(eventSearchForm.getLimitWithinMapBounds())) {
			//when they specify fit view to all results, we don't want a bounding box, unless
			//they also specify a place, in which case the geocode takes precedence
			return null;
		}

		//if we already have bounds
		if (hasBounds(eventSearchForm)) {
			//if there are no bounds or the user explicitly said to use those bounds
			if ((!zoomedInTooLow(eventSearchForm) ||(BooleanUtils.isTrue(eventSearchForm.getLimitWithinMapBounds())))) {
				bounds = new LatLngBounds(eventSearchForm.getBoundingBoxSW(), eventSearchForm.getBoundingBoxNE());
			} 
			//when we are zoomed in real low we want to search more than just the visible map, instead
			//use the a wider search radius
			else  {
				bounds = searchBoxBounds(eventSearchForm);
			}
		} else if (null != eventSearchForm.getWhere()){
			//there is a location, but no bounds, so we have to make them
			bounds = searchBoxBounds(eventSearchForm);
		}
		return bounds;
	}
	
	private LatLngBounds searchBoxBounds(EventSearchForm eventSearchForm) {
    	return ProximityHelper.getBounds(
				SensibleMapDefaults.SEARCH_BOX_DIMENTSIONS[eventSearchForm.getMapZoom()], 
				eventSearchForm.getMapCenter());  			
	}

	private boolean zoomedInTooLow(EventSearchForm eventSearchForm) {
		return eventSearchForm.getMapZoom() >= SensibleMapDefaults.ZOOM_BOX_THRESHOLD;
	}
	private boolean hasBounds(EventSearchForm eventSearchForm) {
		return ((null != eventSearchForm.getBoundingBoxSW()) && (null != eventSearchForm.getBoundingBoxSW()));
	}


	/**
	 * Useful for debugging bounding box problems.
	 * @param events list of Event objects
	 * @param bounds bounding box
	 */
    public void addDebugBoundingBox(List<Event> events, LatLngBounds bounds) {
    	Set<Geometry> boxes = ProximityHelper.getBoundingBoxes(bounds.getSouthWest(), bounds.getNorthEast());
    	for (Geometry box : boxes) {
        	Event event = new Event();
        	event.setSummary("box");
        	event.setTsGeometry(new TsGeometry(box));
        	try {
    			event.setWhen(TimeRangeFormat.parse("2000"));
    		} catch (ParseException e) {
                //no action
            }
    		event.setId(2L);
    		event.setHasUnresolvedFlag(false);
    		event.setWhere("");
    		event.setDescription("");
    		event.setUserTagsAsString("");
    		event.setSource("");
    		events.add(event);
    	}
    }
    /** 
     * Only administrators are allowed to see removed events.  We need to check for proper authorization
     * because the user could have hacked the HTML to add the showInvisible parameter
     * @param eventSearchForm  EventSearchForm
     * @return true if we are supposed to show visible events.  false if we are supposed to show invisible events.
     */
    private Visibility getVisibility(EventSearchForm eventSearchForm) {
    	if (AuthHelper.isUserAnAdmin()) {
    		//TODO probably a better way than this
    		if (StringUtils.equals(EventSearchForm.SHOW_HIDDEN,eventSearchForm.getShow())) {
    			return Visibility.HIDDEN;
    		} else if (StringUtils.equals(EventSearchForm.SHOW_NORMAL,eventSearchForm.getShow())) {
    			return Visibility.NORMAL;
    		} else if (StringUtils.equals(EventSearchForm.SHOW_FLAGGED,eventSearchForm.getShow())) {
    			return Visibility.FLAGGED;
    		} 
    	}
    	return Visibility.NORMAL;
    }
    
    /**
     * Utility to get the google maps API key from a properties file
     * @param request used to find the url and port
     * @return google maps API key
     */
	public String makeMapKeyCode(HttpServletRequest request) {
		//map.${pageContext.request.serverName}.${pageContext.request.serverPort}.key
		return new StringBuffer("map.")
		.append(request.getServerName())
		.append('.')
		.append(request.getServerPort())
		.append('.')
		.append("key").toString();
	}

}
