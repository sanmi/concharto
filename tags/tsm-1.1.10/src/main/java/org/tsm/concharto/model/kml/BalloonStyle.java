/*******************************************************************************
 * Copyright 2009 Time Space Map, LLC
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package org.tsm.concharto.model.kml;

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
