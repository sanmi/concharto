package com.tech4d.tsm.web.eventsearch;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.propertyeditors.CustomBooleanEditor;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractFormController;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.web.util.WebUtils;

import com.tech4d.tsm.auth.AuthConstants;
import com.tech4d.tsm.auth.AuthHelper;
import com.tech4d.tsm.model.Event;
import com.tech4d.tsm.model.time.TimeRange;
import com.tech4d.tsm.service.EventSearchService;
import com.tech4d.tsm.service.Visibility;
import com.tech4d.tsm.util.JSONFormat;
import com.tech4d.tsm.util.ProximityHelper;
import com.tech4d.tsm.web.edit.EventController;
import com.tech4d.tsm.web.util.GeometryPropertyEditor;
import com.tech4d.tsm.web.util.PaginatingFormHelper;
import com.tech4d.tsm.web.util.TimeRangePropertyEditor;
import com.vividsolutions.jts.geom.Geometry;

public class EventSearchController extends AbstractFormController {
    private static final double SEARCH_BOX_DIMENTSION = 40D; //approximate bounding box = miles * 1.4
	private static final int ZOOM_BOX_THRESHOLD = 10;
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
    
    @SuppressWarnings("unchecked")
    @Override
    protected ModelAndView processFormSubmission(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
        EventSearchForm eventSearchForm = (EventSearchForm) command;
        
        if (errors.hasErrors()) {
            if (logger.isDebugEnabled()) {
                logger.debug("Data binding errors: " + errors.getErrorCount());
            }
            //clear out the search results
            eventSearchForm.setSearchResults(null);
            return showForm(request, response, errors);
        }
        else {
            logger.debug("No errors -> processing submit");
            Map model = doSearch(request, errors, eventSearchForm);
            //put the data into the session in case we are leaving to edit, and then want to come back
            WebUtils.setSessionAttribute(request, SESSION_EVENT_SEARCH_FORM, eventSearchForm);
            if (eventSearchForm.getIsEditEvent()) {
                if (eventSearchForm.getEventId() != null) {
                    return new ModelAndView(new RedirectView(request.getContextPath() + "/edit/event.htm?listid=" + eventSearchForm.getEventId()));
                } else {
                    //we are creating a new event
                    return new ModelAndView(new RedirectView(request.getContextPath() + "/edit/event.htm"));
                }
            } else {
                return new ModelAndView(getSuccessView(), model);
            }
        }
    }

    @SuppressWarnings("unchecked")
    private Map doSearch( HttpServletRequest request, BindException errors, EventSearchForm eventSearchForm) {
        /*
         * 1. Geocode "where" and get the lat-long bounding box of whatever zoom
         * level we are at. 2. Parse the time field to extract a time range 3.
         * Do a searcg to find the count of all events within that text filter,
         * time range and bounding box
         * TODO cache the total results if nothing has changed (e.g. pagination)
         */
        Map model = errors.getModel();
        if (eventSearchForm.getMapCenter() != null) {
            int firstRecord = PaginatingFormHelper.calculateFirstRecord(eventSearchForm, MAX_RECORDS);
            eventSearchForm.setCurrentRecord(firstRecord);
            List<Event> events = new ArrayList<Event>();
            Set<Geometry> boxes;
            if (eventSearchForm.getMapZoom() >= ZOOM_BOX_THRESHOLD) {
            	boxes = ProximityHelper.getBoundingBox(SEARCH_BOX_DIMENTSION, eventSearchForm.getMapCenter());  //there may be two
            } else {
            	boxes = ProximityHelper.getBoundingBox(eventSearchForm);  //there may be two
            }
            Long totalResults = 0L;
            //There are 1 or 2 bounding boxes (see comment above)
            for (Geometry geometry : boxes) {
                logger.debug(geometry.toText());
                List results = eventSearchService.search(MAX_RECORDS, firstRecord, 
                		eventSearchForm.getWhat(), eventSearchForm.getWhen(), geometry, getVisibility(eventSearchForm));
                events.addAll(results);
                //--------TODO DEBUG for testing remove this !!!
                //addDebugBoundingBox(events, geometry);
                //--------
                //if there are MAX_RECORDS, then there are probably more records, so get the count
                if (results.size() >= MAX_RECORDS) {
                    totalResults += eventSearchService.getCount(eventSearchForm.getWhat(), 
                    		eventSearchForm.getWhen(), geometry, getVisibility(eventSearchForm));
                } else {
                    totalResults += results.size();
                }
            }

            //if there were two boxes, one on either side of the international date line, we
            //may have twice as many records as we need so we have to sort and then strip off
            //the excess
            sortByDate(events);
            removeExcess(events, MAX_RECORDS);

            //if we just added a new event, it can be very confusing for the user to have the icon
            //dissappear because it isn't high in the search results.  This is a kludge to help them
            //but it is only partially successful because if the hit search, their icon may disappear.  
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

	private void removeExcess(List<Event> events, int maxRecords) {
		int originalSize = events.size();
		for (int i=0; i<originalSize; i++) {
			if (i >= maxRecords) {
				events.remove(events.size()-1);
			}
		}
		
	}

	@SuppressWarnings("unchecked")
	private void sortByDate(List<Event> events) {
		Collections.sort(events, new Comparator() {
			public int compare(Object arg0, Object arg1) {
				Event event0 = (Event) arg0;
				Event event1 = (Event) arg1;
				return(event0.getWhen().getBegin().compareTo(event1.getWhen().getBegin()));
			}
		      });
		
	}

	/* useful for debugging */
/*    private void addDebugBoundingBox(List<Event> events, Geometry geometry) { 
    	Event event = new Event();
    	event.setSummary("box");
    	event.setTsGeometry(new TsGeometry(geometry));
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
    
}
