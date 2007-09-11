package com.tech4d.tsm.model.geometry;

import java.awt.Color;

import javax.persistence.Embeddable;

@Embeddable
public class BalloonStyle {
    private Color bgColor;

    private Color textColor;

    private String text;

    private String displayMode;

    public static String DISPLAY_MODE_HIDE = "hide";

    public static String DISPLAY_MODE_DEFAULT = "default";

    public BalloonStyle() {
        super();
    }

    public BalloonStyle(Color bgColor, Color textColor, String text,
            String displayMode) {
        super();
        this.bgColor = bgColor;
        this.textColor = textColor;
        this.text = text;
        this.displayMode = displayMode;
    }

    public Color getBgColor() {
        return bgColor;
    }

    public void setBgColor(Color bgColor) {
        this.bgColor = bgColor;
    }

    public String getDisplayMode() {
        return displayMode;
    }

    public void setDisplayMode(String displayMode) {
        this.displayMode = displayMode;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Color getTextColor() {
        return textColor;
    }

    public void setTextColor(Color textColor) {
        this.textColor = textColor;
    }
}
