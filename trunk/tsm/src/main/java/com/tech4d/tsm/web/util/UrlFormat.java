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
