package com.tech4d.tsm.web.flagevent;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;
import org.springframework.web.util.WebUtils;

import com.tech4d.tsm.auth.AuthConstants;
import com.tech4d.tsm.dao.AuditEntryDao;
import com.tech4d.tsm.dao.EventDao;
import com.tech4d.tsm.dao.FlagDao;
import com.tech4d.tsm.dao.UserDao;
import com.tech4d.tsm.model.Event;
import com.tech4d.tsm.model.Flag;
import com.tech4d.tsm.model.User;
import com.tech4d.tsm.web.changehistory.ChangeHistoryControllerHelper;
import com.tech4d.tsm.web.eventsearch.SearchSessionUtil;

/**
 * Allows the user to flag an event to be deleted or moved
 * @author frank
 */
public class FlagEventController extends SimpleFormController {
	private static final String MODEL_REASONS = "reasons";
	private static final String REQUEST_ID = "id";
	private static final String MODEL_EVENT = "event";
	//TODO refactor this it is also in EventDetailsController
	private static final String MODEL_DISPOSITIONS = "dispositions";  
    private ChangeHistoryControllerHelper changeHistoryControllerHelper = new ChangeHistoryControllerHelper();
	private FlagDao flagDao; 
	private EventDao eventDao; 
	private UserDao userDao;
	
	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}
	public void setEventDao(EventDao eventDao) {
		this.eventDao = eventDao;
	}
	public void setFlagDao(FlagDao flagDao) {
		this.flagDao = flagDao;
	}
    public void setAuditEntryDao(AuditEntryDao auditEntryDao) {
        changeHistoryControllerHelper.setAuditEntryDao(auditEntryDao);
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
		User user = userDao.find(username);
		Flag flag = new Flag(flagEventForm.getComment(), flagEventForm.getReason(),
				user, event);
		flagDao.save(flag);
		eventDao.saveOrUpdate(event);
		return event;
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
    	model.put(MODEL_DISPOSITIONS, Flag.DISPOSITION_CODES);
		model.put(MODEL_REASONS, Flag.REASON_CODES);
    	changeHistoryControllerHelper.doProcess("edit/flagevent.htm", request, model);
        return new ModelAndView().addAllObjects(model);
	}

}
