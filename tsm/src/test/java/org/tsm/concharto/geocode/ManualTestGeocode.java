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
package org.tsm.concharto.geocode;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.tsm.concharto.geocode.GAddress;
import org.tsm.concharto.geocode.GGcoder;

/**
 * We don't execute this test as part of the test suite because it
 * relies on the google api.  TODO should this be part of the tests?
 * @author frank
 *
 */
public class ManualTestGeocode {
    private String key = "ABQIAAAA1DZDDhaKApTfIDHGfvo13hSQekddw1ZVY1OywWYSY7GTmNOxgRQ1UKcA9cKipDAZNLJ5R_X-JJcYhw";
    @Test
    public void testGeocode() throws Exception {
        assertPlace(GGcoder.geocode("94103", key), "San Francisco"); 
        assertNotPlace(GGcoder.geocode("17 Joshs Way, Landenberg, PA", key));
        assertPlace(GGcoder.geocode("London Tract Rd, Landenberg, PA", key),"");
        assertPlace(GGcoder.geocode("626 Fairway Dr, Thibodaux, LA", key), "Thibodaux");
        assertPlace(GGcoder.geocode("620000000006 Fairway Dr, Thibodaux, LA", key), "Thibodaux");
        assertPlace(GGcoder.geocode("Turnsers Creek, MD", key)); //they are kind of loose here
        assertPlace(GGcoder.geocode("Turners Creek, MD", key), "");
        //"la la land, MD" returns a US postal code location near Front Royal! :)
        assertPlace(GGcoder.geocode("la la land, MD", key),"");
        assertPlace(GGcoder.geocode("la la land, PA", key),"Schnecksville");
        assertPlace(GGcoder.geocode("la land, PA", key),"Schnecksville");
        assertPlace(GGcoder.geocode("la la land, CA", key),"Land");  //Land, North Dakota
        assertPlace(GGcoder.geocode("la la land, NY", key),"");
        assertPlace(GGcoder.geocode("Farm Land Rd Way, Mifflinburg, PA 17844, PA", key),"Mifflinburg");
        assertPlace(GGcoder.geocode("trucios, spain", key),"Trucios-Turtzioz");
        assertPlace(GGcoder.geocode("4, Rue Princesse 75006, paris, france", key),"Paris");
    }

    private void assertPlace(GAddress place) {
        assertNotNull(place.getPoint());
        assertTrue(place.getAccuracy() > 0);
        assertNotNull(place.getPoint().getX());
        assertNotNull(place.getPoint().getY());
        System.out.println(place);
    }
    
    private void assertPlace(GAddress place, String cityName) {
        assertPlace(place);
        assertEquals(cityName, place.getCity());
    }

    private void assertNotPlace(GAddress place) {
        assertEquals(0, place.getAccuracy());
    }    
}
