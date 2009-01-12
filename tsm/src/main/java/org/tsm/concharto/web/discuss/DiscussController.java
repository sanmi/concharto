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
package org.tsm.concharto.web.discuss;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;
import org.tsm.concharto.dao.EventDao;
import org.tsm.concharto.model.Event;
import org.tsm.concharto.model.wiki.WikiText;
import org.tsm.concharto.web.wiki.SubstitutionMacro;


public class DiscussController extends SimpleFormController {
	
	private static final String PARAM_ID = "id"; 
	private EventDao eventDao;
	
	public void setEventDao(EventDao eventDao) {
		this.eventDao = eventDao;
	}

	@Override
	protected Object formBackingObject(HttpServletRequest request)
			throws Exception {
        Long id = ServletRequestUtils.getLongParameter(request, PARAM_ID);
        Event event = eventDao.findById(id);
        if (null == event.getDiscussion()) {
        	//there is no existing discussion, so add a blank one
        	event.setDiscussion(new WikiText());
        }
        return new WikiTextForm(event);
        
	}


	@SuppressWarnings("unchecked")
	@Override
	protected ModelAndView showForm(HttpServletRequest request,
			HttpServletResponse response, BindException errors, Map controlModel)
			throws Exception {
		return new ModelAndView(getFormView(), errors.getModel());
	}

	@Override
	protected ModelAndView onSubmit(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors)
			throws Exception {
		
		//save the wikitext
		WikiTextForm discussion = (WikiTextForm) command;
		if (discussion.getShowPreview()) {
			return new ModelAndView(getFormView(), errors.getModel());
		}else {
			//post process macros.  Later we will probably do something fancier for general macros.
			String substituted = SubstitutionMacro.postSignature(request, discussion.getEvent().getDiscussion().getText());
			discussion.getEvent().getDiscussion().setText(substituted);
			//this isn't new we just need to save the discussion
			eventDao.saveOrUpdate(discussion.getEvent());
			return super.onSubmit(request, response, command, errors);
		}
	}
	
	
}
