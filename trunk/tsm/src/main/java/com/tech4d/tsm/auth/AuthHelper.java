package com.tech4d.tsm.auth;

import org.apache.commons.lang.StringUtils;

public class AuthHelper {

	public static boolean isUserAnAdmin() {
		UserContext userContext = ThreadLocalUserContext.getUserContext();
        return StringUtils.contains(userContext.getRoles(), AuthConstants.ROLE_ADMIN);
    }
	
    public static String getUsername() {
        UserContext userContext = ThreadLocalUserContext.getUserContext();
        return userContext.getUsername();
    }

}
