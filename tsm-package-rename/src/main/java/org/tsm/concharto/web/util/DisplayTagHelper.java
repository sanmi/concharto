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

import org.displaytag.tags.TableTagParameters;
import org.displaytag.util.ParamEncoder;

/**
 * Contains utility functions for dealing with displaytag pagination
 * 
 * @author frank
 *
 */
public class DisplayTagHelper {
    public static final String MODEL_PAGESIZE = "pagesize";
    public static final String MODEL_REQUEST_URI = "requestURI";
    public static final String MODEL_TOTAL_RESULTS = "totalResults";


    /**
     * Calculate the first record based on the displaytag page parameter id in the request 
     * @param request request
     * @param tableId displaytag table id
     * @param pageSize page size
     * @return record to start your search
     */
	public static Integer getFirstRecord(HttpServletRequest request, String tableId, int pageSize) {
		String pageParam = getPageParameterId(request, tableId);
        Integer firstRecord;
        if (null != pageParam) {
        	firstRecord = (Integer.parseInt(pageParam) - 1) * pageSize;
        } else {
        	firstRecord = 0;
        }
		return firstRecord;
	}

	/**
	 * Get the id that will be placed in the request when the user pages (e.g. 
	 * d-148316-p is the page parameter id for http://www.map4d.com//search/eventsearch.htm?d-148316-p=2 
	 * @param request request
	 * @param tableId displaytag table id
	 * @return the page parameter id
	 */
	public static String getPageParameterId(HttpServletRequest request, String tableId) {
		String pageParam = request.getParameter((new ParamEncoder(tableId).encodeParameterName(TableTagParameters.PARAMETER_PAGE)));
		return pageParam;
	}
}
