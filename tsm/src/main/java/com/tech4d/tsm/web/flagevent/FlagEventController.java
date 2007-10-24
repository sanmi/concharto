package com.tech4d.tsm.web.flagevent;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;
import org.springframework.web.util.WebUtils;

import com.tech4d.tsm.auth.AuthConstants;
import com.tech4d.tsm.dao.EventDao;
import com.tech4d.tsm.dao.FlagDao;
import com.tech4d.tsm.dao.UserDao;
import com.tech4d.tsm.model.Event;
import com.tech4d.tsm.model.Flag;
import com.tech4d.tsm.model.User;

/**
 * Allows the user to flag an event to be deleted or moved
 * @author frank
 */
public class FlagEventController extends SimpleFormController {
	private static final String MODEL_REASONS = "reasons";
	private static final String REQUEST_ID = "id";
	private static final String MODEL_EVENT = "event";
	FlagDao flagDao; 
	EventDao eventDao; 
	UserDao userDao;
	
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
		
		saveFlag(flagEventForm, eventId, username);
		return new ModelAndView(getSuccessView(), errors.getModel());
	}

	private void saveFlag(FlagEventForm flagEventForm, Long eventId, String username) {
		Event event = eventDao.findById(eventId);
		User user = userDao.find(username);
		Flag flag = new Flag(flagEventForm.getComment(), flagEventForm.getReason(),
				user, event);
		flagDao.save(flag);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected ModelAndView showForm(HttpServletRequest request,
			HttpServletResponse response, BindException errors)
			throws Exception {
		Long eventId = new Long(request.getParameter(REQUEST_ID));
		Map model = errors.getModel();
		
		Event event = eventDao.findById(eventId);
		model.put(MODEL_EVENT, event);

		model.put(MODEL_REASONS, Flag.REASON_CODES);
		return new ModelAndView(getFormView(), model);
	}

}
