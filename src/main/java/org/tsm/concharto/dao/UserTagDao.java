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
package org.tsm.concharto.dao;

import java.util.List;

import org.tsm.concharto.model.UserTag;
import org.tsm.concharto.model.time.TimeRange;


/**
 * Dao for getting aggregate tag information
 *
 */
public interface UserTagDao extends BaseDao <UserTag>{
    /**
     * Gets most aggregate tag counts for tags created within the past 'daysBack' days
     * @param daysBack number days in the past to search
     * @return a map of occurrences of tags keyed by tag name ordered by tag name ascending
     */
    public List<Object[]> getTagCounts(int daysBack);
    
    /**
     * Gets aggregate tag counts for tags created within the given time range
     * @param timeRange time range to search
     * @return a map of occurrences of tags keyed by tag name ordered by tag name ascending
     */
    public List<Object[]> getTagCounts(TimeRange timeRange); 

    /**
     * Gets aggregate tag counts associated with events with begin date in the given time range
     * @param timeRange time range to search
     * @return a map of occurrences of tags keyed by tag name ordered by event begin date ascending
     */
    public List<Object[]> getTagCountsByEventBeginDate(TimeRange timeRange); 
}
