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

    /* (non-Javadoc)
     * @see com.tech4d.tsm.model.geometry.StyleI#getLineStyle()
     */
    public LineStyle getLineStyle() {
        return lineStyle;
    }

    /* (non-Javadoc)
     * @see com.tech4d.tsm.model.geometry.StyleI#setLineStyle(com.tech4d.tsm.model.geometry.LineStyle)
     */
    public void setLineStyle(LineStyle lineStyle) {
        this.lineStyle = lineStyle;
    }

    /* (non-Javadoc)
     * @see com.tech4d.tsm.model.geometry.StyleI#getBaloonStyle()
     */
    public BalloonStyle getBaloonStyle() {
        return baloonStyle;
    }

    /* (non-Javadoc)
     * @see com.tech4d.tsm.model.geometry.StyleI#setBaloonStyle(com.tech4d.tsm.model.geometry.BalloonStyle)
     */
    public void setBaloonStyle(BalloonStyle baloonStyle) {
        this.baloonStyle = baloonStyle;
    }

    /* (non-Javadoc)
     * @see com.tech4d.tsm.model.geometry.StyleI#getIconStyle()
     */
    public IconStyle getIconStyle() {
        return iconStyle;
    }

    /* (non-Javadoc)
     * @see com.tech4d.tsm.model.geometry.StyleI#setIconStyle(com.tech4d.tsm.model.geometry.IconStyle)
     */
    public void setIconStyle(IconStyle iconStyle) {
        this.iconStyle = iconStyle;
    }

    /* (non-Javadoc)
     * @see com.tech4d.tsm.model.geometry.StyleI#getLabelStyle()
     */
    public LabelStyle getLabelStyle() {
        return labelStyle;
    }

    /* (non-Javadoc)
     * @see com.tech4d.tsm.model.geometry.StyleI#setLabelStyle(com.tech4d.tsm.model.geometry.LabelStyle)
     */
    public void setLabelStyle(LabelStyle labelStyle) {
        this.labelStyle = labelStyle;
    }

    /* (non-Javadoc)
     * @see com.tech4d.tsm.model.geometry.StyleI#getPolyStyle()
     */
    public PolyStyle getPolyStyle() {
        return polyStyle;
    }

    /* (non-Javadoc)
     * @see com.tech4d.tsm.model.geometry.StyleI#setPolyStyle(com.tech4d.tsm.model.geometry.PolyStyle)
     */
    public void setPolyStyle(PolyStyle polyStyle) {
        this.polyStyle = polyStyle;
    }

}
