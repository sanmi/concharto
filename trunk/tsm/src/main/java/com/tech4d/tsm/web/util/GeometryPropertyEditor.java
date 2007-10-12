package com.tech4d.tsm.web.util;

import java.beans.PropertyEditorSupport;

import org.apache.commons.lang.StringUtils;

import com.tech4d.tsm.util.JSONFormat;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.Point;

/**
 * Converts JSON string to a LineString and vica versa.
 * TODO fix this!  
 */
public class GeometryPropertyEditor extends PropertyEditorSupport {

    private Geometry line;
    
    @Override
    public void setValue(Object value) {
        this.line = (Geometry) value;
    }

    @Override
    public Object getValue() {
        return this.line;
    }

    @Override
    public String getAsText() {
        //TODO debug
        if (this.line == null) {
            return null;
        }
        return JSONFormat.toJSON(this.line);
    }

    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        if (!StringUtils.isEmpty(text)) {
            this.line = (Geometry) JSONFormat.fromJSONGeomString(text);
        }
    }
    
}
