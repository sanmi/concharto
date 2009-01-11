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
package com.tech4d.tsm.util;

import static org.junit.Assert.assertEquals;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.junit.Test;

import com.tech4d.tsm.dao.EventUtil;
import com.tech4d.tsm.model.Event;
import com.tech4d.tsm.model.PositionalAccuracy;
import com.tech4d.tsm.model.geometry.TsGeometry;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;

/**
 * Test class for the JSONFormat object.
 *
 */
public class TestJSONFormat {

    private EventUtil eventUtil = new EventUtil(null);
    
    @Test public void testFormatEvent() throws ParseException {
        //An event with a point geometry feature
        Event event = eventUtil.createEvent();
        event.setId(33L);
        event.setSummary("summary");
        event.setDescription("description");
        PositionalAccuracy positionalAccuracy = new PositionalAccuracy("Neighborhood");
        positionalAccuracy.setId(22L);
        event.setPositionalAccuracy(positionalAccuracy);
        event.setHasUnresolvedFlag(true);
        event.setZoomLevel(12);
        
        String jsonEvent = JSONFormat.toJSON(event);
        JSONObject json = JSONObject.fromObject(jsonEvent);
        Geometry geom = event.getTsGeometry().getGeometry();
        assertEquals((long)event.getId(), json.getLong(JSONFormat.FIELD_ID));
        assertEquals(event.getSummary(), json.getString(JSONFormat.FIELD_SUMMARY));
        assertEquals(event.getDescription(), json.getString(JSONFormat.FIELD_DESCRIPTION));
        assertEquals(event.getWhere(), json.getString(JSONFormat.FIELD_WHERE));
        assertEquals((long)event.getPositionalAccuracy().getId(), json.getLong(JSONFormat.FIELD_ACCURACY));
        assertEquals(TimeRangeFormat.format(event.getWhen()), json.getString(JSONFormat.FIELD_WHEN));
        assertEquals(event.getSource(), json.getString(JSONFormat.FIELD_SOURCE));
        assertEquals(event.getHasUnresolvedFlag(), json.getBoolean(JSONFormat.FIELD_FLAGGED));
        assertEquals(GeometryType.getGeometryType(event.getTsGeometry().getGeometry()), json.get(JSONFormat.FIELD_GEOMETRYTYPE));
        assertEquals(((Point)geom).getY(), json.getJSONObject(JSONFormat.FIELD_GEOMETRY).getDouble(JSONFormat.FIELD_LAT), .01);
        assertEquals(((Point)geom).getX(), json.getJSONObject(JSONFormat.FIELD_GEOMETRY).getDouble(JSONFormat.FIELD_LNG), .01);
        assertEquals((int)event.getZoomLevel(), json.getInt(JSONFormat.FIELD_ZOOM));
        assertEquals(event.getDiscussion() != null ,json.getBoolean(JSONFormat.FIELD_HAS_DISCUSSION));

        //an event with a line geometry feature
        geom = new WKTReader().read("LINESTRING (10.0 12.0, 20.0 22.0)");
        event.setTsGeometry(new TsGeometry(geom));
        jsonEvent = JSONFormat.toJSON(event);
        json = JSONObject.fromObject(jsonEvent);
        System.out.println(json);
        JSONArray points = json.getJSONObject(JSONFormat.FIELD_GEOMETRY).getJSONArray(JSONFormat.FIELD_LINE);
        assertEquals(12.0, points.getJSONObject(0).getDouble(JSONFormat.FIELD_LAT), .01);
        assertEquals(20.0, points.getJSONObject(1).getDouble(JSONFormat.FIELD_LNG), .01);
        assertEquals(GeometryType.getGeometryType(event.getTsGeometry().getGeometry()), json.get(JSONFormat.FIELD_GEOMETRYTYPE));
        
    }
}
