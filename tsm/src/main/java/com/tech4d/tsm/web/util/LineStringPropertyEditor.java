package com.tech4d.tsm.web.util;

import java.beans.PropertyEditorSupport;

import org.apache.commons.lang.StringUtils;

import com.tech4d.tsm.util.JSONFormat;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.Point;

/**
 * Converts JSON string to a LineString and vica versa.
 * TODO fix this!  
 */
public class LineStringPropertyEditor extends PropertyEditorSupport {

    private LineString line;
    
    @Override
    public void setValue(Object value) {
        this.line = (LineString) value;
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
            this.line = JSONFormat.fromJSONLineString(text);
        }
    }
    
}
