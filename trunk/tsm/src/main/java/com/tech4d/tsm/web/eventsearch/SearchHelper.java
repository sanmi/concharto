package com.tech4d.tsm.web.eventsearch;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.propertyeditors.CustomBooleanEditor;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.util.WebUtils;

import com.tech4d.tsm.auth.AuthHelper;
import com.tech4d.tsm.geocode.GAddress;
import com.tech4d.tsm.geocode.GGcoder;
import com.tech4d.tsm.model.Event;
import com.tech4d.tsm.model.geometry.TsGeometry;
import com.tech4d.tsm.model.time.TimeRange;
import com.tech4d.tsm.service.EventSearchService;
import com.tech4d.tsm.service.Visibility;
import com.tech4d.tsm.util.JSONFormat;
import com.tech4d.tsm.util.LatLngBounds;
import com.tech4d.tsm.util.ProximityHelper;
import com.tech4d.tsm.util.SensibleMapDefaults;
import com.tech4d.tsm.util.TimeRangeFormat;
import com.tech4d.tsm.web.util.DisplayTagHelper;
import com.tech4d.tsm.web.util.GeometryPropertyEditor;
import com.tech4d.tsm.web.util.TimeRangePropertyEditor;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Point;

/**
 * Utility class used by search web controllers (e.g EventSearch, EmbeddedMap) 
 * @author frank
 *
 */
public class SearchHelper {
	public static final String QUERY_ZOOM = "_zoom";
	public static final String QUERY_WHAT = "_what";
	public static final String QUERY_WHEN = "_when";
	public static final String QUERY_WHERE = "_where";
	public static final String QUERY_ID = "_id";
	public static final String QUERY_FIT = "_fit";
    public static final String MODEL_EVENTS = "events";
    public static final String MODEL_TOTAL_RESULTS = "totalResults";
    public static final String MODEL_CURRENT_RECORD = "currentRecord";
    public static final String SESSION_DO_SEARCH_ON_SHOW = "doSearch";
	public static final String SESSION_EVENT_SEARCH_FORM = "eventSearchForm";
	public static final String SESSION_EVENT_SEARCH_RESULTS = "searchResults";
    public static final String DISPLAYTAG_TABLE_ID = "event";
    public static final int DISPLAYTAG_PAGESIZE = 25;

	private EventSearchService eventSearchService;

	
    public SearchHelper(EventSearchService eventSearchService) {
    	this.eventSearchService = eventSearchService;
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
    	}
    	eventSearchForm.setIsFitViewToResults(ServletRequestUtils.getBooleanParameter(request, QUERY_FIT));
    	eventSearchForm.setDisplayEventId(ServletRequestUtils.getLongParameter(request, QUERY_ID));
    	WebUtils.setSessionAttribute(request, SESSION_DO_SEARCH_ON_SHOW, true);
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
        	if (null == eventSearchForm.getMapZoom()) {
        		//if they didn't supply the zoom level, we will get it from the address accuracy
            	if (address.getAccuracy() < SensibleMapDefaults.ACCURACY_TO_ZOOM.length) {
            		eventSearchForm.setMapZoom(SensibleMapDefaults.ACCURACY_TO_ZOOM[address.getAccuracy()]);
            	} else {
            		eventSearchForm.setMapZoom(SensibleMapDefaults.ZOOM_COUNTRY);  
            	}
        	}
        	eventSearchForm.setMapCenter(point);
    	} else {
    		eventSearchForm.setMapZoom(SensibleMapDefaults.ZOOM_COUNTRY);  
        	eventSearchForm.setMapCenter(SensibleMapDefaults.USA);
    	}
	}
  

	/**
	 * Search for events and return results in the model.  Some search results are also put in the form
	 * so that the javascript functions may use them.
	 * 
	 * @param request request
	 * @param model model containing search results
	 * @param eventSearchForm form containing search parameters and search results    
	 * @return a list of Event objects
	 */
    @SuppressWarnings("unchecked")
    public List<Event> doSearch( HttpServletRequest request, Map model, EventSearchForm eventSearchForm) {
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
    		eventSearchForm.setMapZoom(event.getZoomLevel());
    		eventSearchForm.setMapType(event.getMapType());
    		totalResults = 1L;
    		firstRecord = 0;
    		//now remove the id from the form - we don't want to get stuck forever showing this event
    		eventSearchForm.setDisplayEventId(null);
    		
    	} else if (eventSearchForm.getMapCenter() != null) {
            //int firstRecord = PaginatingFormHelper.calculateFirstRecord(eventSearchForm, MAX_RECORDS);
            firstRecord = DisplayTagHelper.getFirstRecord(request, DISPLAYTAG_TABLE_ID, DISPLAYTAG_PAGESIZE);
            LatLngBounds bounds = getBounds(eventSearchForm);
            events = eventSearchService.search(DISPLAYTAG_PAGESIZE, firstRecord, 
            		eventSearchForm.getWhat(), eventSearchForm.getWhen(), bounds, getVisibility(eventSearchForm));
            totalResults = eventSearchService.getCount(
            		eventSearchForm.getWhat(), eventSearchForm.getWhen(), bounds, getVisibility(eventSearchForm));

    	} else {
    		events = new ArrayList<Event>();
    		totalResults = 0L;
    		firstRecord = 0;
    	}
        //for debugging
        //addDebugBoundingBox(events, bounds);
        
    	prepareModel( model, events, totalResults, firstRecord);
        //NOTE: we are putting the events into the command so that the page javascript
        //functions can properly display them using google's mapping API
        eventSearchForm.setSearchResults(JSONFormat.toJSON(events));
        return events;
    }

	@SuppressWarnings("unchecked")
	public void prepareModel(Map model, List<Event> events, Long totalResults, Integer currentRecord) {		
		model.put(MODEL_CURRENT_RECORD, currentRecord);
		model.put(MODEL_TOTAL_RESULTS, Math.round(totalResults));
    	model.put(MODEL_EVENTS, events);
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
				((eventSearchForm.getIsFitViewToResults() != null) && (eventSearchForm.getIsFitViewToResults()))) {
			//when they specify fit view to all results, we don't want a bounding box, unless
			//they also specify a place, in which case the geocode takes precedence
			return null;
		}
		if ((eventSearchForm.getMapZoom() >= SensibleMapDefaults.ZOOM_BOX_THRESHOLD) ||
				((null == eventSearchForm.getBoundingBoxSW()) && (null == eventSearchForm.getBoundingBoxSW())))	{
			if (null != eventSearchForm.getMapCenter()) {
		    	bounds = ProximityHelper.getBounds(
		    			SensibleMapDefaults.SEARCH_BOX_DIMENTSIONS[eventSearchForm.getMapZoom()], 
		    			eventSearchForm.getMapCenter());  
			}
		} else {
		    bounds = new LatLngBounds(eventSearchForm.getBoundingBoxSW(), eventSearchForm.getBoundingBoxNE());
		}
		return bounds;
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
