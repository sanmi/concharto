package com.tech4d.tsm.util;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;

public class SensibleMapDefaults {
    public static int ACCURACY_TO_ZOOM[] = {4, 5, 7, 10, 11, 12, 13, 14, 15};
    public static final double SEARCH_BOX_MIN = 40D; //approximate bounding box = miles * 1.4
    public static final double SEARCH_BOX_MAX = 2000D; //approximate bounding box = miles * 1.4
    public static final int ZOOM_BOX_THRESHOLD = 10;
    public static final int ZOOM_COUNTRY = 5;
    public static final int ZOOM_USA = 4;
    public static final int ZOOM_WORLD = 2;
    public static int NUM_ZOOM_LEVELS = 20;  //19 for map data, 20 for satellite (possibly 21)
    public static double[] SEARCH_BOX_DIMENTSIONS = new double[NUM_ZOOM_LEVELS];
    public static Point USA;
    public static Point NORTH_ATLANTIC;
    
    static {
    	//the low zoom levels have variable search boxes
    	int i=0;
    	for ( ; i<ZOOM_BOX_THRESHOLD; i++) { 
    		SEARCH_BOX_DIMENTSIONS[i] = SEARCH_BOX_MAX - i*(SEARCH_BOX_MAX/ZOOM_BOX_THRESHOLD) + SEARCH_BOX_MIN;
    	}
    	//anything over threshold has the same search box 
    	for ( ; i<NUM_ZOOM_LEVELS; i++) {
    		SEARCH_BOX_DIMENTSIONS[i] = SEARCH_BOX_MIN;
    	}
    	GeometryFactory gf = new GeometryFactory();
    	USA = gf.createPoint(new Coordinate(-96.667916, 37.013086));
    	NORTH_ATLANTIC = gf.createPoint(new Coordinate(-40.5176, 38.5482));
    	
    }
}
