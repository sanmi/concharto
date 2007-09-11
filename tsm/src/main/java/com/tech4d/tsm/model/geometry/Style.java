package com.tech4d.tsm.model.geometry;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("style")
public class Style extends StyleSelector {
    private LineStyle lineStyle;

    private IconStyle iconStyle;

    private LabelStyle labelStyle;

    private PolyStyle polyStyle;

    private BalloonStyle baloonStyle;

    // private ListStyle listStyle;

    public LineStyle getLineStyle() {
        return lineStyle;
    }

    public void setLineStyle(LineStyle lineStyle) {
        this.lineStyle = lineStyle;
    }

    public BalloonStyle getBaloonStyle() {
        return baloonStyle;
    }

    public void setBaloonStyle(BalloonStyle baloonStyle) {
        this.baloonStyle = baloonStyle;
    }

    public IconStyle getIconStyle() {
        return iconStyle;
    }

    public void setIconStyle(IconStyle iconStyle) {
        this.iconStyle = iconStyle;
    }

    public LabelStyle getLabelStyle() {
        return labelStyle;
    }

    public void setLabelStyle(LabelStyle labelStyle) {
        this.labelStyle = labelStyle;
    }

    public PolyStyle getPolyStyle() {
        return polyStyle;
    }

    public void setPolyStyle(PolyStyle polyStyle) {
        this.polyStyle = polyStyle;
    }

}
