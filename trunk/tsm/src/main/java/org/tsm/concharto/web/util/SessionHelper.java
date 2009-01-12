/*******************************************************************************
 * Copyright 2009 Time Space Map, LLC
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
