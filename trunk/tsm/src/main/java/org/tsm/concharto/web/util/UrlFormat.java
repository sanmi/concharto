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
package org.tsm.concharto.web.util;

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
