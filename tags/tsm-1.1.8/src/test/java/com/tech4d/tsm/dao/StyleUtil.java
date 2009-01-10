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
package com.tech4d.tsm.dao;

import java.awt.Color;

import org.springframework.context.ApplicationContext;

import com.tech4d.tsm.model.kml.BalloonStyle;
import com.tech4d.tsm.model.kml.Icon;
import com.tech4d.tsm.model.kml.LineStyle;
import com.tech4d.tsm.model.kml.Style;
import com.tech4d.tsm.model.kml.Vec2;
import com.tech4d.tsm.util.ContextUtil;

public class StyleUtil {

    private static Style style = new Style();
    static {
        style.setLabelStyle(new com.tech4d.tsm.model.kml.LabelStyle(2.3f));
        style.setLineStyle(new LineStyle(2));
        style.setIconStyle(new com.tech4d.tsm.model.kml.IconStyle(.1f, .2f, new Icon(
                "http://localhost:8080/icons", "icon=house"), new com.tech4d.tsm.model.kml.Vec2(.2,
                com.tech4d.tsm.model.kml.Vec2.UNITS_FRACTION, .4, Vec2.UNITS_FRACTION)));
        style.setBaloonStyle(new BalloonStyle(Color.BLACK, Color.WHITE,
                "Some text", com.tech4d.tsm.model.kml.BalloonStyle.DISPLAY_MODE_DEFAULT));
        style.setPolyStyle(new com.tech4d.tsm.model.kml.PolyStyle(true, true));
    }

    public static Style getStyle() {
        return style;
    }

    public static Style setupStyle() {
        ApplicationContext appCtx = ContextUtil.getCtx();
        StyleDao styleDao=(StyleDao) appCtx.getBean("styleDao");
        styleDao.deleteAll();
        // The style has to be created before you create any features. It is
        // a many to one mapping.
        styleDao.save(style);
        return style;
    }
}
