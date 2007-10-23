package com.tech4d.tsm.web;

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
import com.tech4d.tsm.dao.EventDao;
import com.tech4d.tsm.dao.FlagDao;
import com.tech4d.tsm.model.Event;
import com.tech4d.tsm.model.Flag;
import com.tech4d.tsm.util.JSONFormat;

/**
 * The central switchboard
 * {@link org.springframework.web.servlet.mvc.Controller} implementation for the
 * application.
 */
public class SwitchBoardController extends MultiActionController {
    private static final String PARAM_DISPOSITION = "disposition";

	private static final String PARAM_ID = "id";

	private static final String MODEL_DISPOSITIONS = "dispositions";

	private static final Log log = LogFactory.getLog(SwitchBoardController.class);

    private static final int MAX_RESULTS = 200;
    private EventDao eventDao;
    private FlagDao flagDao;

    public void setEventDao(EventDao eventDao) {
        this.eventDao = eventDao;
    }

    public void setFlagDao(FlagDao flagDao) {
		this.flagDao = flagDao;
	}

	public ModelAndView listEvents(HttpServletRequest request, HttpServletResponse response) throws Exception {
        return new ModelAndView().addObject(this.eventDao.findAll(MAX_RESULTS));
    }
    
    public ModelAndView deleteEvent(HttpServletRequest request, HttpServletResponse response) throws Exception {
        
        Long id = ServletRequestUtils.getLongParameter(request, "listid");
        if (id != null) {
            eventDao.delete(id);
        }
        //redirect back to the list
        return new ModelAndView(new RedirectView("/switchboard/listEvents.htm", true));
    }

    public ModelAndView logout(HttpServletRequest request, HttpServletResponse response) throws Exception {
        //remove the token from the session
        WebUtils.setSessionAttribute(request, AuthConstants.AUTH_USERNAME, null);
        WebUtils.setSessionAttribute(request, AuthConstants.AUTH_ROLES, null);
        return new ModelAndView("redirect:/");
    }
    
    public ModelAndView notauthorized(HttpServletRequest request, HttpServletResponse response) throws Exception {
        return new ModelAndView();
    }
        
    @SuppressWarnings("unchecked")
	public Map mapthumbnail(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map model = new HashMap();
		Event event = getEvent(request);
    	if (event != null) {
    		model.put("event", JSONFormat.toJSON(event));
    	}
        return model;
    }

    @SuppressWarnings("unchecked")
	public Map listflags(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map model = new HashMap();
		Event event = getEvent(request);
    	if (event != null) {
    		model.put("event", event);
    	}
    	model.put(MODEL_DISPOSITIONS, Flag.DISPOSITION_CODES);
        return model;
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
        
        Long id = ServletRequestUtils.getLongParameter(request, PARAM_ID);
        String disposition = ServletRequestUtils.getStringParameter(request, PARAM_DISPOSITION);
        if (id != null) {
        	log.debug("user " + WebUtils.getSessionAttribute(request, AuthConstants.AUTH_USERNAME) + 
        			" disposition of flag " + id + " is " + disposition);
            
        }
        Flag flag = flagDao.setFlagDisposition(id, disposition);
        //redirect back to the list
        return new ModelAndView(new RedirectView(request.getContextPath() + "/edit/listflags.htm?id=" + flag.getEvent().getId(), true));
    }
    	
 }
