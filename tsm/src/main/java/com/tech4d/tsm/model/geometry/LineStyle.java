package com.tech4d.tsm.model.geometry;

import javax.persistence.Embeddable;

@Embeddable
public class LineStyle {
    private int width;

    public LineStyle() {
        super();
    }

    public LineStyle(int width) {
        super();
        this.width = width;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

}
