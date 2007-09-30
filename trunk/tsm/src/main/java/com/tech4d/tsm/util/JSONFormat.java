package com.tech4d.tsm.util;

import java.util.Collection;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.tech4d.tsm.model.TsEvent;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;

public class JSONFormat {

    public static String toJSON(Collection<TsEvent> events) {
        JSONArray jsonEvents = new JSONArray();
        for (TsEvent event : events) {
            jsonEvents.add(toJSON(event));
        }
        return jsonEvents.toString();
    }
    
    public static String toJSON(TsEvent event) {
        JSONObject jsonEvent = new JSONObject();
        jsonEvent.put("id", event.getId());
        jsonEvent.put("summary", event.getSummary());
        jsonEvent.put("description", event.getDescription());
        jsonEvent.put("where", event.getWhere());
        jsonEvent.put("when", TimeRangeFormat.format(event.getWhen()));
        jsonEvent.put("tags", event.getUserTagsAsString());
        jsonEvent.put("source", event.getSourceUrl());
        jsonEvent.put("latLng", toJSON((Point)event.getTsGeometry().getGeometry()));
        return jsonEvent.toString(); 
    }
    
    public static String toJSON(Point point) {
        return toJSONObject(point).toString();
    }
    
    private static JSONObject toJSONObject(Point point) {
        JSONObject jo = new JSONObject();
        jo.put("lat", point.getX());
        jo.put("lng", point.getY());
        return jo;
    }
    
    public static Point fromJSONPoint(String text) {
        JSONObject json = JSONObject.fromObject(text);
        Double x = json.getDouble("lat");
        Double y = json.getDouble("lng");
        GeometryFactory gf = new GeometryFactory();
        return gf.createPoint(new Coordinate(x, y));
    }
}
