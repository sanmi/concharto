package com.tech4d.tsm.web;

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

public class SearchEventController extends SimpleFormController {
    private EventSearchService eventSearchService;

    public EventSearchService getEventSearchService() {
        return eventSearchService;
    }

    @Override
    protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder)
            throws Exception {
        binder.registerCustomEditor(TimeRange.class, new TimeRangePropertyEditor());
        binder.registerCustomEditor(Point.class, new PointPropertyEditor());
        super.initBinder(request, binder);
    }

    public void setEventSearchService(EventSearchService eventSearchService) {
        this.eventSearchService = eventSearchService;
    }

    @Override
    protected void doSubmitAction(Object command) throws Exception {

        /*
         * 1. Geocode "where" and get the lat-long bounding box of whatever zoom
         * level we are at. 2. Parse the time field to extract a time range 3.
         * Do a searcg to find the count of all events within that text filter,
         * time range and bounding box
         */
        SearchEventForm searchEventForm = (SearchEventForm) command;
        if (searchEventForm.getWhen() != null) {
            System.out.println("------ begin: " + searchEventForm.getWhen().getBegin());
        }
        if (searchEventForm.getBoundingBoxNE() != null) {
            System.out.println("------ boundingNE: " + searchEventForm.getBoundingBoxNE().toText());
        }
        if (searchEventForm.getMapCenter() != null) {
            System.out.println("------ center: " + searchEventForm.getMapCenter().toText());
        }

/*        List<TsEvent> events = eventSearchService.search(10, searchEventForm.getWhat(), searchEventForm.getWhen(),
                getBoundingBox(searchEventForm));
        
        System.out.println("----- found " + events.size() + " events ");
*/    }

    private Polygon getBoundingBox(SearchEventForm se) {
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

}
