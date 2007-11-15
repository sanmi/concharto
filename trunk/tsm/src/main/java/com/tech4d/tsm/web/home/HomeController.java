package com.tech4d.tsm.web.home;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;

import com.tech4d.tsm.dao.EventDao;

public class HomeController extends SimpleFormController {
	private static final int MAX_RECENT_EVENTS = 10;
	public static final String MODEL_TOTAL_EVENTS = "totalEvents";
	public static final String MODEL_RECENT_EVENTS = "recentEvents";
	
	EventDao eventDao;
	
	public void setEventDao(EventDao eventDao) {
		this.eventDao = eventDao;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected ModelAndView showForm(HttpServletRequest request,
			HttpServletResponse response, BindException errors, Map controlModel)
			throws Exception {
		Map model = errors.getModel();
		model.put(MODEL_RECENT_EVENTS, eventDao.findRecent(MAX_RECENT_EVENTS));
		model.put(MODEL_TOTAL_EVENTS, eventDao.getTotalCount());
		return new ModelAndView(getFormView(), model);
	}

}