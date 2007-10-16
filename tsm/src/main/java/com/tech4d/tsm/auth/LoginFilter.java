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

/**
 * Authorization filter.  We don't want to rely on the servlet container to do this because we
 * will have to get fancy in the future.
 * @author frank
 *
 */
public class LoginFilter implements Filter{
    private static final String[] PATTERN_REQUIRES_AUTHENTICATION = {"edit","admin"};
    
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        
        if (requiresAuthentication(httpRequest)) {
            if (!isAuthenticated(httpRequest)) {
                httpResponse.sendRedirect(httpResponse.encodeRedirectURL(httpRequest.getContextPath() + "/login.htm"));                
            }
        }
        chain.doFilter(request, response);
    }

    private boolean isAuthenticated(HttpServletRequest httpRequest) {
        HttpSession session = httpRequest.getSession();
        //TODO reliance on session may be a problem for scalability
        if (null == session.getAttribute(AuthConstants.AUTH_USERNAME)) {
            //save the target so we can get there after authentication
            session.setAttribute(AuthConstants.AUTH_TARGET_URI, httpRequest.getRequestURI() + '?'+ httpRequest.getQueryString());
            return false;
        } else {
            return true;
        }
    }

    private boolean requiresAuthentication(HttpServletRequest httpRequest) {
        for (String pattern : PATTERN_REQUIRES_AUTHENTICATION) {
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
