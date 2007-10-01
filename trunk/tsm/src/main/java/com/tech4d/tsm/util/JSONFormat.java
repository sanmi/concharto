package com.tech4d.tsm.util;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.tech4d.tsm.model.TsEvent;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;

public class JSONFormat {
    private static final String FIELD_ID = "id";
    private static final String FIELD_SUMMARY = "summary";
    private static final String FIELD_DESCRIPTION = "description";
    private static final String FIELD_WHERE = "where";
    private static final String FIELD_WHEN = "when";
    private static final String FIELD_TAGS = "tags";
    private static final String FIELD_SOURCE = "source";
    private static final String FIELD_LAT_LNG = "latLng";

    public static String toJSON(Collection<TsEvent> events) {
        JSONArray jsonEvents = new JSONArray();
        for (TsEvent event : events) {
            jsonEvents.add(toJSON(event));
        }
        return jsonEvents.toString();
    }
    
    //TODO add null checking
    public static String toJSON(TsEvent event) {
        JSONObject jsonEvent = new JSONObject();
        jsonEvent.put(FIELD_ID, event.getId());
        jsonEvent.put(FIELD_SUMMARY, event.getSummary());
        jsonEvent.put(FIELD_DESCRIPTION, event.getDescription());
        jsonEvent.put(FIELD_WHERE, event.getWhere());
        jsonEvent.put(FIELD_WHEN, TimeRangeFormat.format(event.getWhen()));
        jsonEvent.put(FIELD_TAGS, event.getUserTagsAsString());
        jsonEvent.put(FIELD_SOURCE, event.getSourceUrl());
        jsonEvent.put(FIELD_LAT_LNG, toJSON((Point)event.getTsGeometry().getGeometry()));
        return jsonEvent.toString(); 
    }
    
    public static String toJSON(Point point) {
        return toJSONObject(point).toString();
    }
    
    private static JSONObject toJSONObject(Point point) {
        JSONObject jo = new JSONObject();
        jo.put("lat", point.getY());
        jo.put("lng", point.getX());
        return jo;
    }
    
    public static String toJSONObject(Collection<Point> points) {
        Set<JSONObject> JSONPoints = new HashSet<JSONObject>(); 
        for (Point point : points) {
            JSONPoints.add(toJSONObject(point));
        }
        return JSONPoints.toString();
    }
    
    public static Point fromJSONPoint(String text) {
        JSONObject json = JSONObject.fromObject(text);
        Double y = json.getDouble("lat");
        Double x = json.getDouble("lng");
        GeometryFactory gf = new GeometryFactory();
        return gf.createPoint(new Coordinate(x, y));
    }
}
