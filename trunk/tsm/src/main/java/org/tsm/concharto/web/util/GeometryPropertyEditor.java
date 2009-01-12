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

import com.vividsolutions.jts.geom.Geometry;
import org.apache.commons.lang.StringUtils;
import org.tsm.concharto.util.JSONFormat;

import java.beans.PropertyEditorSupport;

/**
 * Converts JSON string to a LineString and vica versa.
 * TODO fix this!  
 */
public class GeometryPropertyEditor extends PropertyEditorSupport {

    private Geometry geom;
    
    @Override
    public void setValue(Object value) {
        this.geom = (Geometry) value;
    }

    @Override
    public Object getValue() {
        return this.geom;
    }

    @Override
    public String getAsText() {
        //TODO debug
        if (this.geom == null) {
            return null;
        }
        return JSONFormat.toJSON(this.geom);
    }

    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        if (!StringUtils.isEmpty(text)) {
            this.geom = JSONFormat.fromJSONGeomString(text);
        } else {
            this.geom = null;
        }
    }
    
}
