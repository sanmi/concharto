package com.tech4d.tsm.web.util;

import static org.junit.Assert.*;

import org.junit.Test;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;

public class TestPointPropertyEditor {
    private static final double FP_DELTA = .00001;
    private static final double LNG = -77.3416934545;
    private static final double LAT = 40.8491234563;
    private static final String POINT_TEXT = "{\"lat\":" + LAT + ",\"lng\":" + LNG + "}";

    private PointPropertyEditor pointPropertyEditor = new PointPropertyEditor();

    @Test
    public void valueAsText() {
        pointPropertyEditor.setAsText(POINT_TEXT);
        assertEquals(LAT, ((Point) (pointPropertyEditor.getValue())).getY(), FP_DELTA);
        assertEquals(LNG, ((Point) (pointPropertyEditor.getValue())).getX(), FP_DELTA);
        assertEquals(POINT_TEXT, pointPropertyEditor.getAsText());
    }

    @Test public void value() {
        GeometryFactory gf = new GeometryFactory();
        Point point = gf.createPoint(new Coordinate(LNG, LAT));
        pointPropertyEditor.setValue(point);
        assertEquals(POINT_TEXT, pointPropertyEditor.getAsText());
    }
}
