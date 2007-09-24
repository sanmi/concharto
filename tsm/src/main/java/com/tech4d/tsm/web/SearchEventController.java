package com.tech4d.tsm.web;

import org.springframework.web.servlet.mvc.SimpleFormController;

import com.tech4d.tsm.dao.TsEventDao;

public class SearchEventController extends SimpleFormController {
    private TsEventDao tsEventDao;

    public TsEventDao getTsEventDao() {
        return tsEventDao;
    }

    public void setTsEventDao(TsEventDao tsEventDao) {
        this.tsEventDao = tsEventDao;
    }

    @Override
    protected void doSubmitAction(Object command) throws Exception {

        /* 1. Geocode "where" and get the lat-long bounding box of whatever
              zoom level we are at.
           2. Parse the time field to extract a time range
           3. Do a DB spatial query to find the count of all events within 
              that time range and bounding box
           4. Do a lucene search on the remainder??    
         */ 
    }

}
