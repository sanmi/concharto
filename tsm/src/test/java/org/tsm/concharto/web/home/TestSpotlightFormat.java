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
package org.tsm.concharto.web.home;

import static org.junit.Assert.*;

import org.apache.commons.lang.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.tsm.concharto.model.Spotlight;

public class TestSpotlightFormat {
    Spotlight spotlight = new Spotlight(); 
    @Before public void setUp() {
        spotlight.setLabel("hello this is a [[spotlight]]");
    }
    
    @Test public void testFormat() {
        spotlight.setLink("http://lala.com/search/eventsearch.htm?_what=%22Mongolian+Invasion+of+Central+Asia%22&_maptype=0");
        assertNotContains("\"Mongolian", spotlight);
        spotlight.setLink("http://lala.com/search/eventsearch.htm?_what=5+year's+war&_maptype=0");
        assertNotContains("year's", spotlight);
    }
    
    private void assertNotContains(String substring, Spotlight spotlight) {
        assertNotContains(substring, SpotlightFormat.formatEmbedLabel(spotlight));
        assertNotContains(substring, SpotlightFormat.formatLabel(spotlight));
    }
    private void assertNotContains(String substring, String string) {
        assertTrue(string + "CONTAINS" + substring, !StringUtils.contains(string, substring));
    }
}
