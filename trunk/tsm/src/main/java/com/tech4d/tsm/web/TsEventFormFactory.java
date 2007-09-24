package com.tech4d.tsm.web;

import com.tech4d.tsm.model.TsEvent;
import com.tech4d.tsm.model.UserTag;
import com.tech4d.tsm.model.geometry.TsGeometry;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.util.GeometricShapeFactory;

import static org.apache.commons.lang.StringUtils.join;
import static org.apache.commons.lang.StringUtils.split;

import java.util.ArrayList;
import java.util.List;

public class TsEventFormFactory {

    public static TsEvent createTsEvent() {
        return  new TsEvent();
    }

    public static TsEvent createTsEvent(TsEventForm tsEventForm) {
        return updateTsEvent(createTsEvent(), tsEventForm);
    }

    public static TsEvent updateTsEvent(TsEvent tsEvent, TsEventForm tsEventForm) {
        tsEvent.setId(tsEventForm.getId());
        tsEvent.setDescription(tsEventForm.getDescription());
        tsEvent.setSummary(tsEventForm.getSummary());
        tsEvent.setSourceUrl(tsEventForm.getSource());
        tsEvent.setStreetAddress(tsEventForm.getWhere());
        addGeometry(tsEvent, tsEventForm);

        // a dirty check so we don't have to save each time
        String originalTags = convertToString(tsEvent.getUserTags());
        boolean dirty = false;
        if (originalTags == null) {
            if (tsEventForm.getTags() != null) {
                dirty = true;
            }
        } else {
            if (!originalTags.equals(tsEventForm.getTags())) {
                dirty = true;
            }
        }
        if (dirty) {
            String[] tags = split(tsEventForm.getTags(), ",");
            List<UserTag> userTags = new ArrayList<UserTag>();
            for (String tag : tags) {
                userTags.add(new UserTag(tag));
            }
            tsEvent.setUserTags(userTags);
        }
        return tsEvent;
    }

    private static void addGeometry(TsEvent tsEvent, TsEventForm tsEventForm) {
        GeometryFactory gsf = new GeometryFactory();
        Coordinate coor = new Coordinate(tsEventForm.getLat(), tsEventForm.getLng());
        Point point = gsf.createPoint(coor);
        TsGeometry tsPoint = new TsGeometry(point);
        tsEvent.setTsGeometry(tsPoint);
    }

    public static TsEventForm getTsEventForm(TsEvent tsEvent) {
        TsEventForm tsEventForm = new TsEventForm();
        tsEventForm.setId(tsEvent.getId());
        tsEventForm.setDescription(tsEvent.getDescription());
        tsEventForm.setSummary(tsEvent.getSummary());
        tsEventForm.setSource(tsEvent.getSourceUrl());
        tsEventForm.setWhere(tsEvent.getStreetAddress());
        if (tsEvent.getTsGeometry() != null) {
            tsEventForm.setLat(((Point)tsEvent.getTsGeometry().getGeometry()).getCoordinate().x);
            tsEventForm.setLng(((Point)tsEvent.getTsGeometry().getGeometry()).getCoordinate().y);
        }
        if (tsEvent.getUserTags() != null) {
            String tags = convertToString(tsEvent.getUserTags());
            tsEventForm.setTags(tags);
        }
        return tsEventForm;
    }

    private static String convertToString(List<UserTag> userTags) {
        return join(userTags, ',');
    }
}
