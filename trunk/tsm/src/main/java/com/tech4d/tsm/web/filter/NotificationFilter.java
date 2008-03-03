package com.tech4d.tsm.web.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.util.WebUtils;

import com.tech4d.tsm.auth.AuthHelper;
import com.tech4d.tsm.dao.NotificationDao;

/**
 * Servlet filter for informing the user when there are notifications pending
 *
 */
public class NotificationFilter implements Filter{
    public static final String SESSION_MESSAGES_PENDING = "messagesPending";
	private static final String CONFIG_IGNORE = "ignore";
	private NotificationDao notificationDao;
    private FilterConfig filterConfig;

	public void init(FilterConfig filterConfig) throws ServletException {
    	this.filterConfig = filterConfig;
    	ServletContext ctx = filterConfig.getServletContext();
        WebApplicationContext webAppContext = WebApplicationContextUtils.getWebApplicationContext(ctx);
        notificationDao = (NotificationDao) webAppContext.getBean("notificationDao");
	}

	public void doFilter(ServletRequest servletRequest, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;

        //Ignore URLs like embedded maps
        String ignore = filterConfig.getInitParameter(CONFIG_IGNORE);
        if (!StringUtils.contains(request.getRequestURI(),ignore)) {
    		String username  = AuthHelper.getUsername();
    		if (notificationDao.notificationsExist(username)) {
    			WebUtils.setSessionAttribute(request, SESSION_MESSAGES_PENDING, "yes");
    		}
        }
        
        chain.doFilter(request, response);
	}

	public void destroy() {
	}

}
