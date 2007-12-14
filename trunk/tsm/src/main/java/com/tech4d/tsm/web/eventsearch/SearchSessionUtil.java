package com.tech4d.tsm.web.eventsearch;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.util.WebUtils;

import com.tech4d.tsm.model.Event;

/**
 * Static Utility class for updating search session objects
 * @author frank
 *
 */
public class SearchSessionUtil {
	@SuppressWarnings("unchecked")
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
