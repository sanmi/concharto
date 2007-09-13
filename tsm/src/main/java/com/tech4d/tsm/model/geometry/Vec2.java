package com.tech4d.tsm.model.geometry;

import javax.persistence.Embeddable;

@Embeddable
public class Vec2 {
    private Double x;

    private String xUnits;

    private Double y;

    private String yUnits;

    //TODO should these be an enum? fraction, pixels, insetPixels
    public static String UNITS_FRACTION = "fraction";
    public static String UNITS_PIXELS = "pixels";
    public static String UNITS_INSETPIXELS = "insetPixels";
    
    public Vec2() {
        super();
    }

    public Vec2(Double x, String units, Double y, String units2) {
        super();
        this.x = x;
        xUnits = units;
        this.y = y;
        yUnits = units2;
    }

    public Double getX() {
        return x;
    }

    public void setX(Double x) {
        this.x = x;
    }

    public String getXUnits() {
        return xUnits;
    }

    public void setXUnits(String units) {
        xUnits = units;
    }

    public Double getY() {
        return y;
    }

    public void setY(Double y) {
        this.y = y;
    }

    public String getYUnits() {
        return yUnits;
    }

    public void setYUnits(String units) {
        yUnits = units;
    }

}
