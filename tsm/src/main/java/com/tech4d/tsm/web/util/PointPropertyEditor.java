package com.tech4d.tsm.web.util;

import java.beans.PropertyEditorSupport;

import com.tech4d.tsm.util.JSONFormat;
import com.vividsolutions.jts.geom.Point;

/**
 * Converts JSON string to a Point and vica versa.
 * TODO fix this!  
 */
public class PointPropertyEditor extends PropertyEditorSupport {

    private Point point;
    
    @Override
    public void setValue(Object value) {
        this.point = (Point) value;
    }

    @Override
    public Object getValue() {
        return this.point;
    }

    @Override
    public String getAsText() {
        //TODO debug
        if (this.point == null) {
            return null;
        }
        return JSONFormat.toJSON(this.point);
    }

    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        if (text != null) {
            this.point = JSONFormat.fromJSONPoint(text);
        }
    }
    
}
