package com.tech4d.tsm.web.list;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import com.tech4d.tsm.dao.EventDao;
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

		model.put(MODEL_RECENT_EVENTS, eventDao.findRecent(pageSize, firstResult));
        model.put(DisplayTagHelper.MODEL_PAGESIZE, pageSize);
        model.put(DisplayTagHelper.MODEL_REQUEST_URI, request.getRequestURI());
        model.put(DisplayTagHelper.MODEL_TOTAL_RESULTS, Math.round(eventDao.getTotalCount()));

		return new ModelAndView(this.formView, model);
	}
	
}