package com.tech4d.tsm.web.util;

import javax.servlet.http.HttpServletRequest;

/**
 * Utility to help with url formatting
 * 
 * @author frank
 *
 */
public class UrlFormat {
	
	/**
	 * Get the basepath in the form http://<servername>/<context path> OR if the port is not
	 * port 80, http://<servername>:<port>/<context path>
	 * 
	 * @param request
	 * @return formatted base path
	 */
	public static String getBasepath(HttpServletRequest request) {
		String contextPath = request.getContextPath();
		StringBuffer basePathsb = new StringBuffer()
			.append(request.getScheme())
			.append("://")
			.append(request.getServerName());
		int port = request.getServerPort();
		if (port != 80) {
			basePathsb.append(":").append(port);
		}
		basePathsb.append('/').append(contextPath);
		return basePathsb.toString();
	}

}
