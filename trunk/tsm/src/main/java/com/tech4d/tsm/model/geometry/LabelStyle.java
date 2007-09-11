package com.tech4d.tsm.model.geometry;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class LabelStyle extends ColorStyle {
    private Float scale;

    public LabelStyle() {
        super();
    }

    public LabelStyle(Float scale) {
        super();
        this.setScale(scale);
    }

    @Column (name="labelScale")
    public Float getScale() {
        return scale;
    }

    public void setScale(Float scale) {
        this.scale = scale;
    }

}
