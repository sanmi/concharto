package com.tech4d.tsm.model.geometry;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class IconStyle {
    private Float scale;

    private Float heading;

    private Icon icon;

    private Vec2Type hotSpot;

    public IconStyle() {
        super();
    }

    public IconStyle(Float scale, Float heading, Icon icon, Vec2Type hotSpot) {
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

    public Vec2Type getHotSpot() {
        return hotSpot;
    }

    public void setHotSpot(Vec2Type hotSpot) {
        this.hotSpot = hotSpot;
    }

}
