/*******************************************************************************
 * Copyright 2009 Time Space Map, LLC
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package org.tsm.concharto.service;

import java.util.List;

import org.hibernate.SessionFactory;
import org.tsm.concharto.model.Event;
import org.tsm.concharto.util.LatLngBounds;


public interface EventSearchService {

    public void setSessionFactory(SessionFactory sessionFactory);

    public SessionFactory getSessionFactory();


    /**
     * Search for events.  Handles international dateline boundary
     * by separating the bounding box into two rectangles. 
     * @param maxResults Max results to return
     * @param firstResult Start results at this record (for paging)
     * @param bounds south west and north east corner of the bounding box ignored if null
     * @param params search parameters
     * @return list of Event objects
     *
     * @see org.tsm.concharto.service.SearchParams
     */
    public List<Event> search(int maxResults, int firstResult, LatLngBounds bounds, SearchParams params);
	
    /**
     * Get count of total records matching the search criteria.  Handles international dateline boundary
     * by separating the bounding box into two rectangles.  Used for displaying search results.
     * @param bounds south west and north east corner of the bounding box ignored if null
     * @param params search parameters
     * @return count of records matching the search criteria
     *
     * @see org.tsm.concharto.service.SearchParams
     */
    public Long getCount(LatLngBounds bounds, SearchParams params);
    
    /**
     * Get total count of all events in the DB.  TODO later we want to make
     * this a periodic query that caches the results so it is only run once 
     * every hour or so.
     * @return total count
     */
    public Integer getTotalCount();

    /**
     * Find an event based on its Id
     * @param id the id
     * @return Event an event
     */
    public Event findById(Long id);
}
