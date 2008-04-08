package com.tech4d.tsm.web.wiki;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.web.util.WebUtils;

import com.tech4d.tsm.auth.AuthHelper;
import com.tech4d.tsm.dao.UserDao;
import com.tech4d.tsm.dao.WikiTextDao;
import com.tech4d.tsm.model.user.User;
import com.tech4d.tsm.model.user.Notification.NotificationType;
import com.tech4d.tsm.model.wiki.WikiText;
import com.tech4d.tsm.service.NotificationService;
import com.tech4d.tsm.web.filter.NotificationFilter;
import com.tech4d.tsm.web.util.SessionHelper;

/**
 * Web controller for dealing with wiki pages - modeled on Wikipedia  
 *
 */
public class UserWikiPageController extends SimpleFormController {
	private static final String PARAMSUB_USER_TALK = "User_talk:";
	private static final String PARAMSUB_USER = "User:";
	private static final String PARAMSUB_EVENT = "Event:";
	private String PARAM_PAGE="page";
	private WikiTextDao wikiTextDao;
	private UserDao userDao;
	SessionHelper sessionHelper = new SessionHelper();
	NotificationService notificationService;
 
	public void setNotificationService(NotificationService notificationService) {
		this.notificationService = notificationService;
	}

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

	/*
	 * TODO refactor this User, User_talk, Event - it is messy
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected ModelAndView showForm(HttpServletRequest request,
			HttpServletResponse response, BindException errors)
			throws Exception {
		
		if (isEventPage(request)) {
			//ok we simply redirect to the search page
			return new ModelAndView(new RedirectView("/search/eventsearch.htm?_what=%22" + getEvent(request) + "%22"));
		} else {
			Map model = errors.getModel();
			String username = getUsername(request);
			//if the user doesn't exist, it means this is an anonymous user page
			if (null == getUser(request)) {
				model.put("isAnonymous", true);
			}
			
			//If this is my talk page and there are notifications, then we can clear
			//the notifications
			if (iAmWritingOnMyPage(request) && isUserTalkPage(request)) {
				if (null != WebUtils.getSessionAttribute(request, 
							NotificationFilter.SESSION_MESSAGES_PENDING)) {
					//clear the notifications
					notificationService.clearNotifications(username, NotificationType.TALK);
					//clear the session variable
					WebUtils.setSessionAttribute(request, NotificationFilter.SESSION_MESSAGES_PENDING, null);
					//if the page didn't exist before, we should make it so the link shows
					//that the page now exists
					WebUtils.setSessionAttribute(request, WikiConstants.SESSION_MYTALK_EXISTS, true);
				}
			} 
			return new ModelAndView(getFormView(), model);
		}
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

			//If this was the first time, update the session, so the link is no longer 
			//indicates an empty page (e.g. is red)
			if (wikiTextForm.getNewPage()) {
				wikiTextForm.setNewPage(false);
				if (iAmWritingOnMyPage(request)) {
					sessionHelper.setUserInSession(request, getUser(request));
				}
			}
			
			//If someone else is writing on my talk page, notify me - otherwise check and clear
			//page notifications
			if (!iAmWritingOnMyPage(request) && isUserTalkPage(request)) {
				String usernameOfPage = getUsername(request);
				String me = AuthHelper.getUsername();
				notificationService.notifyNewTalk(usernameOfPage, me);
			} 
			
			return super.onSubmit(request, response, command, errors);
		}
	}

	/**
	 * Get the user object from the page title (e.g. "User_talk:sanmi" )
	 * @param request
	 * @return
	 * @throws ServletRequestBindingException
	 */
	private User getUser(HttpServletRequest request) throws ServletRequestBindingException {
		String username = getUsername(request);
		if (null != username) {
			User user = userDao.find(username);
			if (user == null) {
				//this is a hack.  The wiki formatter adds spaces by wikipedia's convention, but our usernames
				//can have spaces.
				//try replacing '_' with spaces.  The wiki formatter puts them there.
				username = StringUtils.replace(username, "_", " ");
				user = userDao.find(username);
			}
			return user;
		}
		return null;
	}
	
	/**
	 * Get the username from the session 
	 * @param request
	 * @return username or null if the user is not logged in
	 * @throws ServletRequestBindingException
	 */
	private String getUsername(HttpServletRequest request) throws ServletRequestBindingException {
		String page = ServletRequestUtils.getStringParameter(request, PARAM_PAGE);
		if ( (StringUtils.contains(page, PARAMSUB_USER)) || 
				(StringUtils.contains(page, PARAMSUB_USER_TALK))) {
			String username = StringUtils.substringAfter(page,":");
			return username;
		}
		return null;
	}

	/**
	 * Determine whether this page is a user talk page
	 * @param request
	 * @return true if this is a talk page
	 * @throws ServletRequestBindingException
	 */
	private boolean isUserTalkPage(HttpServletRequest request) throws ServletRequestBindingException {
		String page = ServletRequestUtils.getStringParameter(request, PARAM_PAGE);
		return StringUtils.contains(page, PARAMSUB_USER_TALK);
	}
	
	/**
	 * Determine whether I am writing on my own page
	 * @param request
	 * @return 
	 * @throws ServletRequestBindingException
	 */
	private boolean iAmWritingOnMyPage(HttpServletRequest request) throws ServletRequestBindingException {
		String myname =  AuthHelper.getUsername();
		//subsitiute '_' for ' ' because of the wiki username convention (a hack)
		myname = StringUtils.replace(myname, " ", "_");

		String theirname = getUsername(request);
		if (null == myname) {
			return false;
		} else {
			return myname.equals(theirname);	
		}
	}

	/**
	 * Determine whether this page is an event page
	 * @param request
	 * @return
	 * @throws ServletRequestBindingException
	 */
	private boolean isEventPage(HttpServletRequest request) throws ServletRequestBindingException {
		String page = ServletRequestUtils.getStringParameter(request, PARAM_PAGE);
		return StringUtils.contains(page, PARAMSUB_EVENT);
	}
	
	private String getEvent(HttpServletRequest request) throws ServletRequestBindingException {
		String page = ServletRequestUtils.getStringParameter(request, PARAM_PAGE);
		String eventName = StringUtils.substringAfter(page, ":");
		//replace underscores with %20
		eventName = StringUtils.replace(eventName, "_", "%20");
		return eventName;
		
	}

}
