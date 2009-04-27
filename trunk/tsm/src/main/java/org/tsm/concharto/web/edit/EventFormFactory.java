/*******************************************************************************
 * Copyright 2009 Time Space Map, LLC
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package org.tsm.concharto.web.edit;

import info.bliki.wiki.model.WikiModel;

import javax.servlet.http.HttpServletRequest;

import org.tsm.concharto.model.Event;
import org.tsm.concharto.model.PositionalAccuracy;
import org.tsm.concharto.model.geometry.TsGeometry;
import org.tsm.concharto.util.GeometryType;
import org.tsm.concharto.web.wiki.WikiModelFactory;

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
        event.setSequence(eventForm.getSequence());
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
        eventForm.setSequence(event.getSequence());
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
