package com.tech4d.tsm.web.util;

import static org.junit.Assert.*;

import org.junit.Test;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;

public class TestGeometryPropertyEditor {
    private static final double FP_DELTA = .00001;
    private static final double LNG = -77.3416934545;
    private static final double LAT = 40.8491234563;
    private static final String POINT_TEXT = "{\"lat\":" + LAT + ",\"lng\":" + LNG + ",\"gtype\":\"point\"}";

    private GeometryPropertyEditor propertyEditor = new GeometryPropertyEditor();

    @Test
    public void valueAsText() {
        propertyEditor.setAsText(POINT_TEXT);
        assertEquals(LAT, ((Point) (propertyEditor.getValue())).getY(), FP_DELTA);
        assertEquals(LNG, ((Point) (propertyEditor.getValue())).getX(), FP_DELTA);
        assertEquals(POINT_TEXT, propertyEditor.getAsText());
    }

    @Test public void value() {
        GeometryFactory gf = new GeometryFactory();
        Point point = gf.createPoint(new Coordinate(LNG, LAT));
        propertyEditor.setValue(point);
        assertEquals(POINT_TEXT, propertyEditor.getAsText());
    }
}
