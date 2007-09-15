package com.tech4d.tsm.web;

import com.tech4d.tsm.model.TsEvent;
import com.tech4d.tsm.model.UserTag;
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

    public static TsEventForm getTsEventForm(TsEvent tsEvent) {
        TsEventForm tsEventForm = new TsEventForm();
        tsEventForm.setId(tsEvent.getId());
        tsEventForm.setDescription(tsEvent.getDescription());
        tsEventForm.setSummary(tsEvent.getSummary());
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
