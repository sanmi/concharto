package com.tech4d.tsm.web;

import com.tech4d.tsm.model.TsEvent;
import com.tech4d.tsm.model.geometry.TimeRange;
import com.tech4d.tsm.service.EventSearchService;
import com.tech4d.tsm.util.JSONFormat;
import com.tech4d.tsm.web.util.PointPropertyEditor;
import com.tech4d.tsm.web.util.TimeRangePropertyEditor;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.util.GeometricShapeFactory;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractFormController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

public class EventSearchController extends AbstractFormController {
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
        binder.registerCustomEditor(Point.class, new PointPropertyEditor());
        super.initBinder(request, binder);
    }


    private Polygon getBoundingBox(EventSearchForm se) {
        GeometricShapeFactory gsf = new GeometricShapeFactory();
        gsf.setNumPoints(4);
        Point base = se.getBoundingBoxSW();
        Double height = se.getBoundingBoxNE().getY() - se.getBoundingBoxSW().getY();
        Double width = se.getBoundingBoxNE().getX() - se.getBoundingBoxSW().getX();
        gsf.setBase(new Coordinate(base.getX(), base.getY()));
        gsf.setWidth(width);
        gsf.setHeight(height);
        return gsf.createRectangle();
    }


    @SuppressWarnings("unchecked")
    @Override
    protected ModelAndView processFormSubmission(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
        /*
         * 1. Geocode "where" and get the lat-long bounding box of whatever zoom
         * level we are at. 2. Parse the time field to extract a time range 3.
         * Do a searcg to find the count of all events within that text filter,
         * time range and bounding box
         */
        EventSearchForm eventSearchForm = (EventSearchForm) command;
        if (eventSearchForm.getWhat() != null) {
            System.out.println("------ what: " + eventSearchForm.getWhat());
        }
        

        Map model = errors.getModel();
        //TODO set max results from somewhere?
        if (eventSearchForm.getMapCenter() != null) {
            List<TsEvent> events = eventSearchService.search(10, eventSearchForm.getWhat(), eventSearchForm.getWhen(),
                    getBoundingBox(eventSearchForm));

            model.put(MODEL_EVENTS, events);
            //NOTE: we are putting the events into the command so that the page javascript
            //functions can properly display them using google's mapping API
            eventSearchForm.setSearchResults(JSONFormat.toJSON(events));
        }
        
        if (errors.hasErrors()) {
            if (logger.isDebugEnabled()) {
                logger.debug("Data binding errors: " + errors.getErrorCount());
            }
            return showForm(request, response, errors);
        }
        else {
            logger.debug("No errors -> processing submit");
            return new ModelAndView(getSuccessView(), model);
        }
    }

    @Override
    protected ModelAndView showForm(
            HttpServletRequest request, HttpServletResponse response, BindException errors)
            throws Exception {

        return showForm(request, errors, getFormView());
    }
    
}
