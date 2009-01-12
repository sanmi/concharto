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
package org.tsm.concharto.web.spotlight;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;
import org.tsm.concharto.auth.AuthHelper;
import org.tsm.concharto.dao.SpotlightDao;
import org.tsm.concharto.dao.UserDao;
import org.tsm.concharto.model.Spotlight;
import org.tsm.concharto.model.user.User;
import org.tsm.concharto.service.SpotlightService;
import org.tsm.concharto.web.util.CatalogUtil;


/**
 * Edit or add spotlights
 * @author frank
 *
 */
public class SpotlightEditController extends SimpleFormController{
	private SpotlightDao spotlightDao;
	private UserDao userDao;
	private SpotlightService spotlightService;
	
    public void setSpotlightDao(SpotlightDao spotlightDao) {
		this.spotlightDao = spotlightDao;
	}
	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}
	public void setSpotlightService(SpotlightService spotlightService) {
		this.spotlightService = spotlightService;
	}

	private static final String PARAM_ID = "id";

	/**
	 * Get the spotlight from the given id.  If there is no id, we return a blank one.
	 */
	@Override
	protected Object formBackingObject(HttpServletRequest request)
			throws Exception {
		Spotlight spotlight = null;
		Long id = ServletRequestUtils.getLongParameter(request, PARAM_ID);
		if (id != null) {
			spotlight = spotlightDao.find(id);
		} 
		if (null == spotlight) {
			spotlight = new Spotlight();
			spotlight.setVisible(true);
		}
		return spotlight;
	}

	@Override
	protected ModelAndView onSubmit(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors)
			throws Exception {
		
		//save or update it
		Spotlight spotlight = (Spotlight) command;
		//if this is the first time, then we should add the user and catalog
		if (spotlight.getId() == null) {
			User user = userDao.find(AuthHelper.getUsername());
			spotlight.setAddedByUser(user);
			spotlight.setCatalog(CatalogUtil.getCatalog(request));
		}
		spotlightDao.save(spotlight);
		//refresh the round robbin service with our new stuff
		spotlightService.refresh();
		
		return new ModelAndView(getSuccessView());
	}
}
