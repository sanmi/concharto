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
