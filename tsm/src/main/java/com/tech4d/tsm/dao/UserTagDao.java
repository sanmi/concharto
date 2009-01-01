package com.tech4d.tsm.dao;

import java.util.List;

import com.tech4d.tsm.model.UserTag;
import com.tech4d.tsm.model.time.TimeRange;

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
