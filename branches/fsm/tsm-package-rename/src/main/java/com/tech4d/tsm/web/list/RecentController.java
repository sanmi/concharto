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
package com.tech4d.tsm.web.list;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import com.tech4d.tsm.dao.EventDao;
import com.tech4d.tsm.web.util.CatalogUtil;
import com.tech4d.tsm.web.util.DisplayTagHelper;

public class RecentController extends AbstractController {
    private static final int DEFAULT_PAGE_SIZE = 20;
	private static final String MODEL_RECENT_EVENTS = "recentEvents";
    private static final String DISPLAYTAG_TABLE_ID = "event";

	private EventDao eventDao;
    private int pageSize = DEFAULT_PAGE_SIZE;
    private String formView;

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	public void setEventDao(EventDao eventDao) {
		this.eventDao = eventDao;
	}
	public void setFormView(String formView) {
		this.formView = formView;
	}
	

	@SuppressWarnings("unchecked")
	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Map model = new HashMap();

        Integer firstResult = DisplayTagHelper.getFirstRecord(request, DISPLAYTAG_TABLE_ID, pageSize);

		model.put(MODEL_RECENT_EVENTS, eventDao.findRecent(CatalogUtil.getCatalog(request), pageSize, firstResult));
        model.put(DisplayTagHelper.MODEL_PAGESIZE, pageSize);
        model.put(DisplayTagHelper.MODEL_REQUEST_URI, request.getRequestURI());
        model.put(DisplayTagHelper.MODEL_TOTAL_RESULTS, Math.round(eventDao.getTotalCount()));

		return new ModelAndView(this.formView, model);
	}
	
}
