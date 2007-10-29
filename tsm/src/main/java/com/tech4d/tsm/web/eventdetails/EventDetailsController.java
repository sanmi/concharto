package com.tech4d.tsm.web.eventdetails;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.web.util.WebUtils;

import com.tech4d.tsm.auth.AuthConstants;
import com.tech4d.tsm.auth.AuthHelper;
import com.tech4d.tsm.dao.AuditEntryDao;
import com.tech4d.tsm.dao.EventDao;
import com.tech4d.tsm.dao.FlagDao;
import com.tech4d.tsm.model.Event;
import com.tech4d.tsm.model.Flag;
import com.tech4d.tsm.service.RevertEventService;
import com.tech4d.tsm.web.SwitchBoardController;
import com.tech4d.tsm.web.changehistory.ChangeHistoryControllerHelper;

public class EventDetailsController extends MultiActionController{
	private static final String MODEL_EVENT = "event";
	private static final String PARAM_DISPOSITION = "disposition";
	private static final String PARAM_ID = "id";
	private static final String MODEL_DISPOSITIONS = "dispositions";
	private static final Log log = LogFactory.getLog(SwitchBoardController.class);

	private static final String PARAM_TO_REVISION = "toRev";
    private EventDao eventDao;
    private FlagDao flagDao;
    private RevertEventService revertEventService;
    private ChangeHistoryControllerHelper changeHistoryControllerHelper = new ChangeHistoryControllerHelper();

    public void setEventDao(EventDao eventDao) {
        this.eventDao = eventDao;
    }

    public void setFlagDao(FlagDao flagDao) {
		this.flagDao = flagDao;
	}

    public void setAuditEntryDao(AuditEntryDao auditEntryDao) {
        changeHistoryControllerHelper.setAuditEntryDao(auditEntryDao);
    }

	public void setRevertEventService(RevertEventService revertEventService) {
		this.revertEventService = revertEventService;
	}

    @SuppressWarnings("unchecked")
	public ModelAndView eventdetails(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map model = new HashMap();
		Event event = getEvent(request);
    	if (event == null) {
            return searchModelAndView(request);
    	}
    	//Regular users don't get to see it if it has been removed by an admin
    	else if (!AuthHelper.isUserAnAdmin() && !(event.isVisible() == null) && !event.isVisible()) {
    		return searchModelAndView(request);
    	}
		model.put(MODEL_EVENT, event);
    	model.put(MODEL_DISPOSITIONS, Flag.DISPOSITION_CODES);
    	//TODO fix this to get it from the app context 
    	changeHistoryControllerHelper.doProcess("edit/eventdetails.htm", request, model);
        return new ModelAndView().addAllObjects(model);
    }
    
    private ModelAndView searchModelAndView(HttpServletRequest request) {
    	return new ModelAndView(new RedirectView(request.getContextPath() + "/search/eventsearch.htm", true));
    }
    
    private Event getEvent(HttpServletRequest request) {
    	String id = request.getParameter(PARAM_ID);
    	if (!StringUtils.isEmpty(id)) {
        	Long eventId = new Long(id);
           	return eventDao.findById(eventId);
    	}
    	return null;
    }

    public ModelAndView flagdisposition(HttpServletRequest request, HttpServletResponse response) throws Exception {
        //TODO make this into a full fledged controller so we can add disposition comments, etc
        Long id = ServletRequestUtils.getLongParameter(request, PARAM_ID);
        String disposition = ServletRequestUtils.getStringParameter(request, PARAM_DISPOSITION);
        if (id != null) {
        	log.debug("user " + WebUtils.getSessionAttribute(request, AuthConstants.SESSION_AUTH_USERNAME) + 
        			" disposition of flag " + id + " is " + disposition);
        }
        Flag flag = flagDao.setFlagDisposition(id, disposition);
    	Event event = flag.getEvent();
    	if (Flag.DISPOSITION_DELETED.equals(disposition)) {
        	eventDao.delete(flag.getEvent());
            //redirect back to search
            return searchModelAndView(request);
        } else {
            if (Flag.DISPOSITION_REMOVED.equals(disposition)) {
            	//if REMOVED, then we need to remove the event by making it invisible
            	event.setVisible(false);
            } else if (null == disposition) {
            	//we are being asked to reopen it.  TODO fix this kludge!
            	event.setVisible(true);
            }  
            //if there are unresolved flags, set this on the event
            boolean unresolved = false;
            for (Flag otherFlag : event.getFlags()) {
            	if (otherFlag.getId() != flag.getId()) {
                	if (StringUtils.isEmpty(otherFlag.getDisposition())) {
                		unresolved = true;
                	} 
            	} else {
            		if (StringUtils.isEmpty(flag.getDisposition())) {
            			unresolved = true;
            		}
            	}
            }
            event.setHasUnresolvedFlag(unresolved);
        	eventDao.save(event);
        }
        
        //redirect back to the list
        return new ModelAndView(new RedirectView(request.getContextPath() + "/edit/eventdetails.htm?id=" + flag.getEvent().getId(), true));
    }
    
    public ModelAndView undoevent(HttpServletRequest request, HttpServletResponse response) throws Exception {
    	//TODO move this into another controller
    	Long id = ServletRequestUtils.getLongParameter(request, PARAM_ID);
    	Integer revision = ServletRequestUtils.getIntParameter(request, PARAM_TO_REVISION);

    	if ((id != null) && (revision != null)) {
    		revertEventService.revertToRevision(revision, id);
    	}
        //redirect back to the list
        return new ModelAndView(new RedirectView(request.getContextPath() + "/edit/eventdetails.htm?id=" + id, true));
    }
    	

}
