package com.tech4d.tsm.web;

import com.tech4d.tsm.model.geometry.TimeRange;
import com.vividsolutions.jts.geom.Point;

/**
 * Form data object for searching for events
 * 
 * A number of these objects are hidden input values for use by
 * javascript functions
 */
public class EventSearchForm {
    private String what;
    private String where;
    private TimeRange when;
    private Point mapCenter;
    private Point BoundingBoxSW;
    private Point BoundingBoxNE;
    private Integer mapZoom;
    private Integer mapType;
    private String searchResults;
    private Boolean isEditEvent;
    private Integer eventId;
    
    public Integer getMapZoom() {
        return mapZoom;
    }
    public void setMapZoom(Integer mapZoom) {
        this.mapZoom = mapZoom;
    }
    public Point getBoundingBoxNE() {
        return BoundingBoxNE;
    }
    public void setBoundingBoxNE(Point boundingBoxNE) {
        BoundingBoxNE = boundingBoxNE;
    }
    public Point getBoundingBoxSW() {
        return BoundingBoxSW;
    }
    public void setBoundingBoxSW(Point boundingBoxSW) {
        BoundingBoxSW = boundingBoxSW;
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
     * @return resutls JSON formatted array of TsEvent objects 
     */
    public String getSearchResults() {
        return searchResults;
    }
    public void setSearchResults(String searchResults) {
        this.searchResults = searchResults;
    }
    public Boolean getIsEditEvent() {
        return isEditEvent;
    }
    public void setIsEditEvent(Boolean isEditEvent) {
        this.isEditEvent = isEditEvent;
    }
    public Integer getEventId() {
        return eventId;
    }
    public void setEventId(Integer eventId) {
        this.eventId = eventId;
    }
    public Integer getMapType() {
        return mapType;
    }
    public void setMapType(Integer mapType) {
        this.mapType = mapType;
    }
    
}
