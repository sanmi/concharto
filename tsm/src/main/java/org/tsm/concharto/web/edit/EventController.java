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
package org.tsm.concharto.web.edit;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.BooleanUtils;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;
import org.springframework.web.util.WebUtils;
import org.tsm.concharto.dao.EventDao;
import org.tsm.concharto.model.Event;
import org.tsm.concharto.model.PositionalAccuracy;
import org.tsm.concharto.model.time.TimeRange;
import org.tsm.concharto.util.GeometryType;
import org.tsm.concharto.util.JSONFormat;
import org.tsm.concharto.util.SensibleMapDefaults;
import org.tsm.concharto.web.eventsearch.EventSearchForm;
import org.tsm.concharto.web.eventsearch.SearchHelper;
import org.tsm.concharto.web.util.CatalogUtil;
import org.tsm.concharto.web.util.GeometryPropertyEditor;
import org.tsm.concharto.web.util.TimeRangePropertyEditor;

import com.vividsolutions.jts.geom.Geometry;

public class EventController extends SimpleFormController {
	private static final String MODEL_POSITIONAL_ACCURACIES = "positionalAccuracies";
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
        	//TODO this is getting called twice - once on showForm and once on processFormSubmission
            event = this.eventDao.findById(id);
            //TODO don't use session if possible!!!
            //save it in the session for later modification
            WebUtils.setSessionAttribute(request, SESSION_EVENT, event);
            eventForm = EventFormFactory.getEventForm(event, request);
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
        if (ServletRequestUtils.getStringParameter(request,"add") != null) {
        	eventForm.setAddEvent(true);
        }
        return eventForm;
    }

    
    @SuppressWarnings("unchecked")
	@Override
	protected ModelAndView showForm(HttpServletRequest request,
			HttpServletResponse response, BindException errors)
			throws Exception {
    	Map model = errors.getModel();
		addAccuracies(model);
		return new ModelAndView(getFormView(), model);
	}

	@SuppressWarnings("unchecked")
	private void addAccuracies(Map model) {
        model.put(MODEL_POSITIONAL_ACCURACIES, eventDao.getPositionalAccuracies());
	}

	@SuppressWarnings("unchecked")
	@Override
	protected ModelAndView processFormSubmission(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors)
			throws Exception {
        EventForm eventForm = (EventForm)command;

    	Map model = errors.getModel();
        if (BooleanUtils.isTrue(eventForm.getShowPreview())) {
        	Event event = EventFormFactory.createEvent(eventForm);
        	EventFormFactory.renderWiki(event, request);
        	//for processing the drop down selector for positional accuracies
        	addAccuracies(model);
        	if (null != event.getPositionalAccuracy()) {
            	event.setPositionalAccuracy(
            			(PositionalAccuracy) eventDao.getPositionalAccuracy(event.getPositionalAccuracy().getId()));
        	}
        	eventForm.setPreviewEvent(JSONFormat.toJSON(event));
            return new ModelAndView(getFormView(), model);
        } else if (errors.hasErrors()) {
        	addAccuracies(model);
        	return new ModelAndView(getFormView(), model);
        } else {
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
                event = EventFormFactory.updateEvent(event, eventForm);
            } else {
                event = EventFormFactory.createEvent(eventForm);
            }
            //get the catalog from the URL
            event.setCatalog(CatalogUtil.getCatalog(request));
            this.eventDao.saveOrUpdate(event);
            eventSearchForm.setDisplayEventId(event.getId()); //we want to show only the event we've just edited  
            WebUtils.setSessionAttribute(request, SESSION_EVENT, event);
            WebUtils.setSessionAttribute(request, SearchHelper.SESSION_EVENT_SEARCH_FORM, eventSearchForm);
        	WebUtils.setSessionAttribute(request, SearchHelper.SESSION_DO_SEARCH_ON_SHOW, true);
            return new ModelAndView(getSuccessView());        	
        }
	}

}
