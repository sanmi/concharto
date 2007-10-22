package com.tech4d.tsm.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.web.util.WebUtils;

import com.tech4d.tsm.auth.AuthConstants;
import com.tech4d.tsm.dao.EventDao;
import com.tech4d.tsm.dao.EventDaoHib;

/**
 * The central switchboard
 * {@link org.springframework.web.servlet.mvc.Controller} implementation for the
 * application.
 */
public class SwitchBoardController extends MultiActionController {

    private static final int MAX_RESULTS = 200;
    private EventDao eventDao;

    /**
     * Sets the {@link EventDaoHib} that to which this presentation component delegates
     * in order to perform dao operations.
     *
     * @param eventDao the {@link EventDao} to which this presentation
     *                      component delegates in order to perform dao operations
     */
    public void setEventDao(EventDao eventDao) {
        this.eventDao = eventDao;
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

}
