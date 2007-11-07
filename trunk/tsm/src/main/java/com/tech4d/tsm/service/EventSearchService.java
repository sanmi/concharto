package com.tech4d.tsm.service;

import com.tech4d.tsm.model.Event;
import com.tech4d.tsm.model.time.TimeRange;
import com.vividsolutions.jts.geom.Geometry;
import org.hibernate.SessionFactory;

import java.util.List;

public interface EventSearchService {

    public void setSessionFactory(SessionFactory sessionFactory);

    public SessionFactory getSessionFactory();

    /**
     * Search for events.  
     * @param maxResults Max results to return
     * @param firstResult Start results at this record (for paging)
     * @param textFilter Free text search to use.  Ignored if value is null
     * @param timeRange Time range to use Ignored if value is null
     * @param boundingBox Ignored if value is null
     * @param showVisible If 'true', search will return only events with visible=true.  If 'false' 
     * the search will return only events with visible=false.
     * @return list of Event objects
     */
    public List<Event> search(int maxResults, int firstResult, String textFilter, TimeRange timeRange,
            Geometry boundingBox, Visibility showVisible);

    /**
     * Get count of total records matching the search criteria.  Used for displaying search results.
     * @param textFilter Free text search to use.  Ignored if value is null
     * @param timeRange Time range to use Ignored if value is null
     * @param boundingBox Ignored if value is null
     * @param showVisible If 'true', search will return only events with visible=true.  If 'false' 
     * the search will return only events with visible=false.
     * @return count of records matching the search criteria
     */
    public Long getCount(String textFilter, TimeRange timeRange, Geometry boundingBox, Visibility showVisible);
    
    /**
     * Get total count of all events in the DB.  TODO later we want to make
     * this a periodic query that caches the results so it is only run once 
     * every hour or so.
     * @return
     */
    public Integer getTotalCount();

}