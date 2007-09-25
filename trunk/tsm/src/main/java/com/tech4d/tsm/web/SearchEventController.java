package com.tech4d.tsm.web;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.servlet.mvc.SimpleFormController;

import com.tech4d.tsm.model.geometry.TimeRange;
import com.tech4d.tsm.service.EventSearchService;
import com.tech4d.tsm.web.util.TimeRangePropertyEditor;

public class SearchEventController extends SimpleFormController {
    private EventSearchService eventSearchService;

    public EventSearchService getEventSearchService() {
        return eventSearchService;
    }

    @Override
    protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception {
        binder.registerCustomEditor(TimeRange.class, new TimeRangePropertyEditor());
        super.initBinder(request, binder);
    }

    public void setEventSearchService(EventSearchService eventSearchService) {
        this.eventSearchService = eventSearchService;
    }

    @Override
    protected void doSubmitAction(Object command) throws Exception {

        /* 1. Geocode "where" and get the lat-long bounding box of whatever
              zoom level we are at.
           2. Parse the time field to extract a time range
           3. Do a searcg to find the count of all events within 
              that text filter, time range and bounding box
         */ 
        SearchEventForm searchEventForm = (SearchEventForm) command;
        System.out.println("------ begin: " + searchEventForm.getWhen().getBegin());
    }

}
