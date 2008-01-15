package com.tech4d.tsm.auth;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.util.WebUtils;

import com.tech4d.tsm.model.user.Role;
import com.tech4d.tsm.model.user.User;

public class AuthHelper {

	public static boolean isUserAnAdmin() {
		UserContext userContext = ThreadLocalUserContext.getUserContext();
        return StringUtils.contains(userContext.getRoles(), AuthConstants.ROLE_ADMIN);
    }
	
    public static String getUsername() {
        UserContext userContext = ThreadLocalUserContext.getUserContext();
        return userContext.getUsername();
    }

	public static void setUserInSession(HttpServletRequest request, User user) {
		WebUtils.setSessionAttribute(request, AuthConstants.SESSION_AUTH_USERNAME, user.getUsername());
		WebUtils.setSessionAttribute(request, AuthConstants.SESSION_AUTH_ROLES, makeRoles(user.getRoles()));
	}

    private static String makeRoles(List<Role> roles) {
        StringBuffer roleStr = new StringBuffer();
        if (roles != null) {
            for (Role role : roles) {
                roleStr.append(role.getName()).append(" ");
            }
        }
        return roleStr.toString();
    }

}
