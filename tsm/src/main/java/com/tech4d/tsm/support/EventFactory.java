package com.tech4d.tsm.support;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.tech4d.tsm.model.Event;
import com.tech4d.tsm.model.UserTag;
import com.tech4d.tsm.model.geometry.Feature;
import com.tech4d.tsm.web.EventForm;

public class EventFactory {

    public static Event createEvent() {
        Event event = new Event();
        Feature feature = new Feature();
        event.setFeature(feature);
        return event;
    }

    public static Event createEvent(EventForm eventForm) {
        return updateEvent(createEvent(), eventForm);
    }

    public static Event updateEvent(Event event, EventForm eventForm) {
        event.setId(eventForm.getId());
        event.getFeature().setDescription(eventForm.getDescription());
        event.getFeature().setSummary(eventForm.getSummary());

        // a dirty check so we don't have to save each time
        String originalTags = convertToString(event.getUserTags());
        boolean dirty = false;
        if (originalTags == null) {
            if (eventForm.getTags() != null) {
                dirty = true;
            } 
        } else {
            if (!originalTags.equals(eventForm.getTags())) {
                dirty = true;
            }
        }
        if (dirty) {
            String[] tags = StringUtils.split(eventForm.getTags(), ",");
            List<UserTag> userTags = new ArrayList<UserTag>();
            for (String tag : tags) {
                userTags.add(new UserTag(tag));
            }
            event.setUserTags(userTags);
        }
        return event;
    }

    public static EventForm getEventForm(Event event) {
        EventForm eventForm = new EventForm();
        eventForm.setId(event.getId());
        if (event.getFeature() != null) { // should never happen
            eventForm.setDescription(event.getFeature().getDescription());
            eventForm.setSummary(event.getFeature().getSummary());
        }
        if (event.getUserTags() != null) {
            String tags = convertToString(event.getUserTags());
            eventForm.setTags(tags);
        }
        return eventForm;
    }

    private static String convertToString(List<UserTag> userTags) {
        String tags = StringUtils.join(userTags, ',');
        return tags;
    }
}
