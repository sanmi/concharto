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

import com.tech4d.tsm.model.time.TimeRange;

/**
 * Parameter object for use with EventSearchService
 * @author frank
 *
 */
public class SearchParams {
	private String textFilter;
	private TimeRange timeRange;
	private Visibility visibility;
	private boolean includeTimeRangeOverlaps;
	private String userTag;
	private String catalog;

	public SearchParams() {
		super();
	}

    /**
     * Search parameters
     * @param textFilter Free text search to use.  Ignored if value is null
     * @param timeRange Time range to use Ignored if value is null
     * @param visibility enum that determines the visibility of search results
     * @param excludeTimeRangeOverlaps if this is true, then overlapping timeranges will be ignored
     * @param userTag a single userTag to search
     */
    public SearchParams(String textFilter, TimeRange timeRange,
			Visibility visibility, boolean excludeTimeRangeOverlaps, String userTag, String catalog) {
		super();
		this.textFilter = textFilter;
		this.timeRange = timeRange;
		this.visibility = visibility;
		this.includeTimeRangeOverlaps = excludeTimeRangeOverlaps;
		this.userTag = userTag;
		this.catalog = catalog;
	}

	public String getTextFilter() {
		return textFilter;
	}
	public void setTextFilter(String textFilter) {
		this.textFilter = textFilter;
	}
	public TimeRange getTimeRange() {
		return timeRange;
	}
	public void setTimeRange(TimeRange timeRange) {
		this.timeRange = timeRange;
	}
	public Visibility getVisibility() {
		return visibility;
	}
	public void setVisibility(Visibility visibility) {
		this.visibility = visibility;
	}
	public boolean isIncludeTimeRangeOverlaps() {
		return includeTimeRangeOverlaps;
	}
	public void setIncludeTimeRangeOverlaps(boolean excludeTimeRangeOverlaps) {
		this.includeTimeRangeOverlaps = excludeTimeRangeOverlaps;
	}
	public String getUserTag() {
		return userTag;
	}
	public void setUserTag(String tag) {
		this.userTag = tag;
	}
	public String getCatalog() {
		return catalog;
	}
	public void setCatalog(String catalog) {
		this.catalog = catalog;
	}
	
}
