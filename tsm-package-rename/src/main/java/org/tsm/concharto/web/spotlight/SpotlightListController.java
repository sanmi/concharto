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

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;
import org.springframework.web.servlet.view.RedirectView;
import org.tsm.concharto.dao.SpotlightDao;
import org.tsm.concharto.model.Spotlight;
import org.tsm.concharto.service.SpotlightService;
import org.tsm.concharto.web.util.CatalogUtil;


/**
 * Lists all of the spotlights
 * @author frank
 *
 */
public class SpotlightListController extends AbstractController{
	private static final String PARAM_DELETE = "del";
	private static final String PARAM_HIDE = "hide";
	private static final String PARAM_SHOW = "show";
	private SpotlightDao spotlightDao;
	private String formView;
	private String redirectView;
	private SpotlightService spotlightService;
	
    public void setSpotlightDao(SpotlightDao spotlightDao) {
		this.spotlightDao = spotlightDao;
	}
	public void setFormView(String formView) {
		this.formView = formView;
		redirectView = "/" + formView + ".htm";
	}
	public void setSpotlightService(SpotlightService spotlightService) {
		this.spotlightService = spotlightService;
	}
	@SuppressWarnings("unchecked")
	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		//process delete request
		Long id; ;
		if (null != (id = ServletRequestUtils.getLongParameter(request, PARAM_DELETE))) {
			spotlightDao.delete(id);
			return refreshAndRedirect();
		} else if (null != (id = ServletRequestUtils.getLongParameter(request, PARAM_HIDE))){
			setVisibility(id, false);
			return refreshAndRedirect();
		} else if (null != (id = ServletRequestUtils.getLongParameter(request, PARAM_SHOW))){
			setVisibility(id, true);
			return refreshAndRedirect();
		}
		
		//get the list
		Map model = new HashMap();
		model.put("spotlights", spotlightDao.findAll(CatalogUtil.getCatalog(request)));
    	return new ModelAndView(formView, model);
	}
	
	private ModelAndView refreshAndRedirect() {
		//refresh our round robin list
		spotlightService.refresh();
		return new ModelAndView(new RedirectView(redirectView));
	}
	
	private void setVisibility(Long hideId, boolean visibility) {
		Spotlight spotlight = spotlightDao.find(hideId);
		spotlight.setVisible(visibility);
		spotlightDao.save(spotlight);
	}
}
