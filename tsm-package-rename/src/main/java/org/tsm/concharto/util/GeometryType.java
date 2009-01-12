/*******************************************************************************
 * ***** BEGIN LICENSE BLOCK *****
 * Version: MPL 1.1
 * 
 * The contents of this file are subject to the Mozilla Public License Version 
 * 1.1 (the "License"); you may not use this file except in compliance with 
 * the License. You may obtain a copy of the License at 
 * http://www.mozilla.org/MPL/
 * 
 * Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 * for the specific language governing rights and limitations under the
 * License.
 * 
 * The Original Code is Concharto.
 * 
 * The Initial Developer of the Original Code is
 * Time Space Map, LLC
 * Portions created by the Initial Developer are Copyright (C) 2007 - 2009
 * the Initial Developer. All Rights Reserved.
 * 
 * Contributor(s):
 * Time Space Map, LLC
 * 
 * ***** END LICENSE BLOCK *****
 ******************************************************************************/
package org.tsm.concharto.util;

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
