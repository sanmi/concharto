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
package org.tsm.concharto.web.util;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

/**
 * Utility for dealing with event catalogs (e.g. history, fishing, ...)
 * @author frank
 *
 */
public class CatalogUtil {

	public static final String HOST_HISTORY = "history";
	public static final String CATALOG_WWW = "www";

	/**
	 * Get the host part of the server name.  This is our catalog.
	 * If it is www, then that means history.
	 * 
	 * @param request
	 * @return
	 */
	public static String getCatalog(HttpServletRequest request) {
		String host = request.getServerName();
		String catalog = StringUtils.substringBefore(host, ".");
		if (HOST_HISTORY.equals(catalog)) {
			catalog = CATALOG_WWW;
		}
		if (catalog != null) {
			return catalog.toLowerCase();
		} else {
			throw new NoCatalogException("no catalog found in the request object (check the hostname)");
		}
	}
	
}
