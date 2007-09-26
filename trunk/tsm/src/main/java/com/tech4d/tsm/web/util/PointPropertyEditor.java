package com.tech4d.tsm.web.util;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import net.sf.json.JSONObject;

import java.beans.PropertyEditorSupport;

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
        JSONObject text = new JSONObject();
        text.put("lat", point.getX());
        text.put("lng", point.getY());
        return text.toString();
    }

    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        if (text != null) {
            JSONObject json = JSONObject.fromObject(text);
            Double x = json.getDouble("lat");
            Double y = json.getDouble("lng");
/*            text = StringUtils.strip(text, "{}");
            String coordinate[] = StringUtils.split(text, ',');
            if (coordinate.length != 2) {
                throw new IllegalArgumentException();
            }
            Double x = Double.valueOf(coordinate[0]);
            Double y = Double.valueOf(coordinate[1]);
*/            GeometryFactory gf = new GeometryFactory();
            this.point = gf.createPoint(new Coordinate(x, y));
        }
    }
    
}
