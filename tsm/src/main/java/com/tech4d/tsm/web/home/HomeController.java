package com.tech4d.tsm.web.home;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;
import org.springframework.web.util.WebUtils;

import com.tech4d.tsm.dao.EventDao;
import com.tech4d.tsm.model.Spotlight;
import com.tech4d.tsm.service.SpotlightService;
import com.tech4d.tsm.web.eventsearch.SearchHelper;

public class HomeController extends SimpleFormController {
    protected final Log log = LogFactory.getLog(getClass());

	private static final int MAX_RECENT_EVENTS = 10;
	public static final String MODEL_TOTAL_EVENTS = "totalEvents";
	public static final String MODEL_RECENT_EVENTS = "recentEvents";
	private static final Object MODEL_SPOTLIGHT_LABEL = "spotlightLabel";
	private static final Object MODEL_SPOTLIGHT_EMBED_LINK = "spotlightEmbedLink";

	private static final String SESSION_SPOTLIGHT_INDEX = "spotlightIndex";
	
	private EventDao eventDao;
	private SpotlightService spotlightService;
	
	public void setEventDao(EventDao eventDao) {
		this.eventDao = eventDao;
	}
	public void setSpotlightService(SpotlightService spotlightService) {
		this.spotlightService = spotlightService;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected ModelAndView showForm(HttpServletRequest request,
			HttpServletResponse response, BindException errors, Map controlModel)
			throws Exception {
		Map model = errors.getModel();
		model.put(MODEL_RECENT_EVENTS, eventDao.findRecent(MAX_RECENT_EVENTS));
		model.put(MODEL_TOTAL_EVENTS, eventDao.getTotalCount());
		setupSpotlight(request, model);
		//clear out the eventSearchForm session if there is one
		WebUtils.setSessionAttribute(request, SearchHelper.SESSION_EVENT_SEARCH_FORM, null);
		return new ModelAndView(getFormView(), model);
	}
	
	/**
	 * Find the next spotlight, add it to the model and save the index in the session so
	 * we can show the next one next time.
	 * 
	 * @param request
	 * @param model
	 */
	@SuppressWarnings("unchecked")
	private void setupSpotlight(HttpServletRequest request, Map model) {
		Integer spotlightIndex = (Integer) WebUtils.getSessionAttribute(request, SESSION_SPOTLIGHT_INDEX);
		if (spotlightIndex == null) {
			spotlightIndex = new Integer(0);
		}
		Spotlight spotlight = spotlightService.getSpotlight(spotlightIndex);
		spotlightIndex++;
		WebUtils.setSessionAttribute(request, SESSION_SPOTLIGHT_INDEX, spotlightIndex);
		if (null != spotlight) {
			model.put(MODEL_SPOTLIGHT_LABEL, formatLabel(spotlight));
			model.put(MODEL_SPOTLIGHT_EMBED_LINK, formatEmbedLabel(spotlight));
		} else {
			log.error("no spotlight");
		}
	}
	private String formatLabel(Spotlight spotlight) {
		String label = StringUtils.replace(spotlight.getLabel(),"[[","<a href='" + spotlight.getLink() + "'>");
		label = StringUtils.replace(label,"]]","</a>");
		return label;
	}
	private String formatEmbedLabel(Spotlight spotlight) {
		return StringUtils.replace(spotlight.getLink(),"search/eventsearch.htm","search/embeddedsearch.htm");
	}
}