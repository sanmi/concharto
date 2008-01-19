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
		model.put("spotlights", spotlightDao.findAll());
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
