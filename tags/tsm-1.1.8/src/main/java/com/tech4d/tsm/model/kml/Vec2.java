/*******************************************************************************
 * ***** BEGIN LICENSE BLOCK *****
 * Version: MPL 1.1
 * 
 * The contents of this file are subject to the Mozilla Public License Version 
 * 1.1 (the "License"); you may not use this file except in compliance with 
 * the License. You may obtain a copy of the License at 
 * http://www.mozilla.org/MPL/
 * 
 * Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 * for the specific language governing rights and limitations under the
 * License.
 * 
 * The Original Code is Concharto.
 * 
 * The Initial Developer of the Original Code is
 * Time Space Map, LLC
 * Portions created by the Initial Developer are Copyright (C) 2007 - 2009
 * the Initial Developer. All Rights Reserved.
 * 
 * Contributor(s):
 * Time Space Map, LLC
 * 
 * ***** END LICENSE BLOCK *****
 ******************************************************************************/
package com.tech4d.tsm.model.kml;

import javax.persistence.Embeddable;

@Embeddable
public class Vec2 {
    private Double x;

    private String xUnits;

    private Double y;

    private String yUnits;

    //TODO should these be an enum? fraction, pixels, insetPixels
    public static String UNITS_FRACTION = "fraction";
    public static String UNITS_PIXELS = "pixels";
    public static String UNITS_INSETPIXELS = "insetPixels";
    
    public Vec2() {
        super();
    }

    public Vec2(Double x, String units, Double y, String units2) {
        super();
        this.x = x;
        xUnits = units;
        this.y = y;
        yUnits = units2;
    }

    public Double getX() {
        return x;
    }

    public void setX(Double x) {
        this.x = x;
    }

    public String getXUnits() {
        return xUnits;
    }

    public void setXUnits(String units) {
        xUnits = units;
    }

    public Double getY() {
        return y;
    }

    public void setY(Double y) {
        this.y = y;
    }

    public String getYUnits() {
        return yUnits;
    }

    public void setYUnits(String units) {
        yUnits = units;
    }

}
