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
package org.tsm.concharto.web.list;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;
import org.tsm.concharto.dao.EventDao;
import org.tsm.concharto.web.util.CatalogUtil;
import org.tsm.concharto.web.util.DisplayTagHelper;


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
        model.put(DisplayTagHelper.MODEL_TOTAL_RESULTS, Math.round(eventDao.getTotalCount(CatalogUtil.getCatalog(request))));

		return new ModelAndView(this.formView, model);
	}
	
}
