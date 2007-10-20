package com.tech4d.tsm.auth;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Authorization filter.  We don't want to rely on the servlet container to do this because we
 * will have to get fancy in the future.
 * @author frank
 *
 */
public class LoginFilter implements Filter{
    private static final String REDIRECT_NOTAUTHORIZED = "/notauthorized.htm";
    private static final String REDIRECT_LOGIN = "/login.htm";
    //TODO search requires authentication only during the private pilot
    private static final String[] PATTERN_REQUIRES_AUTHENTICATION = {"search","edit","admin","member"};
    private static final String[] PATTERN_REQUIRES_AUTHORIZATION = {"edit","admin"};
    private static final Log log = LogFactory.getLog(LoginFilter.class);
    
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        
        //Does this page require authentication
        if (requiresAuthentication(httpRequest)) {
            //Has the user signed in?
            if (!isAuthenticated(httpRequest)) {
                httpResponse.sendRedirect(httpResponse.encodeRedirectURL(httpRequest.getContextPath() + REDIRECT_LOGIN));                
            }  
            //ok, is the user authorized for this URL
            else if (!isAuthorized(httpRequest)) {
                httpResponse.sendRedirect(httpResponse.encodeRedirectURL(httpRequest.getContextPath() + REDIRECT_NOTAUTHORIZED));
            }
        }
        //setup the user context for those who can't get user and role data from
        //the session (e.g. audit interceptor)
        HttpSession session = httpRequest.getSession();
        UserContext userContext = new UserContext();
        userContext.setUsername((String) session.getAttribute(AuthConstants.AUTH_USERNAME));
        userContext.setRoles((String) session.getAttribute(AuthConstants.AUTH_ROLES));
        ThreadLocalUserContext.setUserContext(userContext);
        chain.doFilter(request, response);
    }

    /**
     * This implements is a crude 2 level URL based authorization scheme.
     * For each of the role patterns ensure that the appropriate role exists.
     * Examples:
     * <pre>
     *  /admin/canDelete/deleteUser.htm requres 'admin' and 'canDelete'
     *  /admin/findUsers.htm requires 'admin'
     *  /admin/canBlock/blockUser.htm requires 'admin' and 'canBlock'
     * </pre>
     * @param httpRequest request
     * @return true if the user is authorized for this resource
     */
    private boolean isAuthorized(HttpServletRequest httpRequest) {
        HttpSession session = httpRequest.getSession();
        if (!requiresAuthorization(httpRequest)) {
            return true;
        }
        if (null != session.getAttribute(AuthConstants.AUTH_ROLES)) {
            String roles = (String) session.getAttribute(AuthConstants.AUTH_ROLES);
            String uri = httpRequest.getRequestURI();
            //should like like '/admin/canDelete' or '/admin/findUsers'
            String path = StringUtils.substringBetween(uri, httpRequest.getContextPath(),".htm");
            String[] parts = StringUtils.split(path, '/');
            if (parts.length == 2) {
                //one level only e.g. URL was admin/findUsers.htm
                if (StringUtils.contains(roles, parts[0])) {
                    //e.g. "admin canDelete canMove" contains "admin"
                    return true;
                }
            } else if (parts.length == 3) {
                //two levels e.g. URL was admin/canDelete/deleteUsers.htm
                if (StringUtils.contains(roles, parts[0]) && StringUtils.contains(roles, parts[1])) {
                    //e.g. "admin canDelete canMove" contains "admin" and "canDelete"
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isAuthenticated(HttpServletRequest httpRequest) {
        HttpSession session = httpRequest.getSession();
        if (log.isDebugEnabled()) {
            log.debug("auth login filter");
        }
        //TODO reliance on session may be a problem for scalability
        if (null == session.getAttribute(AuthConstants.AUTH_USERNAME)) {
            //save the target so we can get there after authentication
            StringBuffer redirect = new StringBuffer(httpRequest.getRequestURI());
            if (!StringUtils.isEmpty(httpRequest.getQueryString())) {
                redirect.append('?').append(httpRequest.getQueryString());
            }
            session.setAttribute(AuthConstants.AUTH_TARGET_URI, redirect.toString() );
            return false;
        } else {
            return true;
        }
    }

    private boolean requiresAuthentication(HttpServletRequest httpRequest) {
        //TODO this is for the beta period.  Remove later!!
        if (httpRequest.getServerName().equals("www.map-4d.com")) {
            return false;
        } 
        for (String pattern : PATTERN_REQUIRES_AUTHENTICATION) {
            if (StringUtils.contains(httpRequest.getRequestURI(), pattern)) {
                return true;
            }
        }
        return false;
    }
    private boolean requiresAuthorization(HttpServletRequest httpRequest) {
        for (String pattern : PATTERN_REQUIRES_AUTHORIZATION) {
            if (StringUtils.contains(httpRequest.getRequestURI(), pattern)) {
                return true;
            }
        }
        return false;
    }

    public void destroy() {
        // TODO Auto-generated method stub
        
    }

}
