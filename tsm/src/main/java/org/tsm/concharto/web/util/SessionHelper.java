/*******************************************************************************
 * ***** BEGIN LICENSE BLOCK *****
 * Version: MPL 1.1
 * 
 * The contents of this file are subject to the Mozilla Public License Version 
 * 1.1 (the "License"); you may not use this file except in compliance with 
 * the License. You may obtain a copy of the License at 
 * http://www.mozilla.org/MPL/
 * 
 * Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 * for the specific language governing rights and limitations under the
 * License.
 * 
 * The Original Code is Concharto.
 * 
 * The Initial Developer of the Original Code is
 * Time Space Map, LLC
 * Portions created by the Initial Developer are Copyright (C) 2007 - 2009
 * the Initial Developer. All Rights Reserved.
 * 
 * Contributor(s):
 * Time Space Map, LLC
 * 
 * ***** END LICENSE BLOCK *****
 ******************************************************************************/
package org.tsm.concharto.web.util;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.util.WebUtils;
import org.tsm.concharto.auth.AuthConstants;
import org.tsm.concharto.dao.WikiTextDao;
import org.tsm.concharto.model.user.Role;
import org.tsm.concharto.model.user.User;
import org.tsm.concharto.web.wiki.WikiConstants;


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
