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
