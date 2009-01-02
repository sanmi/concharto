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
package com.tech4d.tsm.web.filter;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.TimeZone;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * For setting http headers for various files.  NOTE: if this gets
 * much more complicated, consider using Spring Framework filter 
 * classes to configure instead of the limited web.xml features
 *
 */
public class ResponseHeaderFilter implements Filter {
	private static final String HEADER_EXPIRES = "Expires";
	private static final String HEADER_DATE_FORMAT = "EEE, d MMM yyyy HH:mm:ss z";
	private static final String HEADER_CACHE_CONTROL = "Cache-Control";
	private FilterConfig fc;
    private Log log = LogFactory.getLog(ResponseHeaderFilter.class);

	public void doFilter(ServletRequest req, ServletResponse res,
			FilterChain chain) throws IOException, ServletException {
		HttpServletResponse response = (HttpServletResponse) res;
		// set the provided HTTP response parameters
		for (Enumeration<?> e = fc.getInitParameterNames(); e.hasMoreElements();) {
			String headerName = (String) e.nextElement();
			String value = fc.getInitParameter(headerName);
			response.addHeader(headerName, value);
			//special case for Cache-Control
			if (HEADER_CACHE_CONTROL.equals(headerName)) {
				String maxAgeStr = StringUtils.substringBetween(value, "=", ",");
				addExpiresHeader(response, maxAgeStr);
			}
		}
		// pass the request/response on
		chain.doFilter(req, response);
	}

	private void addExpiresHeader(HttpServletResponse response, String maxAgeStr) {
		try {
			Integer maxAge = new Integer(maxAgeStr);
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.SECOND, maxAge);
			SimpleDateFormat sdf = new SimpleDateFormat(HEADER_DATE_FORMAT);
			sdf.setTimeZone (TimeZone.getTimeZone("GMT"));
			String expires = sdf.format(cal.getTime());
			response.addHeader(HEADER_EXPIRES, expires);
			         
		} catch (NumberFormatException e) {
			log.error("incorrectly specified web.xml - can't find maxAge", e);
		}
		
	}

	public void init(FilterConfig filterConfig) {
		this.fc = filterConfig;
	}

	public void destroy() {
		this.fc = null;
	}

}
