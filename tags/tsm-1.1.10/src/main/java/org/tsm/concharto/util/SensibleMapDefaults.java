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
    public static int DEFAULT_MAP_TYPE = 0;
    
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
