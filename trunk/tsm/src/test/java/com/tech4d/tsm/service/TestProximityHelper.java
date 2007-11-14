package com.tech4d.tsm.service;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.tech4d.tsm.util.LatLngBounds;
import com.tech4d.tsm.util.ProximityHelper;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;

//center is 38.371528, -75.58672
//bounding box is 
//-75.90110778808594 38.205274034117814, -75.27214050292969 38.205274034117814, -75.27214050292969 38.53688722680627, -75.90110778808594 38.53688722680627, -75.90110778808594 38.205274034117814
//width = 34.04 mi
//height = 22.94 mi
public class TestProximityHelper {

	@Test public void testGetBoundingBox() {
		LatLngBounds bounds = ProximityHelper.getBounds(34.02/2, makePoint( -75.58672, 38.371528));
		assertEquals(-75.9D, bounds.getSouthWest().getX(), .1);
		
		bounds = ProximityHelper.getBounds(22.29/2, makePoint( -75.58672, 38.371528));
        assertEquals(38.5D, bounds.getNorthEast().getY(), .1);
	}
	

	@Test public void distance() {
		assertEquals(41.10D, ProximityHelper.getDistance(
				makePoint(-75.901107, 38.205274), 
				makePoint(-75.272140, 38.536887)), .01);
		assertEquals(22.94D, ProximityHelper.getDistance(
				makePoint(-75.90110778808594, 38.53688722680627),
				makePoint(-75.90110778808594, 38.205274034117814)), .01);
		assertEquals(34.03D, ProximityHelper.getDistance(
				makePoint(-75.27214050292969, 38.53688722680627),
				makePoint(-75.90110778808594, 38.53688722680627)), .01);
	}
	
	private Point makePoint(double x, double y) {
		GeometryFactory gf = new GeometryFactory();
		return gf.createPoint(new Coordinate(x, y));
	}

	public static double round(double value, int decimalPlace)
	  {
	    double power_of_ten = 1;
	    while (decimalPlace-- > 0)
	       power_of_ten *= 10.0;
	    return Math.round(value * power_of_ten) / power_of_ten;
	  }
}
