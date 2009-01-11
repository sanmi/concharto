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
package com.tech4d.tsm.web.flagevent;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;
import org.springframework.web.util.WebUtils;

import com.tech4d.tsm.auth.AuthConstants;
import com.tech4d.tsm.auth.AuthHelper;
import com.tech4d.tsm.dao.EventDao;
import com.tech4d.tsm.dao.FlagDao;
import com.tech4d.tsm.dao.UserDao;
import com.tech4d.tsm.model.Event;
import com.tech4d.tsm.model.Flag;
import com.tech4d.tsm.web.changehistory.ChangeHistoryControllerHelper;
import com.tech4d.tsm.web.eventsearch.SearchSessionUtil;

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
