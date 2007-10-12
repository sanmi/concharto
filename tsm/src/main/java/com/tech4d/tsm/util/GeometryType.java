package com.tech4d.tsm.util;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;

public class GeometryType {
    public static final String POINT="point";
    public static final String LINE="line";
    public static final String POLYGON="polygon";

    /**
     * Utility class to get geometry type as a short string.
     * TODO we should probably use the classname here 
     * @param geom
     * @return geometry type string 
     */
    public static String getGeometryType(Geometry geom) {
        if (geom instanceof Point) {
            return GeometryType.POINT;
        } else if (geom instanceof LineString) {
            return GeometryType.LINE;
        } else if (geom instanceof Polygon) {
            return GeometryType.POLYGON;
        } 
        return null;
    }
}
