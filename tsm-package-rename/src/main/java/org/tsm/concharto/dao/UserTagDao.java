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
