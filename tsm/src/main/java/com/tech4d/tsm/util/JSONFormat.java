package com.tech4d.tsm.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.tech4d.tsm.model.TsEvent;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.Point;

public class JSONFormat {
    public static final String FIELD_ID = "id";
    public static final String FIELD_SUMMARY = "summary";
    public static final String FIELD_DESCRIPTION = "description";
    public static final String FIELD_WHERE = "where";
    public static final String FIELD_WHEN = "when";
    public static final String FIELD_TAGS = "tags";
    public static final String FIELD_SOURCE = "source";
    public static final String FIELD_LAT_LNG = "latLng";
    public static final String FIELD_GEOMETRYTYPE="gtype";

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
        Geometry geom = event.getTsGeometry().getGeometry();
        if (geom instanceof Point) {
            jsonEvent.put(FIELD_LAT_LNG, toJSON((Point)geom));
            jsonEvent.put(FIELD_GEOMETRYTYPE, GeometryType.POINT);            
        } else if (geom instanceof LineString) {
            jsonEvent.put(FIELD_LAT_LNG, toJSON((LineString)geom));
            jsonEvent.put(FIELD_GEOMETRYTYPE, GeometryType.LINE);            
            
        }
        return jsonEvent.toString(); 
    }
    
    public static String toJSON(LineString line) {
        List<JSONObject> JSONPoints = new ArrayList<JSONObject>(); 
        for (int i=0; i<line.getNumPoints(); i++) {
            JSONPoints.add(toJSONObject(line.getPointN(i)));
        }
        return JSONPoints.toString();
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
        List<JSONObject> JSONPoints = new ArrayList<JSONObject>(); 
        for (Point point : points) {
            JSONPoints.add(toJSONObject(point));
        }
        return JSONPoints.toString();
    }
    
    public static Point fromJSONPoint(String text) {
        JSONObject json = JSONObject.fromObject(text);
        return fromJSONPoint(json);
    }
    
    private static Point fromJSONPoint(JSONObject json) {
        Double y = json.getDouble("lat");
        Double x = json.getDouble("lng");
        GeometryFactory gf = new GeometryFactory();
        return gf.createPoint(new Coordinate(x, y));    }

    public static LineString fromJSONLineString(String text) {
        JSONObject json = JSONObject.fromObject(text);
        JSONArray jsonPoints = json.getJSONArray("line");
        Coordinate[] coordinates = new Coordinate[jsonPoints.size()];
        int i=0;
        for (Object obj : jsonPoints) {
            Point point = fromJSONPoint((JSONObject) obj);
            coordinates[i++] = point.getCoordinate();
        }
        GeometryFactory gf = new GeometryFactory();
        return gf.createLineString(coordinates);
    }
}
