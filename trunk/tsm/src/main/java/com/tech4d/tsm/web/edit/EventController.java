package com.tech4d.tsm.web.edit;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;
import org.springframework.web.util.WebUtils;

import com.tech4d.tsm.dao.EventDao;
import com.tech4d.tsm.model.Event;
import com.tech4d.tsm.model.time.TimeRange;
import com.tech4d.tsm.util.GeometryType;
import com.tech4d.tsm.util.SensibleMapDefaults;
import com.tech4d.tsm.web.eventsearch.EventSearchForm;
import com.tech4d.tsm.web.eventsearch.SearchHelper;
import com.tech4d.tsm.web.util.GeometryPropertyEditor;
import com.tech4d.tsm.web.util.TimeRangePropertyEditor;
import com.vividsolutions.jts.geom.Geometry;

public class EventController extends SimpleFormController {
    public static final String SESSION_EVENT = "EVENT";
	private static final String PARAM_ID = "id";
    EventDao eventDao;

    public void setEventDao(EventDao eventDao) {
        this.eventDao = eventDao;
    }
    
    @Override
    protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder)
            throws Exception {
        binder.registerCustomEditor(TimeRange.class, new TimeRangePropertyEditor());
        binder.registerCustomEditor(Geometry.class, new GeometryPropertyEditor());
        super.initBinder(request, binder);
    }
    
    private EventSearchForm getEventSearchForm(HttpServletRequest request) {
        return (EventSearchForm) WebUtils.getSessionAttribute(request, SearchHelper.SESSION_EVENT_SEARCH_FORM);
    }

    @Override
    protected Object formBackingObject(HttpServletRequest request) throws Exception {
        Long id = ServletRequestUtils.getLongParameter(request, PARAM_ID);
        Event event;
        EventForm eventForm;
        EventSearchForm eventSearchForm = getEventSearchForm(request);
        if (id != null) {
            //get the event
            event = this.eventDao.findById(id);
            //TODO don't use session if possible!!!
            //save it in the session for later modification
            WebUtils.setSessionAttribute(request, SESSION_EVENT, event);
            eventForm = com.tech4d.tsm.web.edit.EventFormFactory.getEventForm(event);
            if (eventSearchForm != null) {
                eventForm.setSearchResults(eventSearchForm.getSearchResults());
                eventForm.setMapCenter(eventSearchForm.getMapCenter());
            }
        } else {
            //this is a new form
            eventForm = new EventForm();
            if (eventSearchForm != null) {
                eventForm.setZoomLevel(eventSearchForm.getMapZoom());
                eventForm.setSearchResults(eventSearchForm.getSearchResults());
                //default geometry type is point
                eventForm.setGeometryType(GeometryType.POINT);
                eventForm.setMapCenter(eventSearchForm.getMapCenter());
                eventForm.setMapType(eventSearchForm.getMapType());
            } else {
                eventForm.setZoomLevel(SensibleMapDefaults.ZOOM_WORLD);
                //default geometry type is point
                eventForm.setGeometryType(GeometryType.POINT);
                eventForm.setMapCenter(SensibleMapDefaults.NORTH_ATLANTIC);
            }
        }
        return eventForm;
    }

    @Override
    protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
        EventForm eventForm = (EventForm)command;

        //change the map center of the search form to wherever we are now!    	
        EventSearchForm eventSearchForm = getEventSearchForm(request);
        if (eventSearchForm == null) {
        	eventSearchForm = new EventSearchForm();
        }
        //now create or update the event
        Event event;
        if (eventForm.getEventId() != null) {
            //get the event from the session
            event = (Event) WebUtils.getSessionAttribute(request, SESSION_EVENT);
            event = com.tech4d.tsm.web.edit.EventFormFactory.updateEvent(event, eventForm);
        } else {
            event = com.tech4d.tsm.web.edit.EventFormFactory.createEvent(eventForm);
        }
        this.eventDao.saveOrUpdate(event);
        eventSearchForm.setDisplayEventId(event.getId()); //we want to show only the event we've just edited  
        WebUtils.setSessionAttribute(request, SESSION_EVENT, event);
        WebUtils.setSessionAttribute(request, SearchHelper.SESSION_EVENT_SEARCH_FORM, eventSearchForm);
    	WebUtils.setSessionAttribute(request, SearchHelper.SESSION_DO_SEARCH_ON_SHOW, true);
        return new ModelAndView(getSuccessView());
    }

}
