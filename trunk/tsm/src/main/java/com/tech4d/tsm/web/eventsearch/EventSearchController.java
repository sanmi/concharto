package com.tech4d.tsm.web.eventsearch;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.propertyeditors.CustomBooleanEditor;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractFormController;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.web.util.WebUtils;

import com.tech4d.tsm.auth.AuthConstants;
import com.tech4d.tsm.auth.AuthHelper;
import com.tech4d.tsm.geocode.GAddress;
import com.tech4d.tsm.geocode.GGcoder;
import com.tech4d.tsm.model.Event;
import com.tech4d.tsm.model.time.TimeRange;
import com.tech4d.tsm.service.EventSearchService;
import com.tech4d.tsm.service.Visibility;
import com.tech4d.tsm.util.JSONFormat;
import com.tech4d.tsm.util.LatLngBounds;
import com.tech4d.tsm.util.ProximityHelper;
import com.tech4d.tsm.util.TimeRangeFormat;
import com.tech4d.tsm.web.edit.EventController;
import com.tech4d.tsm.web.util.GeometryPropertyEditor;
import com.tech4d.tsm.web.util.PaginatingFormHelper;
import com.tech4d.tsm.web.util.TimeRangePropertyEditor;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;

public class EventSearchController extends AbstractFormController {
	private static final String QUERY_ZOOM = "_zoom";
	private static final String QUERY_WHAT = "_what";
	private static final String QUERY_WHEN = "_when";
	private static final String QUERY_WHERE = "_where";
	private static final String QUERY_FIT = "_fit";
	private static final Log log = LogFactory.getLog(EventSearchController.class);
	private static final String MODEL_TOTAL_EVENTS = "totalEvents";
	private static final String MODEL_IS_FIRST_VIEW = "isFirstView";
	public static final String SESSION_EVENT_SEARCH_FORM = "eventSearchForm";
    public static final String SESSION_FIRST_VIEW = "firstView";
    public static final String MODEL_EVENTS = "events";
    public static final String MODEL_TOTAL_RESULTS = "totalResults";
    public static final String MODEL_CURRENT_RECORD = "currRecord";
    public static final String MODEL_PAGESIZE = "pageSize";

    public static final int MAX_RECORDS = 25;
    private EventSearchService eventSearchService;
    private String formView;
    private String successView;
    
    //TODO move these into another class
    private static int ACCURACY_TO_ZOOM[] = {4, 5, 7, 9, 10, 12, 13, 14, 15};
    private static final double SEARCH_BOX_MIN = 40D; //approximate bounding box = miles * 1.4
    private static final double SEARCH_BOX_MAX = 2000D; //approximate bounding box = miles * 1.4
	private static final int ZOOM_BOX_THRESHOLD = 10;
	private static final int ZOOM_WORLD = 4;
    private static int NUM_ZOOM_LEVELS = 19;
    private static double[] SEARCH_BOX_DIMENTSIONS = new double[NUM_ZOOM_LEVELS];
    private static Point USA;
    
    static {
    	//the low zoom levels have variable search boxes
    	int i=0;
    	for ( ; i<ZOOM_BOX_THRESHOLD; i++) { 
    		SEARCH_BOX_DIMENTSIONS[i] = SEARCH_BOX_MAX - i*(SEARCH_BOX_MAX/ZOOM_BOX_THRESHOLD) + SEARCH_BOX_MIN;
    	}
    	//anything over threshold has the same search box 
    	for ( ; i<NUM_ZOOM_LEVELS; i++) {
    		SEARCH_BOX_DIMENTSIONS[i] = SEARCH_BOX_MIN;
    	}
    	GeometryFactory gf = new GeometryFactory();
    	USA = gf.createPoint(new Coordinate(-96.667916, 37.013086));
    }
    	
    
    public String getFormView() {
        return formView;
    }                              

    public void setFormView(String formView) {
        this.formView = formView;
    }

    public String getSuccessView() {
        return successView;
    }

    public void setSuccessView(String successView) {
        this.successView = successView;
    }

    public EventSearchService getEventSearchService() {
        return eventSearchService;
    }

    public void setEventSearchService(EventSearchService eventSearchService) {
        this.eventSearchService = eventSearchService;
    }

	@Override
    protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder)
            throws Exception {
        binder.registerCustomEditor(TimeRange.class, new TimeRangePropertyEditor());
        binder.registerCustomEditor(Geometry.class, new GeometryPropertyEditor());
        binder.registerCustomEditor(Boolean.class, new CustomBooleanEditor("true", "false", true));
        super.initBinder(request, binder);
    }

    private EventSearchForm getEventSearchForm(HttpServletRequest request) {
        return (EventSearchForm) WebUtils.getSessionAttribute(request, SESSION_EVENT_SEARCH_FORM);
    }

    @Override
    protected Object formBackingObject(HttpServletRequest request) throws Exception {
        //check to see if there is a form object in the session, if so it
        //is there for us to use right now (e.g. we are supposed to remember where
        //we were when we left here
        EventSearchForm eventSearchForm = getEventSearchForm(request);
        if (eventSearchForm != null) {
            return eventSearchForm;
        } else {
            return super.formBackingObject(request);
        }
    }
    /**
     * Override the default setting.  If there are query parameters in the URL, we assume
     * this is a form submission (e.g. when=, where=, what=)
     */
    @Override
	protected boolean isFormSubmission(HttpServletRequest request) {
        return !StringUtils.isEmpty(request.getQueryString()) || super.isFormSubmission(request);
    }

	@SuppressWarnings("unchecked")
    @Override
    protected ModelAndView processFormSubmission(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
		//TODO DEBUG don't do anything if the user isn't logged in.  Remove this kludge after we go live.  It is because
		//of redirects causing havoc with our LoginFilter - I can't figure out a better way at the moment
		//fsm 11-13-07
		if (null == WebUtils.getSessionAttribute(request, AuthConstants.SESSION_AUTH_USERNAME)) {
			return showForm(request, response, errors);
		}
		
        EventSearchForm eventSearchForm = (EventSearchForm) command;
        logSearchQuery(eventSearchForm);
        ModelAndView returnModelAndView;
        if (errors.hasErrors()) {
            if (logger.isDebugEnabled()) {
                logger.debug("Data binding errors: " + errors.getErrorCount());
            }
            //clear out the search results
            eventSearchForm.setSearchResults(null);
            returnModelAndView = showForm(request, response, errors);
        } else if (!StringUtils.isEmpty(request.getQueryString())) {
        	//if this is a form submission via query params, we will have to geocode first 
        	//then do a redirect
            eventSearchForm = new EventSearchForm();
        	returnModelAndView = handleGet(request, eventSearchForm);
        } else {
            logger.debug("No errors -> processing submit");
            Map model = doSearch(request, errors, eventSearchForm);
            if ((null != eventSearchForm.getIsEditEvent()) && eventSearchForm.getIsEditEvent()) {
            	eventSearchForm.setIsEditEvent(false); //turn this flag back off
                if (eventSearchForm.getEventId() != null) {
                	returnModelAndView = new ModelAndView(new RedirectView(request.getContextPath() + "/edit/event.htm?listid=" + eventSearchForm.getEventId()));
                } else {
                    //we are creating a new event
                	returnModelAndView = new ModelAndView(new RedirectView(request.getContextPath() + "/edit/event.htm"));
                }
            } else {
            	returnModelAndView = new ModelAndView(getSuccessView(), model);
            }
        }
        //put the data into the session in case we are leaving to edit, and then want to come back
        WebUtils.setSessionAttribute(request, SESSION_EVENT_SEARCH_FORM, eventSearchForm);
        return returnModelAndView;
    }

	private void logSearchQuery(EventSearchForm eventSearchForm) {
		if (log.isInfoEnabled()) {
        	StringBuffer msg = new StringBuffer("search,");
        	msg.append("where,\"").append(eventSearchForm.getWhere());
        	msg.append("\",when,");
        	if (eventSearchForm.getWhen() != null) {
        		msg.append(eventSearchForm.getWhen().getAsText());
        	}
        	msg.append("\",what,\"").append(eventSearchForm.getWhat()).append("\"");
        	log.info(msg);
        }
	}
	
    /**
     * They have put query string data on the request, so we ignore all else
     * @param request servlet request
     * @return ModelAndView a new ModelAndView
     * @throws Exception exception
     */
    private ModelAndView handleGet(HttpServletRequest request, EventSearchForm eventSearchForm) throws Exception {
    	//this request contains enough information to do a search right now
    	//use the same binder to get params of the query string as we were using for the POST
        //populate the form with parameters off the URL query string
    	bindGetParameters(request, eventSearchForm);
    	
    	//geocode
    	geocode(request, eventSearchForm);
    	
    	//save the form for redirect
    	WebUtils.setSessionAttribute(request, SESSION_EVENT_SEARCH_FORM, eventSearchForm);
    	return new ModelAndView(new RedirectView(request.getContextPath() + "/search/eventsearch.htm"));
    }

    /**
     * Populate the form with params from the URL query string
     * @param request servlet request
     * @param eventSearchForm the form to populate
     * @throws ServletRequestBindingException e
     */
	private void bindGetParameters(HttpServletRequest request, EventSearchForm eventSearchForm)
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
    	if ((zoom != null) && (zoom >0) && (zoom < NUM_ZOOM_LEVELS)) {
        	eventSearchForm.setMapZoom(zoom);
    	}
    	eventSearchForm.setIsFitViewToResults(ServletRequestUtils.getBooleanParameter(request, QUERY_FIT));
	}

    /**
     * Geocode and update the eventSearchForm accordingly
     * @param request servlet request
     * @param eventSearchForm form to be updated
     * @throws IOException e
     */
	private void geocode(HttpServletRequest request,
			EventSearchForm eventSearchForm) throws IOException {
		if (!StringUtils.isEmpty(eventSearchForm.getWhere())) {
        	String mapKey = getMessageSourceAccessor().getMessage(makeMapKeyCode(request));
        	GAddress address = GGcoder.geocode(eventSearchForm.getWhere(), mapKey);
        	Point point = address.getPoint();
        	if (null == eventSearchForm.getMapZoom()) {
        		//if they didn't supply the zoom level, we will get it from the address accuracy
            	if (address.getAccuracy() < ACCURACY_TO_ZOOM.length) {
            		eventSearchForm.setMapZoom(ACCURACY_TO_ZOOM[address.getAccuracy()]);
            	} else {
            		eventSearchForm.setMapZoom(ZOOM_WORLD);  
            	}
        	}
        	eventSearchForm.setMapCenter(point);
    	} else {
    		eventSearchForm.setMapZoom(ZOOM_WORLD);  
        	eventSearchForm.setMapCenter(USA);
    	}
	}
  

    @SuppressWarnings("unchecked")
    private Map doSearch( HttpServletRequest request, BindException errors, EventSearchForm eventSearchForm) {
        /*
         * 1. Get the lat-long bounding box of whatever zoom
         * level we are at. 2. Parse the time field to extract a time range 3.
         * Do a search to find the count of all events within that text filter,
         * time range and bounding box
         * TODO cache the total results if nothing has changed (e.g. pagination)
         */
        Map model = errors.getModel();
        if (eventSearchForm.getMapCenter() != null) {
            int firstRecord = PaginatingFormHelper.calculateFirstRecord(eventSearchForm, MAX_RECORDS);
            eventSearchForm.setCurrentRecord(firstRecord);
            //if we are below a certain zoom level, we will still search a wider area
            LatLngBounds bounds = null;
            if (eventSearchForm.getMapZoom() >= ZOOM_BOX_THRESHOLD) {
            	if (null != eventSearchForm.getMapCenter()) {
                	bounds = ProximityHelper.getBounds(
                			SEARCH_BOX_DIMENTSIONS[eventSearchForm.getMapZoom()], 
                			eventSearchForm.getMapCenter());  
            	}
            } else {
            	if ((null != eventSearchForm.getBoundingBoxSW()) && (null != eventSearchForm.getBoundingBoxSW())) {
                	bounds = new LatLngBounds(eventSearchForm.getBoundingBoxSW(), 
                			eventSearchForm.getBoundingBoxNE());
            	}
            }
            List<Event> events = eventSearchService.search(MAX_RECORDS, firstRecord, 
            		eventSearchForm.getWhat(), eventSearchForm.getWhen(), bounds, getVisibility(eventSearchForm));
            Long totalResults = eventSearchService.getCount(
            		eventSearchForm.getWhat(), eventSearchForm.getWhen(), bounds, getVisibility(eventSearchForm));

            //for debugging
            //addDebugBoundingBox(events, bounds);
            
            //if we just added a new event, it can be very confusing for the user to have the icon
            //disappear because it isn't high in the search results.  This is a kludge to help them
            //but it is only partially successful because the next time they hit 'search', their icon may disappear.  
            //I guess they will just have to get used to it?
            //So we will add the new event to the search results this one time
            //TODO - this may still be confusing to the user
            Event event = (Event) WebUtils.getSessionAttribute(request, EventController.SESSION_EVENT);
            if (event != null) {
            	//erase it for next time
            	WebUtils.setSessionAttribute(request, EventController.SESSION_EVENT, null);
            	boolean alreadyInList = false;
            	for (Event listEvent : events) {
            		if (event.getId().equals(listEvent.getId())) {
            			alreadyInList = true;
            		}
            	}
            	if (!alreadyInList) {
                	events.add(event);           		
            	}
            }
            model.put(MODEL_TOTAL_RESULTS, totalResults);
            model.put(MODEL_EVENTS, events);
            //NOTE: we are putting the events into the command so that the page javascript
            //functions can properly display them using google's mapping API
            eventSearchForm.setSearchResults(JSONFormat.toJSON(events));
        }
        return model;
    }

	/* useful for debugging */
/*    private void addDebugBoundingBox(List<Event> events, LatLngBounds bounds) {
    	Set<Geometry> boxes = ProximityHelper.getBoundingBoxes(bounds.getSouthWest(), bounds.getNorthEast());
    	for (Geometry box : boxes) {
        	Event event = new Event();
        	event.setSummary("box");
        	event.setTsGeometry(new TsGeometry(box));
        	try {
    			event.setWhen(TimeRangeFormat.parse("2000"));
    		} catch (ParseException e) {
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
*/
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
    
    @SuppressWarnings("unchecked")
	@Override
    protected ModelAndView showForm(
            HttpServletRequest request, HttpServletResponse response, BindException errors)
            throws Exception {

        //if there is a form, we should redo the search, just in case things have been
        //added since we left (for instance the user just added a point to the map)
        //don't search if there are errors
        EventSearchForm eventSearchForm = getEventSearchForm(request);
        if ((eventSearchForm != null) && (!errors.hasErrors())){
            Map model = doSearch(request, errors, eventSearchForm);
            return new ModelAndView(getFormView(), model);
        } else {
        	//we go through these IS_FIRST_VIEW hoops because we only want to show the "welcome" 
        	//message once per login, but this bit of code may get executed once before the 
        	//screen is actually shown if they are going to the search URL and then the user 
        	//is redirected because they need to log in.
        	String username = (String) WebUtils.getSessionAttribute(request, AuthConstants.SESSION_AUTH_USERNAME);
        	Object isFirstView = WebUtils.getSessionAttribute(request, SESSION_FIRST_VIEW);
            if ((username != null) && (isFirstView == null)) {
            	WebUtils.setSessionAttribute(request, SESSION_FIRST_VIEW, true);
            	Map model = errors.getModel();
            	model.put(MODEL_IS_FIRST_VIEW, true);
            	model.put(MODEL_TOTAL_EVENTS, eventSearchService.getTotalCount());
            	return new ModelAndView(getFormView(), model);
            }
            return showForm(request, errors, getFormView());
        }
    }

	private String makeMapKeyCode(HttpServletRequest request) {
		//map.${pageContext.request.serverName}.${pageContext.request.serverPort}.key
		return new StringBuffer("map.")
		.append(request.getServerName())
		.append('.')
		.append(request.getServerPort())
		.append('.')
		.append("key").toString();
	}
    
}
