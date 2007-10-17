package com.tech4d.tsm.model.kml;

import javax.persistence.Embeddable;

@Embeddable
public class LineStyle {
    private Integer width;

    public LineStyle() {
        super();
    }

    public LineStyle(Integer width) {
        super();
        this.width = width;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

}
