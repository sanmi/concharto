package com.tech4d.tsm.web.util;

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
