package com.tech4d.tsm.dao;

import java.awt.Color;

import org.springframework.context.ApplicationContext;

import com.tech4d.tsm.model.geometry.BalloonStyle;
import com.tech4d.tsm.model.geometry.Icon;
import com.tech4d.tsm.model.geometry.IconStyle;
import com.tech4d.tsm.model.geometry.LabelStyle;
import com.tech4d.tsm.model.geometry.LineStyle;
import com.tech4d.tsm.model.geometry.PolyStyle;
import com.tech4d.tsm.model.geometry.Style;
import com.tech4d.tsm.model.geometry.Vec2;
import com.tech4d.tsm.util.ContextUtil;

public class StyleUtil {
    private static StyleDao styleDao;

    private static Style style = new Style();
    static {
        style.setLabelStyle(new LabelStyle(2.3f));
        style.setLineStyle(new LineStyle(2));
        style.setIconStyle(new IconStyle(.1f, .2f, new Icon(
                "http://localhost:8080/icons", "icon=house"), new Vec2(.2,
                Vec2.UNITS_FRACTION, .4, Vec2.UNITS_FRACTION)));
        style.setBaloonStyle(new BalloonStyle(Color.BLACK, Color.WHITE,
                "Some text", BalloonStyle.DISPLAY_MODE_DEFAULT));
        style.setPolyStyle(new PolyStyle(true, true));
    }

    public static Style getStyle() {
        return style;
    }

    public static Style setupStyle() {
        ApplicationContext appCtx = ContextUtil.getCtx();
        styleDao = (StyleDao) appCtx.getBean("styleDao");
        styleDao.deleteAll();
        // The style has to be created before you create any features. It is
        // a many to one mapping.
        styleDao.save(style);
        return style;
    }
}
