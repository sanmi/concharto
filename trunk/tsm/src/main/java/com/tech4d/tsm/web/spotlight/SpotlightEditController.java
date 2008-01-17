package com.tech4d.tsm.web.spotlight;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;

import com.tech4d.tsm.auth.AuthHelper;
import com.tech4d.tsm.dao.SpotlightDao;
import com.tech4d.tsm.dao.UserDao;
import com.tech4d.tsm.model.Spotlight;
import com.tech4d.tsm.model.user.User;
import com.tech4d.tsm.service.SpotlightService;

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
		//if this is the first time, then we should add the user
		if (spotlight.getId() == null) {
			User user = userDao.find(AuthHelper.getUsername());
			spotlight.setAddedByUser(user);
		}
		spotlightDao.save(spotlight);
		//refress the round robbin service with our new stuff
		spotlightService.refresh();
		
		return new ModelAndView(getSuccessView());
	}
}
