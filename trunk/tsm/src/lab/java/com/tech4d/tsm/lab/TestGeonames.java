package com.tech4d.tsm.lab;

import static org.junit.Assert.assertEquals;

import org.geonames.FeatureClass;
import org.geonames.Style;
import org.geonames.Toponym;
import org.geonames.ToponymSearchCriteria;
import org.geonames.ToponymSearchResult;
import org.geonames.WebService;
import org.junit.Before;
import org.junit.Test;

public class TestGeonames {
    ToponymSearchCriteria searchCriteria;

    @Before public void setUp() {
        searchCriteria = new ToponymSearchCriteria();
    }

    private ToponymSearchResult assertResults(int numResults) throws Exception {
        ToponymSearchResult searchResult = WebService.search(searchCriteria);
        System.out.println("---------------------------------");
        for (Toponym toponym : searchResult.getToponyms()) {
            System.out.println(toponym.getName() + ", " + toponym.getAdminCode1() + ", "
                    + toponym.getCountryName() + ", " + toponym.getLatitude() + ", " + toponym.getLongitude());
//            System.out.println(toponym.getElevation());
        }
        assertEquals(numResults, searchResult.getTotalResultsCount());
        return searchResult;
    }
    
    @Test public void qSearch() throws Exception {
        String placeName = "colorado springs, co";
        searchCriteria.setQ(placeName);
        searchCriteria.setStyle(Style.FULL);
        searchCriteria.setFeatureClass(FeatureClass.P);
        assertResults(23);
        searchCriteria.setQ(null);
        searchCriteria.setName(placeName);
        assertResults(1);
    }
    
    @Test public void nameSearch() throws Exception {
        searchCriteria.setStyle(Style.FULL);
        String placeName = "Fort Collins";
        searchCriteria.setName(placeName);
        searchCriteria.setFeatureClass(FeatureClass.P);
        ToponymSearchResult searchResult = assertResults(1);
        assertEquals(placeName, searchResult.getToponyms().get(0).getName());

        //The geonames service doesn't do abbreviations algorithmically
        searchCriteria.setName("Ft. Collins, co");
        assertResults(0);
        
        //But it handles alternate names
        searchCriteria.setName("St. Louis, mo");
        assertResults(1);

        //Five results!
        searchCriteria.setName("Saint Louis, mo");
        assertResults(5);

        searchCriteria.setName("fr. collins");
        assertResults(0);
         
    }
}
