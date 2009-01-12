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
package org.tsm.concharto.web.util;

import static org.junit.Assert.*;

import net.sf.json.JSONObject;

import org.junit.Test;
import org.tsm.concharto.web.util.GeometryPropertyEditor;

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
        verifyText(propertyEditor.getAsText());
    }
    
    private void verifyText(String text) {
        JSONObject point = JSONObject.fromObject(text);
        assertEquals(LAT, point.get("lat"));
        assertEquals(LNG, point.get("lng"));        
    }

    @Test public void value() {
        GeometryFactory gf = new GeometryFactory();
        Point point = gf.createPoint(new Coordinate(LNG, LAT));
        propertyEditor.setValue(point);
        verifyText(propertyEditor.getAsText());
    }
}
