package com.tech4d.tsm.web;

import com.tech4d.tsm.dao.AuditEntryDao;
import com.tech4d.tsm.dao.EventDao;
import com.tech4d.tsm.model.Event;
import com.tech4d.tsm.model.audit.AuditFieldChange;
import com.tech4d.tsm.model.geometry.TsGeometry;
import com.tech4d.tsm.util.JSONFormat;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.io.WKTReader;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

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

	public ModelAndView listEvents(HttpServletRequest request, HttpServletResponse response) throws Exception {
        return new ModelAndView().addObject(this.eventDao.findRecent(MAX_RESULTS));
    }
    
    @SuppressWarnings("unchecked")
	public ModelAndView logout(HttpServletRequest request, HttpServletResponse response) throws Exception {
    	//clear out the session
        Enumeration attrNames = request.getSession().getAttributeNames();
        while (attrNames.hasMoreElements()) {
        	String name = (String) attrNames.nextElement();
        	WebUtils.setSessionAttribute(request, name, null);
        	//free all session data
        	request.getSession().invalidate();
        }
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
