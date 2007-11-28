package com.tech4d.tsm.web.util;

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
