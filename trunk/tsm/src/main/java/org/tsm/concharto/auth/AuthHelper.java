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
package org.tsm.concharto.auth;

import java.util.Enumeration;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.util.WebUtils;

/**
 * Utility for dealing with authentication via threadlocal and session data
 * @author frank
 *
 */
public class AuthHelper {
	public static final String COOKIE_REMEMBER_ME = "remember";
	public static final String COOKIE_REMEMBER_ME_USERNAME = "user";
	public static final int COOKIE_REMEMBER_ME_MAX_AGE = 3600*24*20;  //20 DAYS

	public static boolean isUserAnAdmin() {
		UserContext userContext = ThreadLocalUserContext.getUserContext();
        return StringUtils.contains(userContext.getRoles(), AuthConstants.ROLE_ADMIN);
    }
	
    public static String getUsername() {
        UserContext userContext = ThreadLocalUserContext.getUserContext();
        return userContext.getUsername();
    }
	
	public static boolean isUserInSession(HttpServletRequest request) {
		HttpSession session = request.getSession();
		return (null != session.getAttribute(AuthConstants.SESSION_AUTH_USERNAME));
	}
	
	@SuppressWarnings("unchecked")
	public static void clearCredentials(HttpServletRequest request, HttpServletResponse response) {
    	//clear out the session
        Enumeration attrNames = request.getSession().getAttributeNames();
        while (attrNames.hasMoreElements()) {
        	String name = (String) attrNames.nextElement();
        	WebUtils.setSessionAttribute(request, name, null);
        	//free all session data
        	request.getSession().invalidate();
        }
        //clear the remember me cookies
		AuthHelper.setCookie(response, AuthHelper.COOKIE_REMEMBER_ME, 0, "");
		AuthHelper.setCookie(response, AuthHelper.COOKIE_REMEMBER_ME_USERNAME, 0, "");
		
	}
	
	public static void setCookie(HttpServletResponse response, String cookieName, int maxAge, String value) {
		Cookie cookie = new Cookie(cookieName, value);
		cookie.setMaxAge(maxAge);
		response.addCookie(cookie);
	}


}
