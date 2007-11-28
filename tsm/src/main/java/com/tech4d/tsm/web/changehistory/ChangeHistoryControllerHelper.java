package com.tech4d.tsm.web.changehistory;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.ServletRequestUtils;

import com.tech4d.tsm.dao.AuditEntryDao;
import com.tech4d.tsm.model.Event;
import com.tech4d.tsm.model.audit.AuditEntry;
import com.tech4d.tsm.model.audit.AuditEntryFormat;
import com.tech4d.tsm.web.util.DisplayTagHelper;

public class ChangeHistoryControllerHelper {
    private static final String MODEL_ACTION_LABELS = "actionLabels";
    private static final String MODEL_ID = "id";
    private static final String MODEL_AUDIT_ENTRIES = "auditEntries";
    private static final String DISPLAYTAG_TABLE_ID = "auditEntryTable";
    private static final int DISPLAYTAG_PAGESIZE = 10;
    AuditEntryDao auditEntryDao;
    
    public void setAuditEntryDao(AuditEntryDao auditEntryDao) {
		this.auditEntryDao = auditEntryDao;
	}

	@SuppressWarnings("unchecked")
    public Map doProcess(String formView, HttpServletRequest request, Map model) throws Exception {
        Integer firstRecord = DisplayTagHelper.getFirstRecord(request, DISPLAYTAG_TABLE_ID, DISPLAYTAG_PAGESIZE);
        Long id = ServletRequestUtils.getLongParameter(request, MODEL_ID);
        Event event = new Event();
        event.setId(id);
        List<AuditEntry> auditEntries = auditEntryDao.getAuditEntries(event, firstRecord, DISPLAYTAG_PAGESIZE);
        Long totalResults = auditEntryDao.getAuditEntriesCount(event);
        model.put(DisplayTagHelper.MODEL_PAGESIZE, DISPLAYTAG_PAGESIZE);
        model.put(DisplayTagHelper.MODEL_REQUEST_URI, request.getContextPath() + '/' + formView);
        model.put(DisplayTagHelper.MODEL_TOTAL_RESULTS, Math.round(totalResults));
        model.put(MODEL_AUDIT_ENTRIES, auditEntries);
        model.put(MODEL_ID, id);
        model.put(MODEL_ACTION_LABELS, AuditEntryFormat.ACTION_LABELS);

        return model;
    }

}
