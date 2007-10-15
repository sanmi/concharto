package com.tech4d.tsm.web;

import com.tech4d.tsm.model.TsEvent;
import com.tech4d.tsm.model.geometry.TimeRange;
import com.tech4d.tsm.service.EventSearchService;
import com.tech4d.tsm.util.JSONFormat;
import com.tech4d.tsm.web.util.GeometryPropertyEditor;
import com.tech4d.tsm.web.util.TimeRangePropertyEditor;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.util.GeometricShapeFactory;
import org.springframework.beans.propertyeditors.CustomBooleanEditor;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractFormController;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

public class EventSearchController extends AbstractFormController {
    public static final String SESSION_EVENT_SEARCH_FORM = "eventSearchForm";
    
    private static final int MAX_RECORDS = 26;
    private static final double LONGITUDE_180 = 180d;
    private static final String MODEL_EVENTS = "events";
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
        return (EventSearchForm) WebUtils.getSessionAttribute(request, EventSearchController.SESSION_EVENT_SEARCH_FORM);
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
     * Calculate the bounding box, given the north east and south west coordinates of 
     * a box.  Take into account boxes that span the dateline where longitude changes
     * from 180 to -180
     * 
     * TODO put the longitude logic in a utility class
     * @return Set<Geometry> a POLYGON object representing the bounding box
     * @param se EventSearchForm
     */
    private Set<Geometry> getBoundingBox(EventSearchForm se) {
        //longitudes have to contend with the international date line where it switches from -180 to +180
        //so we mod 180.  We assume the bounding box is less than 360 degrees.  If you want to figure
        //this out, you might want to draw it on paper
        Set<Geometry> polygons = new HashSet<Geometry>();
        Point base = se.getBoundingBoxSW();
        Double height = se.getBoundingBoxNE().getY() - se.getBoundingBoxSW().getY();
        Double east = se.getBoundingBoxNE().getX();
        Double west = se.getBoundingBoxSW().getX();
        if (east < west) {
            //ok this box spans the date line.  We need two bounding boxes.
            Double westWidth = LONGITUDE_180 + east;
            Double eastWidth = LONGITUDE_180 - west;
            
            Geometry westmost = makeRectangle(height, westWidth,  -LONGITUDE_180, base.getY()); 
            Geometry eastmost = makeRectangle(height, eastWidth,  west, base.getY()); 

            polygons.add(westmost);
            polygons.add(eastmost);
        } else {
            polygons.add(makeRectangle(height, east-west,  base.getX(), base.getY()));
        }
        return polygons;
    }


    private Geometry makeRectangle(Double height, Double width, Double eastMost, Double southMost) {
        GeometricShapeFactory gsf = new GeometricShapeFactory();
        gsf.setNumPoints(4);
        gsf.setBase(new Coordinate(eastMost, southMost));
        gsf.setHeight(height);
        gsf.setWidth(width);
        return gsf.createRectangle();
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
            Map model = doSearch(errors, eventSearchForm);
            //put the data into the session in case we are leaving
            WebUtils.setSessionAttribute(request, SESSION_EVENT_SEARCH_FORM, eventSearchForm);
            if (eventSearchForm.getIsEditEvent()) {
                if (eventSearchForm.getEventId() != null) {
                    return new ModelAndView(new RedirectView("event.htm?listid=" + eventSearchForm.getEventId()));
                } else {
                    //we are creating a new event
                    return new ModelAndView(new RedirectView("event.htm"));
                }
            } else {
                return new ModelAndView(getSuccessView(), model);
            }
        }
    }

    @SuppressWarnings("unchecked")
    private Map doSearch( BindException errors, EventSearchForm eventSearchForm) {
        /*
         * 1. Geocode "where" and get the lat-long bounding box of whatever zoom
         * level we are at. 2. Parse the time field to extract a time range 3.
         * Do a searcg to find the count of all events within that text filter,
         * time range and bounding box
         */
        Map model = errors.getModel();
        if (eventSearchForm.getMapCenter() != null) {
            List<TsEvent> events = new ArrayList<TsEvent>(); 
            Set<Geometry> boxes = getBoundingBox(eventSearchForm);  //there may be two 
            for (Geometry geometry : boxes) {
                logger.debug(geometry.toText());
                List results = eventSearchService.search(MAX_RECORDS, eventSearchForm.getWhat(), eventSearchForm.getWhen(), geometry);
                events.addAll(results);
            }

            model.put(MODEL_EVENTS, events);
            //NOTE: we are putting the events into the command so that the page javascript
            //functions can properly display them using google's mapping API
            eventSearchForm.setSearchResults(JSONFormat.toJSON(events));
        }
        return model;
    }


    @Override
    protected ModelAndView showForm(
            HttpServletRequest request, HttpServletResponse response, BindException errors)
            throws Exception {
        
        //if there is a form, we should redo the search, just in case things have been
        //added since we left (for instance the user just added a point to the map)
        //don't search if there are errors
        EventSearchForm eventSearchForm = getEventSearchForm(request);
        if ((eventSearchForm != null) && (!errors.hasErrors())){
            Map model = doSearch(errors, eventSearchForm);
            return new ModelAndView(getFormView(), model);
        } else {
            return showForm(request, errors, getFormView());
        }
    }
    

    
}
