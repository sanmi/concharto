package com.tech4d.tsm.web.util;

import com.tech4d.tsm.util.JSONFormat;
import com.vividsolutions.jts.geom.Geometry;
import org.apache.commons.lang.StringUtils;

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
