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
package com.tech4d.tsm.service;

import java.util.List;

import org.hibernate.SessionFactory;

import com.tech4d.tsm.model.Event;
import com.tech4d.tsm.util.LatLngBounds;

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
     * @see com.tech4d.tsm.service.SearchParams
     */
    public List<Event> search(int maxResults, int firstResult, LatLngBounds bounds, SearchParams params);
	
    /**
     * Get count of total records matching the search criteria.  Handles international dateline boundary
     * by separating the bounding box into two rectangles.  Used for displaying search results.
     * @param bounds south west and north east corner of the bounding box ignored if null
     * @param params search parameters
     * @return count of records matching the search criteria
     *
     * @see com.tech4d.tsm.service.SearchParams
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
