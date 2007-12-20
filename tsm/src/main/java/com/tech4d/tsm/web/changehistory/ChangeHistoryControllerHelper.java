package com.tech4d.tsm.web.changehistory;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;

import com.tech4d.tsm.dao.AuditEntryDao;
import com.tech4d.tsm.dao.AuditUserChange;
import com.tech4d.tsm.dao.EventDao;
import com.tech4d.tsm.model.Auditable;
import com.tech4d.tsm.model.Event;
import com.tech4d.tsm.model.audit.AuditEntry;
import com.tech4d.tsm.web.util.DisplayTagHelper;

/**
 * Helper class for displaying change lists based on GET string parameters.  Used by
 * web controllers wishing to display a list of changes using displaytag.
 * 
 */
public class ChangeHistoryControllerHelper {
    private static final String MODEL_ID = "id";
    private static final String MODEL_USER = "user";
    private static final String MODEL_AUDIT_ENTRIES = "auditEntries";
	private static final String MODEL_USER_CHANGES = "userChanges";
    private static final String DISPLAYTAG_TABLE_ID = "auditEntryTable";
	private static final String MODEL_EVENT_ID = "eventId";
	private static final String MODEL_EVENT = "event";
    private AuditEntryDao auditEntryDao;
    private EventDao eventDao;
    
    public void setAuditEntryDao(AuditEntryDao auditEntryDao) {
		this.auditEntryDao = auditEntryDao;
	}
	public void setEventDao(EventDao eventDao) {
		this.eventDao = eventDao;
	}

	@SuppressWarnings("unchecked")
    public Map doProcess(Class<?> clazz, String formView, HttpServletRequest request, Map model, int pageSize) throws Exception {
        Integer firstRecord = DisplayTagHelper.getFirstRecord(request, DISPLAYTAG_TABLE_ID, pageSize);
        Long id = null;
        boolean hasEmptyId = false;
        try {
        	id = ServletRequestUtils.getLongParameter(request, MODEL_ID);
        } catch (ServletRequestBindingException e) {
        	//this can happen when a discussion has not yet been created for 
        	//an event.  The URL will say ?id=&eventId=1234
        	hasEmptyId = true;
        }
       
        String user = ServletRequestUtils.getStringParameter(request, MODEL_USER);
        Long totalResults = 0L;
        //TODO - split this into two controllers one for user contribs, one for event changes
        if ((id != null) ||(hasEmptyId)) {
        	//we are doing history for an Auditable
            Long eventId = ServletRequestUtils.getLongParameter(request, MODEL_EVENT_ID);
            Event event;
            //TODO this is a UI hack : auditables are event or event.discussion
            if (null == eventId) {
            	event = eventDao.findById(id);
            } else {
            	event = eventDao.findById(eventId);
            }
            model.put(MODEL_EVENT_ID, eventId);
            model.put(MODEL_EVENT, event);
            if (!hasEmptyId) {
	            Auditable auditable = (Auditable) clazz.newInstance();
	            auditable.setId(id);
	            List<AuditEntry> auditEntries = auditEntryDao.getAuditEntries(auditable, firstRecord, pageSize);
	            totalResults = auditEntryDao.getAuditEntriesCount(auditable);        	
	            model.put(MODEL_AUDIT_ENTRIES, auditEntries);
            }
        } else {
        	//we are doing history for a user
        	List<AuditUserChange> userChanges = auditEntryDao.getAuditEntries(user, clazz, firstRecord, pageSize);
            totalResults = auditEntryDao.getAuditEntriesCount(user, clazz);
            model.put(MODEL_USER_CHANGES, userChanges);
        }
        
        model.put(DisplayTagHelper.MODEL_PAGESIZE, pageSize);
        model.put(DisplayTagHelper.MODEL_REQUEST_URI, request.getContextPath() + '/' + formView);
        model.put(DisplayTagHelper.MODEL_TOTAL_RESULTS, Math.round(totalResults));
        model.put(MODEL_ID, id);

        return model;
    }
}
