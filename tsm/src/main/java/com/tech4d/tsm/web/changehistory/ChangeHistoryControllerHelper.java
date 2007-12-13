package com.tech4d.tsm.web.changehistory;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.ServletRequestUtils;

import com.tech4d.tsm.dao.AuditEntryDao;
import com.tech4d.tsm.dao.AuditUserChange;
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
    private static final int DEFAULT_DISPLAYTAG_PAGESIZE = 10;
    private AuditEntryDao auditEntryDao;
    private int pageSize;
    
    public void setAuditEntryDao(AuditEntryDao auditEntryDao) {
		setAuditEntryDao(auditEntryDao, DEFAULT_DISPLAYTAG_PAGESIZE);
	}
    
    public void setAuditEntryDao(AuditEntryDao auditEntryDao, int pageSize) {
    	this.auditEntryDao = auditEntryDao;
    	this.pageSize = pageSize;
    }

	@SuppressWarnings("unchecked")
    public Map doProcess(String formView, HttpServletRequest request, Map model) throws Exception {
        Integer firstRecord = DisplayTagHelper.getFirstRecord(request, DISPLAYTAG_TABLE_ID, pageSize);
        Long id = ServletRequestUtils.getLongParameter(request, MODEL_ID);
        String user = ServletRequestUtils.getStringParameter(request, MODEL_USER);
        Long totalResults;
        if (id != null) {
            Event event = new Event();
            event.setId(id);
            List<AuditEntry> auditEntries = auditEntryDao.getAuditEntries(event, firstRecord, pageSize);
            totalResults = auditEntryDao.getAuditEntriesCount(event);        	
            model.put(MODEL_AUDIT_ENTRIES, auditEntries);
        } else {
        	List<AuditUserChange> userChanges = auditEntryDao.getAuditEntries(user, Event.class, firstRecord, pageSize);
            totalResults = auditEntryDao.getAuditEntriesCount(user, Event.class);
            model.put(MODEL_USER_CHANGES, userChanges);
        }
        
        model.put(DisplayTagHelper.MODEL_PAGESIZE, pageSize);
        model.put(DisplayTagHelper.MODEL_REQUEST_URI, request.getContextPath() + '/' + formView);
        model.put(DisplayTagHelper.MODEL_TOTAL_RESULTS, Math.round(totalResults));
        model.put(MODEL_ID, id);

        return model;
    }

}
