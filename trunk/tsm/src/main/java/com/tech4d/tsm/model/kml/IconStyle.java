package com.tech4d.tsm.model.kml;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class IconStyle {
    private Float scale;

    private Float heading;

    private Icon icon;

    private com.tech4d.tsm.model.kml.Vec2 hotSpot;

    public IconStyle() {
        super();
    }

    public IconStyle(Float scale, Float heading, Icon icon, com.tech4d.tsm.model.kml.Vec2 hotSpot) {
        super();
        this.scale = scale;
        this.heading = heading;
        this.icon = icon;
        this.hotSpot = hotSpot;
    }

    public Float getHeading() {
        return heading;
    }

    public void setHeading(Float heading) {
        this.heading = heading;
    }

    @Column(name = "iconScale")
    public Float getScale() {
        return scale;
    }

    public void setScale(Float scale) {
        this.scale = scale;
    }

    public Icon getIcon() {
        return icon;
    }

    public void setIcon(Icon icon) {
        this.icon = icon;
    }

    public com.tech4d.tsm.model.kml.Vec2 getHotSpot() {
        return hotSpot;
    }

    public void setHotSpot(com.tech4d.tsm.model.kml.Vec2 hotSpot) {
        this.hotSpot = hotSpot;
    }

}
