package com.tech4d.tsm.web;

import com.tech4d.tsm.model.TsEvent;
import com.tech4d.tsm.model.geometry.TsGeometry;
import com.tech4d.tsm.util.GeometryType;
import com.vividsolutions.jts.geom.Geometry;

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
        tsEvent.setWhere(tsEventForm.getWhere());
        tsEvent.setTsGeometry(new TsGeometry(tsEventForm.getGeometry()));
        tsEvent.setUserTagsAsString(tsEventForm.getTags());
        tsEvent.setWhen(tsEventForm.getWhen());
        tsEvent.setZoomLevel(tsEventForm.getZoomLevel());
        tsEvent.setMapType(tsEventForm.getMapType());
        return tsEvent;
    }

    public static TsEventForm getTsEventForm(TsEvent tsEvent) {
        TsEventForm tsEventForm = new TsEventForm();
        tsEventForm.setId(tsEvent.getId());
        tsEventForm.setDescription(tsEvent.getDescription());
        tsEventForm.setSummary(tsEvent.getSummary());
        tsEventForm.setSource(tsEvent.getSourceUrl());
        tsEventForm.setWhere(tsEvent.getWhere());
        tsEventForm.setWhen(tsEvent.getWhen());
        tsEventForm.setZoomLevel(tsEvent.getZoomLevel());
        tsEventForm.setMapType(tsEvent.getMapType());
        if (tsEvent.getTsGeometry() != null) {
            Geometry geom = tsEvent.getTsGeometry().getGeometry();
            tsEventForm.setGeometry(geom);
            tsEventForm.setGeometryType(GeometryType.getGeometryType(geom));
        }
        if (tsEvent.getUserTags() != null) {
            String tags = tsEvent.getUserTagsAsString();
            tsEventForm.setTags(tags);
        }
        return tsEventForm;
    }

}
