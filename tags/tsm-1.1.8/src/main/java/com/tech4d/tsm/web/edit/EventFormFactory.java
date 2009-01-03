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
package com.tech4d.tsm.web.edit;

import info.bliki.wiki.model.WikiModel;

import javax.servlet.http.HttpServletRequest;

import com.tech4d.tsm.model.Event;
import com.tech4d.tsm.model.PositionalAccuracy;
import com.tech4d.tsm.model.geometry.TsGeometry;
import com.tech4d.tsm.util.GeometryType;
import com.tech4d.tsm.web.wiki.WikiModelFactory;
import com.vividsolutions.jts.geom.Geometry;

public class EventFormFactory {

    public static Event createEvent() {
        return  new Event();
    }

    public static Event createEvent(EventForm eventForm) {
        return updateEvent(createEvent(), eventForm);
    }

    public static Event updateEvent(Event event, EventForm eventForm) {
        event.setId(eventForm.getEventId());
        event.setDescription(eventForm.getDescription());
        event.setSummary(eventForm.getSummary());
        event.setSource(eventForm.getSource());
        event.setWhere(eventForm.getWhere());
        event.setTsGeometry(new TsGeometry(eventForm.getGeometry()));
        event.setUserTagsAsString(eventForm.getTags());
        event.setWhen(eventForm.getWhen());
        event.setZoomLevel(eventForm.getZoomLevel());
        event.setMapType(eventForm.getMapType());
        PositionalAccuracy pa = new PositionalAccuracy();
        pa.setId(eventForm.getPositionalAccuracy());
        event.setPositionalAccuracy(pa);
        return event;
    }

    public static EventForm getEventForm(Event event, HttpServletRequest request) {
        EventForm eventForm = new EventForm();

        eventForm.setEventId(event.getId());
        eventForm.setDescription(event.getDescription());
        eventForm.setSummary(event.getSummary());
        eventForm.setSource(event.getSource());
        eventForm.setWhere(event.getWhere());
        eventForm.setWhen(event.getWhen());
        eventForm.setZoomLevel(event.getZoomLevel());
        eventForm.setMapType(event.getMapType());
        if (null != event.getPositionalAccuracy()) {
            eventForm.setPositionalAccuracy(event.getPositionalAccuracy().getId());
        }
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
    
    public static void renderWiki(Event event, HttpServletRequest request) {
        WikiModel wikiModel = WikiModelFactory.newWikiModel(request);
        //DEBUG clean up other stuff
    	event.setDescription(wikiModel.render(event.getDescription()));
        event.setSource(wikiModel.render(event.getSource()));
    	
    }

}
