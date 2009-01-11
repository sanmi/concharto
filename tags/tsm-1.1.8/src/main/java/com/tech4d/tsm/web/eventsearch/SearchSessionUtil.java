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
