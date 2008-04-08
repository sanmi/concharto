package com.tech4d.tsm.web.util;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.util.WebUtils;

import com.tech4d.tsm.auth.AuthConstants;
import com.tech4d.tsm.dao.WikiTextDao;
import com.tech4d.tsm.model.user.Role;
import com.tech4d.tsm.model.user.User;
import com.tech4d.tsm.web.wiki.WikiConstants;

public class SessionHelper {
    private WikiTextDao wikiTextDao;

	public void setWikiTextDao(WikiTextDao wikiTextDao) {
		this.wikiTextDao = wikiTextDao;
	}

	public void setUserInSession(HttpServletRequest request, User user) {
		if (user == null) {
			user = makeAnonymousUser(request);
		}
		WebUtils.setSessionAttribute(request, AuthConstants.SESSION_AUTH_USERNAME, user.getUsername());
		WebUtils.setSessionAttribute(request, AuthConstants.SESSION_AUTH_ROLES, makeRoles(user.getRoles()));
        //put some flags to indicate whether the user and talk pages have been created
		//wiki convention is that we replace ' ' with '_' in user names
		String username = user.getUsername();
		username = StringUtils.replace(username, " ", "_");
		WebUtils.setSessionAttribute(request, WikiConstants.SESSION_MYPAGE_EXISTS, 
				wikiTextDao.exists(WikiConstants.PREFIX_USER + username));
		WebUtils.setSessionAttribute(request, WikiConstants.SESSION_MYTALK_EXISTS, 
				wikiTextDao.exists(WikiConstants.PREFIX_USER_TALK + user.getUsername()));
	}

    private String makeRoles(List<Role> roles) {
        StringBuffer roleStr = new StringBuffer();
        if (roles != null) {
            for (Role role : roles) {
                roleStr.append(role.getName()).append(" ");
            }
        }
        return roleStr.toString();
    }

	private User makeAnonymousUser(HttpServletRequest request) {
		User user = new User();
		user.setUsername(request.getRemoteAddr());
		List<Role> roles = new ArrayList<Role>();
		roles.add(Role.ROLE_EDIT);
		roles.add(Role.ROLE_ANONYMOUS);
		user.setRoles(roles);
		return user;
	}
}
