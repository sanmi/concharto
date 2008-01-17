package com.tech4d.tsm.web.spotlight;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;
import org.springframework.web.servlet.view.RedirectView;

import com.tech4d.tsm.dao.SpotlightDao;
import com.tech4d.tsm.model.Spotlight;
import com.tech4d.tsm.service.SpotlightService;

/**
 * Lists all of the spotlights
 * @author frank
 *
 */
public class SpotlightListController extends AbstractController{
	private static final String PARAM_DELETE = "del";
	private static final String PARAM_HIDE = "hide";
	private SpotlightDao spotlightDao;
	private String formView;
	private SpotlightService spotlightService;
	
    public void setSpotlightDao(SpotlightDao spotlightDao) {
		this.spotlightDao = spotlightDao;
	}
	public void setFormView(String formView) {
		this.formView = formView;
	}
	public void setSpotlightService(SpotlightService spotlightService) {
		this.spotlightService = spotlightService;
	}
	@SuppressWarnings("unchecked")
	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		//process delete request
		Long deleteId = ServletRequestUtils.getLongParameter(request, PARAM_DELETE);
		if (null != deleteId) {
			spotlightDao.delete(deleteId);
			//refresh our round robin list
			spotlightService.refresh();
			return new ModelAndView(new RedirectView("/" + formView + ".htm"));
		}
		//process hide request
		Long hideId = ServletRequestUtils.getLongParameter(request, PARAM_HIDE);
		if (null != hideId) {
			Spotlight spotlight = spotlightDao.find(hideId);
			spotlight.setVisible(false);
			spotlightDao.save(spotlight);
			//refresh our round robin list
			spotlightService.refresh();
			return new ModelAndView(new RedirectView("/" + formView + ".htm"));
		}
		
		//get the list
		Map model = new HashMap();
		model.put("spotlights", spotlightDao.findAll());
    	return new ModelAndView(formView, model);
	}
}
