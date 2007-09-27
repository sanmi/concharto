package com.tech4d.tsm.web;

import java.util.List;

import com.tech4d.tsm.model.TsEvent;
import com.tech4d.tsm.model.geometry.TimeRange;
import com.tech4d.tsm.service.EventSearchService;
import com.tech4d.tsm.web.util.PointPropertyEditor;
import com.tech4d.tsm.web.util.TimeRangePropertyEditor;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.util.GeometricShapeFactory;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.servlet.mvc.SimpleFormController;

import javax.servlet.http.HttpServletRequest;

public class EventSearchController extends SimpleFormController {
    private EventSearchService eventSearchService;

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

    @Override
    protected void doSubmitAction(Object command) throws Exception {

        /*
         * 1. Geocode "where" and get the lat-long bounding box of whatever zoom
         * level we are at. 2. Parse the time field to extract a time range 3.
         * Do a searcg to find the count of all events within that text filter,
         * time range and bounding box
         */
        EventSearchForm eventSearchForm = (EventSearchForm) command;
        if (eventSearchForm.getWhen() != null) {
            System.out.println("------ begin: " + eventSearchForm.getWhen().getBegin());
        }
        if (eventSearchForm.getBoundingBoxNE() != null) {
            System.out.println("------ boundingNE: " + eventSearchForm.getBoundingBoxNE().toText());
        }
        if (eventSearchForm.getMapCenter() != null) {
            System.out.println("------ center: " + eventSearchForm.getMapCenter().toText());
        }

        //TODO set max results from somewhere?
        List<TsEvent> events = eventSearchService.search(10, eventSearchForm.getWhat(), eventSearchForm.getWhen(),
                getBoundingBox(eventSearchForm));
        
        System.out.println("----- found " + events.size() + " events ");
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
        Polygon polygon =  gsf.createRectangle();
        System.out.println("--- " + polygon);
        return gsf.createRectangle();
    }

}
