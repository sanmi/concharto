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
package org.tsm.concharto.web.eventsearch;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.tsm.concharto.model.time.TimeRange;

import com.vividsolutions.jts.geom.Point;

/**
 * Form data object for searching for events
 * 
 * A number of these objects are hidden input values for use by
 * javascript functions
 */
public class EventSearchForm  {
	public final static String SHOW_NORMAL = "normal";
	public final static String SHOW_HIDDEN = "hidden";
	public final static String SHOW_FLAGGED = "flagged";
    private String what;
    private String where;
    private TimeRange when;
    private Point mapCenter;
    private Boolean mapCenterOverride;
    private Point boundingBoxSW;
    private Point boundingBoxNE;
    private Integer mapZoom;
    private Boolean zoomOverride;
    private Integer mapType;
    private Boolean isGeocodeSuccess;
    private String searchResults;
    private Long editEventId;
    private Long displayEventId;
    private Long linkHereEventId;
    private Boolean isAddEvent;
    private String show;
    private Boolean limitWithinMapBounds;
    private Boolean excludeTimeRangeOverlaps;
    private Boolean embed;
    private String userTag;
    private Boolean kml;
    
	public String getShow() {
		return show;
	}
	public void setShow(String showInvisible) {
		this.show = showInvisible;
	}
	public Integer getMapZoom() {
        return mapZoom;
    }
    public void setMapZoom(Integer mapZoom) {
        this.mapZoom = mapZoom;
    }
    public Boolean getZoomOverride() {
		return zoomOverride;
	}
	public void setZoomOverride(Boolean zoomOverride) {
		this.zoomOverride = zoomOverride;
	}
	public Point getBoundingBoxNE() {
        return boundingBoxNE;
    }
    public void setBoundingBoxNE(Point boundingBoxNE) {
    	this.boundingBoxNE = boundingBoxNE;
    }
    public Point getBoundingBoxSW() {
        return boundingBoxSW;
    }
    public void setBoundingBoxSW(Point boundingBoxSW) {
        this.boundingBoxSW = boundingBoxSW;
    }
    public Point getMapCenter() {
        return mapCenter;
    }
    public void setMapCenter(Point whereLatLng) {
        this.mapCenter = whereLatLng;
    }
    public Boolean getMapCenterOverride() {
		return mapCenterOverride;
	}
	public void setMapCenterOverride(Boolean mapCenterOverride) {
		this.mapCenterOverride = mapCenterOverride;
	}
	public String getWhat() {
        return what;
    }
    public void setWhat(String what) {
        this.what = what;
    }
    public TimeRange getWhen() {
        return when;
    }
    public void setWhen(TimeRange when) {
        this.when = when;
    }
    public String getWhere() {
        return where;
    }
    public void setWhere(String where) {
        this.where = where;
    }
    /**
     * Search results in JSON format need to be passed as a hidden value
     * in the form so that the google maps API javascript functions can retrieve them 
     * @return resutls JSON formatted array of Event objects
     */
    public String getSearchResults() {
        return searchResults;
    }
    public void setSearchResults(String searchResults) {
        this.searchResults = searchResults;
    }
    public Boolean getIsAddEvent() {
        return isAddEvent;
    }
    public void setIsAddEvent(Boolean isAddEvent) {
        this.isAddEvent = isAddEvent;
    }
    public Long getEditEventId() {
        return editEventId;
    }
    public void setEditEventId(Long eventId) {
        this.editEventId = eventId;
    }
    public Long getDisplayEventId() {
		return displayEventId;
	}
	public void setDisplayEventId(Long displayEventId) {
		this.displayEventId = displayEventId;
	}
	public Long getLinkHereEventId() {
		return linkHereEventId;
	}
	public void setLinkHereEventId(Long linkHereEventId) {
		this.linkHereEventId = linkHereEventId;
	}
	public Integer getMapType() {
        return mapType;
    }
    public void setMapType(Integer mapType) {
        this.mapType = mapType;
    }
    public Boolean getIsGeocodeSuccess() {
        return isGeocodeSuccess;
    }
    public void setIsGeocodeSuccess(Boolean isGeocodeSuccess) {
        this.isGeocodeSuccess = isGeocodeSuccess;
    }
	public Boolean getLimitWithinMapBounds() {
		return limitWithinMapBounds;
	}
	public void setLimitWithinMapBounds(Boolean isOnlySearchCurrentMapBounds) {
		this.limitWithinMapBounds = isOnlySearchCurrentMapBounds;
	}
	public Boolean getExcludeTimeRangeOverlaps() {
		return excludeTimeRangeOverlaps;
	}
	public void setExcludeTimeRangeOverlaps(Boolean includeTimeRangeOverlaps) {
		this.excludeTimeRangeOverlaps = includeTimeRangeOverlaps;
	}
	public Boolean getEmbed() {
		return embed;
	}
	public void setEmbed(Boolean embed) {
		this.embed = embed;
	}
	public String getUserTag() {
		return userTag;
	}
	public void setUserTag(String userTag) {
		this.userTag = userTag;
	}
	public Boolean getKml() {
		return kml;
	}
	public void setKml(Boolean kml) {
		this.kml = kml;
	}
	/**
	 * Return a comma delimited string that represents the user viewable form settings
	 * @return
	 */
	public String getAsText() {
		List<String> params = new ArrayList<String>();
		addIfNotEmpty(params, getWhere());
		if (null != getWhen()) {
			params.add(getWhen().getAsText());
		}
		addIfNotEmpty(params, getWhat());
		addIfNotEmpty(params, getUserTag());
		
		return StringUtils.join(params.toArray(), ", ");
	}
	
	private void addIfNotEmpty(List<String> params, String param) {
		if (!StringUtils.isEmpty(param)) {
			params.add(param);
		}
	}
}
