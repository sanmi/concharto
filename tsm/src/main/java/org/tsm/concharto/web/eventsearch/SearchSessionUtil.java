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
package org.tsm.concharto.web.eventsearch;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.util.WebUtils;
import org.tsm.concharto.model.Event;


/**
 * Static Utility class for updating search session objects
 * @author frank
 *
 */
public class SearchSessionUtil {
	public static void updateEventInSession(HttpServletRequest request, Event event) {
		updateEventInSession(request, event.getId(), event);
	}

	public static void deleteEventInSession(HttpServletRequest request, long eventId) {
		updateEventInSession(request, eventId, null);
	}
	
	@SuppressWarnings("unchecked")
	private static void updateEventInSession(HttpServletRequest request, Long eventId, Event event) {
		List<Event> events = (List<Event>) 
		WebUtils.getSessionAttribute(request, SearchHelper.SESSION_EVENT_SEARCH_RESULTS);
		if (events != null) {
			for (int i=0; i<events.size(); i++) {
				if (events.get(i).getId().equals(eventId)) {
					if (event == null) {
						//the event was deleted, so we should remove it here
						events.remove(i);
					} else {
						//replace it
						events.set(i, event);
					}
				}
			}
			WebUtils.setSessionAttribute(request, SearchHelper.SESSION_EVENT_SEARCH_RESULTS, events);
		}
	}

}
