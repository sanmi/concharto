package com.tech4d.tsm.web.eventsearch;

import com.tech4d.tsm.model.time.TimeRange;
import com.tech4d.tsm.web.util.PaginatingForm;
import com.vividsolutions.jts.geom.Point;

/**
 * Form data object for searching for events
 * 
 * A number of these objects are hidden input values for use by
 * javascript functions
 */
public class EventSearchForm implements PaginatingForm {
	public final static String SHOW_NORMAL = "normal";
	public final static String SHOW_HIDDEN = "hidden";
	public final static String SHOW_FLAGGED = "flagged";
    private String what;
    private String where;
    private TimeRange when;
    private Point mapCenter;
    private Point boundingBoxSW;
    private Point boundingBoxNE;
    private Integer mapZoom;
    private Integer mapType;
    private Boolean isGeocodeSuccess;
    private String searchResults;
    private Long editEventId;
    private Long displayEventId;
    private Boolean isAddEvent;
    private Integer currentRecord;
    private String pageCommand;
    private String show;
    private Boolean isFitViewToResults;
    
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
    /* (non-Javadoc)
     * @see com.tech4d.tsm.web.PaginatingForm#getCurrentRecord()
     */
    public Integer getCurrentRecord() {
        return currentRecord;
    }
    /* (non-Javadoc)
     * @see com.tech4d.tsm.web.PaginatingForm#setCurrentRecord(java.lang.Integer)
     */
    public void setCurrentRecord(Integer currentRecord) {
        this.currentRecord = currentRecord;
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
    /* (non-Javadoc)
     * @see com.tech4d.tsm.web.PaginatingForm#getPageCommand()
     */
    public String getPageCommand() {
        return pageCommand;
    }
    /* (non-Javadoc)
     * @see com.tech4d.tsm.web.PaginatingForm#setPageCommand(java.lang.String)
     */
    public void setPageCommand(String pageCommand) {
        this.pageCommand = pageCommand;
    }
    public Boolean getIsFitViewToResults() {
		return isFitViewToResults;
	}
	public void setIsFitViewToResults(Boolean isFitViewToResults) {
		this.isFitViewToResults = isFitViewToResults;
	}
    
}