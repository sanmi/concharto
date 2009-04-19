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
package org.tsm.concharto.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.tsm.concharto.model.Event;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

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
 * TODO investigate using a simpler way to do this, perhaps using dynabeans.  
 * NOTE: because this stuff gets used in a repetitive way inside HTML page, it should be fairly terse, so
 * using verbose attribute names should be avoided.
 * 
 * @author frank
 */
public class JSONFormat {
    public static final String FIELD_ID = "id";
    public static final String FIELD_SUMMARY = "summary";
    public static final String FIELD_DESCRIPTION = "description";
    public static final String FIELD_WHERE = "where";
    public static final String FIELD_ACCURACY = "accy";
    public static final String FIELD_WHEN = "when";
    public static final String FIELD_TAGS = "tags";
    public static final String FIELD_SOURCE = "source";
    public static final String FIELD_FLAGGED = "flagged";
    public static final String FIELD_GEOMETRYTYPE="gtype";
    public static final String FIELD_LNG = "lng";
    public static final String FIELD_LAT = "lat";
    public static final String FIELD_GEOMETRY = "geom";
    public static final String FIELD_ZOOM="zoom";
    public static final String FIELD_LINE = "line";
    public static final String FIELD_HAS_DISCUSSION = "hasDiscuss";

    public static String toJSON(Collection<Event> events) {
        JSONArray jsonEvents = new JSONArray();
        for (Event event : events) {
            jsonEvents.add(toJSON(event));
        }
        return jsonEvents.toString();
    }
    
    //TODO add null checking
    public static String toJSON(Event event) {
        JSONObject jsonEvent = new JSONObject();
        jsonEvent.put(FIELD_ID, event.getId());
        jsonEvent.put(FIELD_SUMMARY, event.getSummary());
        jsonEvent.put(FIELD_DESCRIPTION, event.getDescription());
        jsonEvent.put(FIELD_WHERE, event.getWhere());
        String accuracy = "";
        if (null != event.getPositionalAccuracy()) {
        	accuracy = event.getPositionalAccuracy().getId().toString();
        } 
        //TODO this needs to be localized!
        jsonEvent.put(FIELD_ACCURACY, accuracy);
        jsonEvent.put(FIELD_WHEN, TimeRangeFormat.format(event.getWhen()));
        jsonEvent.put(FIELD_TAGS, event.getUserTagsAsString());
        jsonEvent.put(FIELD_SOURCE, event.getSource());
        jsonEvent.put(FIELD_FLAGGED, event.getHasUnresolvedFlag());
        Geometry geom = event.getTsGeometry().getGeometry();
        jsonEvent.put(FIELD_GEOMETRYTYPE, GeometryType.getGeometryType(geom));
        jsonEvent.put(FIELD_GEOMETRY, toJSON(geom));
        jsonEvent.put(FIELD_ZOOM, event.getZoomLevel());
        jsonEvent.put(FIELD_HAS_DISCUSSION, event.getDiscussion() != null);
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
        jo.put(FIELD_LINE, jsonPoints);
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
            if (coordinates.length >0) {
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
