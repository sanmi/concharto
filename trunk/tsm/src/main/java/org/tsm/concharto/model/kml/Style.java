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
package org.tsm.concharto.model.kml;

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
     * @see org.tsm.concharto.model.geometry.StyleI#getLineStyle()
     */
    public LineStyle getLineStyle() {
        return lineStyle;
    }

    /* (non-Javadoc)
     * @see org.tsm.concharto.model.geometry.StyleI#setLineStyle(org.tsm.concharto.model.kml.LineStyle)
     */
    public void setLineStyle(LineStyle lineStyle) {
        this.lineStyle = lineStyle;
    }

    /* (non-Javadoc)
     * @see org.tsm.concharto.model.geometry.StyleI#getBaloonStyle()
     */
    public BalloonStyle getBaloonStyle() {
        return baloonStyle;
    }

    /* (non-Javadoc)
     * @see org.tsm.concharto.model.geometry.StyleI#setBaloonStyle(org.tsm.concharto.model.kml.BalloonStyle)
     */
    public void setBaloonStyle(BalloonStyle baloonStyle) {
        this.baloonStyle = baloonStyle;
    }

    /* (non-Javadoc)
     * @see org.tsm.concharto.model.geometry.StyleI#getIconStyle()
     */
    public IconStyle getIconStyle() {
        return iconStyle;
    }

    /* (non-Javadoc)
     * @see org.tsm.concharto.model.geometry.StyleI#setIconStyle(org.tsm.concharto.model.kml.IconStyle)
     */
    public void setIconStyle(IconStyle iconStyle) {
        this.iconStyle = iconStyle;
    }

    /* (non-Javadoc)
     * @see org.tsm.concharto.model.geometry.StyleI#getLabelStyle()
     */
    public LabelStyle getLabelStyle() {
        return labelStyle;
    }

    /* (non-Javadoc)
     * @see org.tsm.concharto.model.geometry.StyleI#setLabelStyle(org.tsm.concharto.model.kml.LabelStyle)
     */
    public void setLabelStyle(LabelStyle labelStyle) {
        this.labelStyle = labelStyle;
    }

    /* (non-Javadoc)
     * @see org.tsm.concharto.model.geometry.StyleI#getPolyStyle()
     */
    public PolyStyle getPolyStyle() {
        return polyStyle;
    }

    /* (non-Javadoc)
     * @see org.tsm.concharto.model.geometry.StyleI#setPolyStyle(org.tsm.concharto.model.kml.PolyStyle)
     */
    public void setPolyStyle(PolyStyle polyStyle) {
        this.polyStyle = polyStyle;
    }

}
