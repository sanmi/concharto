package com.tech4d.tsm.model.kml;

import javax.persistence.Embeddable;

@Embeddable
public class PolyStyle {
    private Boolean fill;

    private Boolean outline;

    public PolyStyle() {
        super();
    }

    public PolyStyle(Boolean fill, Boolean outline) {
        super();
        this.fill = fill;
        this.outline = outline;
    }

    public Boolean getFill() {
        return fill;
    }

    public void setFill(Boolean fill) {
        this.fill = fill;
    }

    public Boolean getOutline() {
        return outline;
    }

    public void setOutline(Boolean outline) {
        this.outline = outline;
    }

}
