package com.tech4d.tsm.auth;

/**
 * Static class for saving user information as ThreadLocal info.  This
 * is used by the audit interceptor and others. 
 * 
 * @author frank
 *
 */
public class ThreadLocalUserContext {
    private static ThreadLocal<UserContext> localUserContext = new ThreadLocal<UserContext>();

    public static UserContext getUserContext() {
        UserContext context = (UserContext) localUserContext.get();
        if (context == null) {
            context = new UserContext();
            localUserContext.set(context);
        }
        return context;
    }

    public static void setUserContext(UserContext context) {
        localUserContext.set(context);
    }
}
