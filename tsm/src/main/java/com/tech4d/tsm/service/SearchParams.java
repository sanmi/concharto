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

	public SearchParams() {
		super();
	}

    /**
     * Search parameters
     * @param textFilter Free text search to use.  Ignored if value is null
     * @param timeRange Time range to use Ignored if value is null
     * @param visibility enum that determines the visibility of search results
     * @param excludeTimeRangeOverlaps if this is true, then overlapping timeranges will be ignored
     */
    public SearchParams(String textFilter, TimeRange timeRange,
			Visibility visibility, boolean excludeTimeRangeOverlaps) {
		super();
		this.textFilter = textFilter;
		this.timeRange = timeRange;
		this.visibility = visibility;
		this.includeTimeRangeOverlaps = excludeTimeRangeOverlaps;
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
}
