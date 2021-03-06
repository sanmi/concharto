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
package org.tsm.concharto.web.flagevent;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;
import org.springframework.web.util.WebUtils;
import org.tsm.concharto.auth.AuthConstants;
import org.tsm.concharto.auth.AuthHelper;
import org.tsm.concharto.dao.EventDao;
import org.tsm.concharto.dao.FlagDao;
import org.tsm.concharto.dao.UserDao;
import org.tsm.concharto.model.Event;
import org.tsm.concharto.model.Flag;
import org.tsm.concharto.web.changehistory.ChangeHistoryControllerHelper;
import org.tsm.concharto.web.eventsearch.SearchSessionUtil;


/**
 * Allows the user to flag an event to be deleted or moved
 * @author frank
 */
public class FlagEventController extends SimpleFormController {
	private static final String MODEL_REASONS = "reasons";
	private static final String REQUEST_ID = "id";
	//TODO refactor this it is also in EventAdminController
	private static final String MODEL_DISPOSITIONS = "dispositions";  
    private ChangeHistoryControllerHelper changeHistoryControllerHelper;
	private FlagDao flagDao; 
	private EventDao eventDao; 
	private UserDao userDao;
	private int PAGE_SIZE = 10;
	
	public void setChangeHistoryControllerHelper(
			ChangeHistoryControllerHelper changeHistoryControllerHelper) {
		this.changeHistoryControllerHelper = changeHistoryControllerHelper;
	}
	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}
	public void setEventDao(EventDao eventDao) {
		this.eventDao = eventDao;
	}
	public void setFlagDao(FlagDao flagDao) {
		this.flagDao = flagDao;
	}


	@Override
	protected ModelAndView onSubmit(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors)
			throws Exception {
		Long eventId = new Long(request.getParameter(REQUEST_ID));
		String username = (String) WebUtils.getSessionAttribute(request, AuthConstants.SESSION_AUTH_USERNAME);
		FlagEventForm flagEventForm = (FlagEventForm) command;
		
		Event event = saveFlag(flagEventForm, eventId, username);
		//update the event that is in the search results, since we are going back there
		SearchSessionUtil.updateEventInSession(request, event);
		return new ModelAndView(getSuccessView(), errors.getModel());
	}


	private Event saveFlag(FlagEventForm flagEventForm, Long eventId, String username) {
		
		Event event = eventDao.findById(eventId);
		event.setHasUnresolvedFlag(true);
		Flag flag = new Flag(flagEventForm.getComment(), flagEventForm.getReason(),
				AuthHelper.getUsername(), event);
		flagDao.save(flag);
		eventDao.saveOrUpdate(event);
		return event;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected ModelAndView showForm(HttpServletRequest request,
			HttpServletResponse response, BindException errors)
			throws Exception {
		Map model = errors.getModel();
		
    	model.put(MODEL_DISPOSITIONS, Flag.DISPOSITION_CODES);
		model.put(MODEL_REASONS, Flag.REASON_CODES);
    	changeHistoryControllerHelper.doProcess(Event.class, "edit/flagevent.htm", request, model, PAGE_SIZE);
        return new ModelAndView().addAllObjects(model);
	}

}
