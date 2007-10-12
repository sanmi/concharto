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
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;

/**
 * For formatting events and geometries.  
 * TODO split this into two classes.
 * 
 * @author frank
 */
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
    public static final String FIELD_LNG = "lng";
    public static final String FIELD_LAT = "lat";
    public static final String FIELD_POINTS = "line";

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
            
        } else if (geom instanceof Polygon) {
            jsonEvent.put(FIELD_LAT_LNG, toJSON((Polygon)geom));
            jsonEvent.put(FIELD_GEOMETRYTYPE, GeometryType.POLYGON);            
            
        }
        return jsonEvent.toString(); 
    }
    
    /**
     * @param geometry
     * @return json string for a point, line or polygon.  Null if the type is neither.
     */
    public static String toJSON(Geometry geometry) {
        if (geometry instanceof Point) {
           return toJSON((Point)geometry); 
        } else if (geometry instanceof LineString) {
            return toJSON((LineString)geometry, GeometryType.LINE);
        }else if (geometry instanceof Polygon) {
            //we serialize the polygon as a single linestring
            return toJSON(((Polygon)geometry).getExteriorRing(), GeometryType.POLYGON);
        }
        return null;
    }
    
    private static String toJSON(LineString line, String type) {
        JSONObject jo = new JSONObject();
        jo.put(FIELD_GEOMETRYTYPE, type);        
        List<JSONObject> jsonPoints = new ArrayList<JSONObject>();
        for (int i=0; i<line.getNumPoints(); i++) {
            jsonPoints.add(toJSONObject(line.getPointN(i)));
        }
        jo.put(FIELD_POINTS, jsonPoints);
        return jo.toString();
    }

    private static String toJSON(Point point) {
        return toJSONObject(point).toString();
    }
    
    private static JSONObject toJSONObject(Point point) {
        JSONObject jo = new JSONObject();
        jo.put(FIELD_GEOMETRYTYPE, GeometryType.POINT);
        jo.put(FIELD_LAT, point.getY());
        jo.put(FIELD_LNG, point.getX());
        return jo;
    }
    
    public static String toJSONObject(Collection<Point> points) {
        List<JSONObject> JSONPoints = new ArrayList<JSONObject>(); 
        for (Point point : points) {
            JSONPoints.add(toJSONObject(point));
        }
        return JSONPoints.toString();
    }
    
    private static Point fromJSONPoint(JSONObject json) {
        Double y = json.getDouble(FIELD_LAT);
        Double x = json.getDouble(FIELD_LNG);
        GeometryFactory gf = new GeometryFactory();
        return gf.createPoint(new Coordinate(x, y));    }

/*    public static Point fromJSONPoint(String text) {
        JSONObject json = JSONObject.fromObject(text);
        return fromJSONPoint(json);
    }
    

    public static LineString fromJSONLineString(String text) {
        JSONObject json = JSONObject.fromObject(text);
        JSONArray jsonPoints = json.getJSONArray(FIELD_POINTS);
        Coordinate[] coordinates = new Coordinate[jsonPoints.size()];
        int i=0;
        for (Object obj : jsonPoints) {
            Point point = fromJSONPoint((JSONObject) obj);
            coordinates[i++] = point.getCoordinate();
        }
        GeometryFactory gf = new GeometryFactory();
        return gf.createLineString(coordinates);
    }
*/    
    public static Geometry fromJSONGeomString(String text) {
        JSONObject json = JSONObject.fromObject(text);
        String geometryType = json.getString(FIELD_GEOMETRYTYPE);
        GeometryFactory gf = new GeometryFactory();
        Geometry result = null;
        if (GeometryType.POINT.equals(geometryType)) {
            Double y = json.getDouble(FIELD_LAT);
            Double x = json.getDouble(FIELD_LNG);
            result = gf.createPoint(new Coordinate(x, y));    
        } else if (GeometryType.LINE.equals(geometryType)) {
            JSONArray jsonPoints = json.getJSONArray(GeometryType.LINE);
            result = gf.createLineString(getCoordinates(jsonPoints));
        } else if (GeometryType.POLYGON.equals(geometryType)) {
            JSONArray jsonPoints = json.getJSONArray(GeometryType.LINE);
            Coordinate[] coordinates = getCoordinates(jsonPoints);
            //if they didn't close the polygon, we will close it for them
            if (!coordinates[0].equals(coordinates[coordinates.length-1])) {
                //create a larger array and copy the contents
                Coordinate[] closedCoordinates = new Coordinate[coordinates.length+1];
                for (int i=0; i<coordinates.length; i++) {
                    closedCoordinates[i] = coordinates[i];
                }
                //last element = first element
                Coordinate closure = (Coordinate) coordinates[0].clone();
                closedCoordinates[closedCoordinates.length-1] = closure;
                coordinates = closedCoordinates;
            }
            LinearRing ring = gf.createLinearRing(coordinates); 
            result = gf.createPolygon(ring, null);
        }
        return result;
    }
    
    private static Coordinate[] getCoordinates(JSONArray jsonPoints) {
        Coordinate[] coordinates = new Coordinate[jsonPoints.size()];
        int i=0;
        for (Object obj : jsonPoints) {
            Point point = fromJSONPoint((JSONObject) obj);
            coordinates[i++] = point.getCoordinate();
        }
        return coordinates;
    }
}
