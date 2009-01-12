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
package org.tsm.concharto.web;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;
import org.tsm.concharto.auth.AuthHelper;
import org.tsm.concharto.dao.AuditEntryDao;
import org.tsm.concharto.dao.EventDao;
import org.tsm.concharto.model.Event;
import org.tsm.concharto.model.audit.AuditFieldChange;
import org.tsm.concharto.model.geometry.TsGeometry;
import org.tsm.concharto.util.JSONFormat;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.io.WKTReader;

/**
 * The central switchboard
 * {@link org.springframework.web.servlet.mvc.Controller} implementation for the
 * application.
 */
public class SwitchBoardController extends MultiActionController {
    private static final String CHANGE_NEW_VALUE = "newValue";
	private static final String PARAM_CHANGE = "change";
	private static final String MODEL_EVENT = "event";
	private static final String PARAM_ID = "id";

    private static final int MAX_RESULTS = 200;
    private EventDao eventDao;
    private AuditEntryDao auditEntryDao;

    public void setEventDao(EventDao eventDao) {
        this.eventDao = eventDao;
    }

	public void setAuditEntryDao(AuditEntryDao auditEntryDao) {
		this.auditEntryDao = auditEntryDao;
	}

	public ModelAndView logout(HttpServletRequest request, HttpServletResponse response) throws Exception {
    	AuthHelper.clearCredentials(request, response);
        return new ModelAndView("redirect:/");
    }
    
    public ModelAndView notauthorized(HttpServletRequest request, HttpServletResponse response) throws Exception {
        return new ModelAndView();
    }
        
    @SuppressWarnings("unchecked")
	public Map mapthumbnail(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map model = new HashMap();
		Event event = getEvent(request);
    	if (event != null) {
    		model.put(MODEL_EVENT, JSONFormat.toJSON(event));
    	}
        return model;
    }

    @SuppressWarnings("unchecked")
	public ModelAndView auditmapthumbnail(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map model = new HashMap();
		Long id = ServletRequestUtils.getLongParameter(request, PARAM_ID);
		String whichChange = ServletRequestUtils.getStringParameter(request, PARAM_CHANGE);
		AuditFieldChange change = auditEntryDao.getAuditFieldChange(id);
		
		//now create a fake event using this geometry
		Event event = new Event();
		TsGeometry tsGeometry = new TsGeometry();
		//this will be oldValue or newValue
		Geometry geometry;
		if (CHANGE_NEW_VALUE.equals(whichChange)) {
			geometry = new WKTReader().read(change.getNewValue());
		} else {
			geometry = new WKTReader().read(change.getOldValue());
		}
		tsGeometry.setGeometry(geometry);
		event.setTsGeometry(tsGeometry);
		model.put(MODEL_EVENT, JSONFormat.toJSON(event));
        return new ModelAndView("search/mapthumbnail",model);
    }
    
    private Event getEvent(HttpServletRequest request) {
    	String id = request.getParameter(PARAM_ID);
    	if (!StringUtils.isEmpty(id)) {
        	Long eventId = new Long(id);
           	return eventDao.findById(eventId);
    	}
    	return null;
    }
    	
 }
