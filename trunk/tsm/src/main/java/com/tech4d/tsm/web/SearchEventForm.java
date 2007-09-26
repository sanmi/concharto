package com.tech4d.tsm.web;

import com.tech4d.tsm.model.geometry.TimeRange;
import com.vividsolutions.jts.geom.Point;

/**
 * Form data object for searching for events
 */
public class SearchEventForm {
    private String what;
    private String where;
    private TimeRange when;
    private Point mapCenter;
    private Point BoundingBoxSW;
    private Point BoundingBoxNE;
    private Integer mapZoom;
    
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
}
