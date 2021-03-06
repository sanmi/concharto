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
package org.tsm.concharto.web.changehistory;

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
import org.tsm.concharto.auth.AuthConstants;
import org.tsm.concharto.dao.EventDao;
import org.tsm.concharto.dao.FlagDao;
import org.tsm.concharto.model.Event;
import org.tsm.concharto.model.Flag;
import org.tsm.concharto.web.SwitchBoardController;
import org.tsm.concharto.web.eventsearch.SearchSessionUtil;


public class EventAdminController extends MultiActionController{
	private static final String PARAM_DISPOSITION = "disposition";
	private static final String PARAM_ID = "id";
	private static final Log log = LogFactory.getLog(SwitchBoardController.class);

    private EventDao eventDao;
    private FlagDao flagDao;
    private String detailsView;

    public void setEventDao(EventDao eventDao) {
        this.eventDao = eventDao;
    }

    public void setFlagDao(FlagDao flagDao) {
		this.flagDao = flagDao;
	}

	public void setDetailsView(String detailsView) {
		this.detailsView = detailsView;
	}
	
    private ModelAndView searchModelAndView(HttpServletRequest request) {
    	return new ModelAndView(new RedirectView(request.getContextPath() + "/search/eventsearch.htm", true));
    }

    public ModelAndView flagdisposition(HttpServletRequest request, HttpServletResponse response) throws Exception {
        //TODO make this into a full fledged controller so we can add disposition comments, etc
        Long id = ServletRequestUtils.getLongParameter(request, PARAM_ID);
        String disposition = ServletRequestUtils.getStringParameter(request, PARAM_DISPOSITION);
        if (id != null) {
        	log.info("user " + WebUtils.getSessionAttribute(request, AuthConstants.SESSION_AUTH_USERNAME) + 
        			" disposition of flag " + id + " is " + disposition);
        }
        Flag flag = flagDao.setFlagDisposition(id, disposition);
    	Event event = flag.getEvent();
    	
    	if (Flag.DISPOSITION_DELETED.equals(disposition)) {
        	eventDao.delete(flag.getEvent());
    		//update the event that is in the search results, since we are going back there
    		SearchSessionUtil.deleteEventInSession(request, flag.getEvent().getId());
            //redirect back to search
            return searchModelAndView(request);
        } else {
            if (Flag.DISPOSITION_REMOVED.equals(disposition)) {
            	//if REMOVED, then we need to remove the event by making it invisible
            	event.setVisible(false);
            } else if (Flag.DISPOSITION_FLAG_SPAM.equals(disposition)) {
            	//clear the comment
            	flagDao.delete(flag);
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
    		SearchSessionUtil.updateEventInSession(request, event);
        	eventDao.save(event);
        }
        
        //redirect back to the list
        return new ModelAndView(new RedirectView(request.getContextPath() + '/' + this.detailsView + ".htm?id=" + flag.getEvent().getId(), true));
    }
    

}
