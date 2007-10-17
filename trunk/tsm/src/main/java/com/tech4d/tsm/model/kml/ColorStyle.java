package com.tech4d.tsm.model.kml;

import java.awt.Color;

import javax.persistence.MappedSuperclass;

@MappedSuperclass
public class ColorStyle {
    private Color color;
    private String colorMode;
    
    public Color getColor() {
        return color;
    }
    public void setColor(Color color) {
        this.color = color;
    }
    public String getColorMode() {
        return colorMode;
    }
    public void setColorMode(String colorMode) {
        this.colorMode = colorMode;
    }
    
}
