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
package org.tsm.concharto.model.kml;

import org.tsm.concharto.model.geometry.*;
import org.tsm.concharto.model.time.TimePrimitive;

/**
 * Copyright 2007, Concharto
 *
 * Implements a subset of the KML Feature object for rendering
 * in a viewer such as google maps, google earth, etc
 */
public interface KmlFeature {

    /**
     * Street Address in single line form (e.g. street, city, state, zip)
     * @return String representing street address
     */
    String getStreetAddress();

    void setStreetAddress(String address);

    /**
     * Geometry - this can be a point, polygon, line, etc.
     * @return {@link org.tsm.concharto.model.geometry.TsGeometry} Geometry of this feature
     */
    TsGeometry getTsGeometry();

    void setTsGeometry(TsGeometry geometry);

    String getDescription();

    void setDescription(String description);

    String getSnippet();

    void setSnippet(String snippet);

    /**
     * This can be a single date or a date range
     * @return {@link TimePrimitive} when it happened
     */
    TimePrimitive getTimePrimitive();

    void setTimePrimitive(TimePrimitive timePrimative);

    /**
     * StyleSelector to be used in rendering this KML feature
     * @return {@link org.tsm.concharto.model.kml.StyleSelector}  style to use to draw this feature
     */
    org.tsm.concharto.model.kml.StyleSelector getStyleSelector();

    void setStyleSelector(org.tsm.concharto.model.kml.StyleSelector styleSelector);

    String getSummary();

    void setSummary(String summary);
}
