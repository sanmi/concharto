package com.tech4d.tsm.geocode;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * We don't execute this test as part of the test suite because it
 * relies on the google api.  TODO should this be part of the tests?
 * @author frank
 *
 */
public class ManualTestGeocode {
    
    @Test
    public void testGeocode() throws Exception {
        assertNotPlace(GGcoder.geocode("17 Joshs Way, Landenberg, PA"));
        assertPlace(GGcoder.geocode("London Tract Rd, Landenberg, PA"),"");
        assertPlace(GGcoder.geocode("626 Fairway Dr, Thibodaux, LA"), "Thibodaux");
        assertNotPlace(GGcoder.geocode("Turnsers Creek, MD"));
        assertPlace(GGcoder.geocode("Turners Creek, MD"), "");
        assertPlace(GGcoder.geocode("94103"), "San Francisco");
        //"la la land, MD" returns a US postal code location near Front Royal! :)
        assertPlace(GGcoder.geocode("la la land, MD"),"");
        assertPlace(GGcoder.geocode("trucios, spain"),"Trucios-Turtzioz");
        assertPlace(GGcoder.geocode("4, Rue Princesse 75006, paris, france"),"Paris");
    }

    private void assertPlace(GAddress place, String cityName) {
        assertNotNull(place.getLat());
        assertTrue(place.getAccuracy() > 0);
        assertEquals(cityName, place.getCity());
        System.out.println(place);
    }

    private void assertNotPlace(GAddress place) {
        assertEquals(0, place.getAccuracy());
    }
}
