package com.tech4d.tsm.web.wiki;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;

import com.tech4d.tsm.dao.UserDao;
import com.tech4d.tsm.dao.WikiTextDao;
import com.tech4d.tsm.model.user.User;
import com.tech4d.tsm.model.wiki.WikiText;
import com.tech4d.tsm.web.util.SessionHelper;

/**
 * Web controller for dealing with wiki pages - modeled on Wikipedia  
 *
 */
public class WikiPageController extends SimpleFormController {
	private static final String PARAMSUB_USER_TALK = "User_talk:";
	private static final String PARAMSUB_USER = "User:";
	private String PARAM_PAGE="page";
	private WikiTextDao wikiTextDao;
	private UserDao userDao;
	SessionHelper sessionHelper = new SessionHelper();

	public void setSessionHelper(SessionHelper sessionHelper) {
		this.sessionHelper = sessionHelper;
	}

	public void setWikiTextDao(WikiTextDao wikiTextDao) {
		this.wikiTextDao = wikiTextDao;
	}

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}


	@Override
	protected Object formBackingObject(HttpServletRequest request)
			throws Exception {
		
		String pageTitle = ServletRequestUtils.getStringParameter(request, PARAM_PAGE);
		if (!StringUtils.isEmpty(pageTitle)) {
			WikiText wikiText = wikiTextDao.find(pageTitle);
			if (null != wikiText) {
				return new WikiTextForm(wikiText, false);
			}
		}
		//ok, we are creating a new wiki page
		WikiText wikiText = new WikiText();
		wikiText.setTitle(pageTitle);
		return new WikiTextForm(wikiText, true);
	}

	@Override
	protected ModelAndView showForm(HttpServletRequest request,
			HttpServletResponse response, BindException errors)
			throws Exception {
		if (null != getUser(request)) {
			return super.showForm(request, response, errors);
		}
		//not good this user doesn't exist - redirect to notfound
		return new ModelAndView("404");
	}

	/**
	 * Get the user object from the page title (e.g. "User_talk:sanmi" )
	 * @param request
	 * @return
	 * @throws ServletRequestBindingException
	 */
	private User getUser(HttpServletRequest request) throws ServletRequestBindingException {
		String page = ServletRequestUtils.getStringParameter(request, PARAM_PAGE);
		if ( (StringUtils.contains(page, PARAMSUB_USER)) || 
				(StringUtils.contains(page, PARAMSUB_USER_TALK))) {
			String username = StringUtils.substringAfter(page,":");
			User user = userDao.find(username);
			return user;
		}
		return null;
	}
		
	@Override
	protected ModelAndView onSubmit(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors)
			throws Exception {

		//save the page
		WikiTextForm wikiTextForm = (WikiTextForm) command;
		if (wikiTextForm.getShowPreview()) {
			return new ModelAndView(getFormView(), errors.getModel());
		}else {
			//!!!!!!!!!!!!!!!!!! TODO refactor this into a single place.  
			//(cut and paste from DiscussController)
			
			//post process macros.  Later we will probably do something fancier for general macros.
			String substituted = SubstitutionMacro.postSignature(request, wikiTextForm.getWikiText().getText());
			wikiTextForm.getWikiText().setText(substituted);
			wikiTextDao.saveOrUpdate(wikiTextForm.getWikiText());
			if (wikiTextForm.getNewPage()) {
				wikiTextForm.setNewPage(false);
				//update the session, so the link is no longer indicates an empty page (e.g. is red)
				sessionHelper.setUserInSession(request, getUser(request));
			}
			return super.onSubmit(request, response, command, errors);
		}
	}

}
