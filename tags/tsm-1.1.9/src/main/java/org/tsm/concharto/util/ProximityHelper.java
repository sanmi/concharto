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

import java.util.HashSet;
import java.util.Set;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.util.GeometricShapeFactory;
/**
 * Functions for calculating bounding box and proximity.
 * TODO: these functions are based on the spherical law of cosines documented
 * here http://www.movable-type.co.uk/scripts/latlong.html.  The blogger claims that they
 * are accurate and the distance functions seems to be so, but the bounding box calculation
 * is off by 30 or 40 percent.  When attempting to create a 50 mile by 50 mile bounding box, I
 * get a box that is about 83 miles high and 64 miles wide.  This is consistent at
 * the equator and the arctic, so I'm going to leave it for now, but it would be
 * nice to figure this out some time in the future --fsm 11-7-07  
 * 
 * @author frank
 *
 */
public class ProximityHelper {
	private static final double EARTHS_RADIUS = 3963.0D;
    private static final double LONGITUDE_180 = 180d;

    /**
     * Calculate the bounding box, given the north east and south west coordinates of 
     * a box.  Take into account boxes that span the dateline where longitude changes
     * from 180 to -180
     * 
     * @return Set<Geometry> one or two POLYGON objects representing the bounding box
     * @param boundingBoxSW south west corner
     * @param boundingBoxNE north east corner
     */
    public static Set<Geometry> getBoundingBoxes(Point boundingBoxSW, Point boundingBoxNE) {
    	//longitudes have to contend with the international date line where it switches from -180 to +180
    	//so we mod 180.  We assume the bounding box is less than 360 degrees.  If you want to figure
    	//this out, you might want to draw it on paper
        Double height = boundingBoxNE.getY() - boundingBoxSW.getY();
    	Double east = boundingBoxNE.getX();
    	Double west = boundingBoxSW.getX();
    	return handleDateLine(boundingBoxSW.getY(), east, west, height);
    }

    private static Set<Geometry> handleDateLine(Double south, Double east, Double west, Double height) {
        Set<Geometry> polygons = new HashSet<Geometry>();
        if (east < west) {
            //ok this box spans the date line.  We need two bounding boxes.
            Double westWidth = LONGITUDE_180 + east;
            Double eastWidth = LONGITUDE_180 - west;
            
            Geometry westmost = makeRectangle(height, westWidth,  -LONGITUDE_180, south); 
            Geometry eastmost = makeRectangle(height, eastWidth,  west, south); 

            polygons.add(westmost);
            polygons.add(eastmost);
        } else {
            polygons.add(makeRectangle(height, east-west,  west, south));
        }
        return polygons;
    }
    
    private static Geometry makeRectangle(Double height, Double width, Double eastMost, Double southMost) {
        GeometricShapeFactory gsf = new GeometricShapeFactory();
        gsf.setNumPoints(4);
        gsf.setBase(new Coordinate(eastMost, southMost));
        gsf.setHeight(height);
        gsf.setWidth(width);
        return gsf.createRectangle();
    }


	/**
	 * Calculate a bounding box roughly corresponding to a search radius based
	 * on the given spherical coordinate.
	 * Taken from http://www.movable-type.co.uk/scripts/latlong.html
	 * 
     * @return Set<Geometry> one or two a POLYGON objects representing the bounding box
	 * @param radius search radius
	 * @param point center point
	 */
	public static LatLngBounds getBounds(Double radius, Point point) {
		Double lat1 = Math.toRadians(point.getY());
		Double lng1 = Math.toRadians(point.getX());
		Double R = EARTHS_RADIUS;
		Double south = Math.asin( Math.sin(lat1)*Math.cos(radius/R) + 
                Math.cos(lat1)*Math.sin(radius/R)*Math.cos(180) );
		Double west = lng1 + Math.atan2(Math.sin(-90)*Math.sin(radius/R)*Math.cos(lat1), 
                Math.cos(radius/R)-Math.sin(lat1)*Math.sin(south));
		Double east = lng1 + Math.atan2(Math.sin(90)*Math.sin(radius/R)*Math.cos(lat1), 
				Math.cos(radius/R)-Math.sin(lat1)*Math.sin(south));
		Double height = (lat1- south)*2.3;  //kludge! this should be '2' but for some reason the height is squashed
		//make it work like google maps' bounding box
		west = Math.toDegrees(west);
		if (west < -LONGITUDE_180) {
			west = LONGITUDE_180*2 + west;
		}
		south = Math.toDegrees(south);
		Double north = south + Math.toDegrees(height);
		east = Math.toDegrees(east);
		
		LatLngBounds bounds = new LatLngBounds();
		GeometryFactory gf = new GeometryFactory();
		bounds.setSouthWest(gf.createPoint(new Coordinate(west, south)));
		bounds.setNorthEast(gf.createPoint(new Coordinate(east, north)));
		return bounds;
	}

	/**
	 * Calculate distance between two points using the great circle spherical transformation
	 * Taken from http://www.movable-type.co.uk/scripts/latlong.html
	 * 
	 * @param p1 Point 1
	 * @param p2 Point 2
	 * @return distance in miles
	 */
	public static Double getDistance(Point p1, Point p2) {
		Double lat1=Math.toRadians(p1.getY());
		Double lat2=Math.toRadians(p2.getY());
		Double lon1=Math.toRadians(p1.getX());
		Double lon2=Math.toRadians(p2.getX());
		
		Double d = Math.acos(Math.sin(lat1)*Math.sin(lat2) + 
                Math.cos(lat1)*Math.cos(lat2) *
                Math.cos(lon2-lon1)) * EARTHS_RADIUS;
		//System.out.println("distance: " + d);
		return d;
	}
	
}
