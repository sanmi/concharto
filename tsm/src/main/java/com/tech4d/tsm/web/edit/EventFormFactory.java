package com.tech4d.tsm.web.edit;

import com.tech4d.tsm.model.Event;
import com.tech4d.tsm.model.geometry.TsGeometry;
import com.tech4d.tsm.util.GeometryType;
import com.vividsolutions.jts.geom.Geometry;

public class EventFormFactory {

    public static Event createEvent() {
        return  new Event();
    }

    public static Event createEvent(EventForm eventForm) {
        return updateEvent(createEvent(), eventForm);
    }

    public static Event updateEvent(Event event, EventForm eventForm) {
        event.setId(eventForm.getId());
        event.setDescription(eventForm.getDescription());
        event.setSummary(eventForm.getSummary());
        event.setSource(eventForm.getSource());
        event.setWhere(eventForm.getWhere());
        event.setTsGeometry(new TsGeometry(eventForm.getGeometry()));
        event.setUserTagsAsString(eventForm.getTags());
        event.setWhen(eventForm.getWhen());
        event.setZoomLevel(eventForm.getZoomLevel());
        event.setMapType(eventForm.getMapType());
        return event;
    }

    public static EventForm getEventForm(Event event) {
        EventForm eventForm = new EventForm();
        eventForm.setId(event.getId());
        eventForm.setDescription(event.getDescription());
        eventForm.setSummary(event.getSummary());
        eventForm.setSource(event.getSource());
        eventForm.setWhere(event.getWhere());
        eventForm.setWhen(event.getWhen());
        eventForm.setZoomLevel(event.getZoomLevel());
        eventForm.setMapType(event.getMapType());
        if (event.getTsGeometry() != null) {
            Geometry geom = event.getTsGeometry().getGeometry();
            eventForm.setGeometry(geom);
            eventForm.setGeometryType(GeometryType.getGeometryType(geom));
        }
        if (event.getUserTags() != null) {
            String tags = event.getUserTagsAsString();
            eventForm.setTags(tags);
        }
        return eventForm;
    }

}
