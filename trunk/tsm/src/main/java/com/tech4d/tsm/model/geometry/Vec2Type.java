package com.tech4d.tsm.model.geometry;

import javax.persistence.Embeddable;

@Embeddable
public class Vec2Type {
    private Double x;

    private String xUnits;

    private Double y;

    private String yUnits;

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

    public Vec2Type(Double x, String units, Double y, String units2) {
        super();
        this.x = x;
        xUnits = units;
        this.y = y;
        yUnits = units2;
    }

    public Vec2Type() {
        super();
    }

}
