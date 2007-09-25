package com.tech4d.tsm.web;

import com.tech4d.tsm.model.geometry.TimeRange;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;

/**
 * Form data object for searching for events
 */
public class SearchEventForm {
    private String what;
    private String where;
    private TimeRange when;
    private Point whereLatLng;
    private Polygon whereBoundingBox;
    
    public Polygon getWhereBoundingBox() {
        return whereBoundingBox;
    }
    public void setWhereBoundingBox(Polygon whereBoundingBox) {
        this.whereBoundingBox = whereBoundingBox;
    }
    public Point getWhereLatLng() {
        return whereLatLng;
    }
    public void setWhereLatLng(Point whereLatLng) {
        this.whereLatLng = whereLatLng;
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
