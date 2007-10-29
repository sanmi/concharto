package com.tech4d.tsm.web.changehistory;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.displaytag.tags.TableTagParameters;
import org.displaytag.util.ParamEncoder;

import com.tech4d.tsm.dao.AuditEntryDao;
import com.tech4d.tsm.model.Event;
import com.tech4d.tsm.model.audit.AuditEntry;
import com.tech4d.tsm.model.audit.AuditEntryFormat;

public class ChangeHistoryControllerHelper {
    private static final String MODEL_TOTAL_RESULTS = "totalResults";
    private static final String MODEL_ACTION_LABELS = "actionLabels";
    private static final String MODEL_ID = "id";
    private static final String MODEL_AUDIT_ENTRIES = "auditEntries";
    private static final String MODEL_DISPLAYTAG_PAGESIZE = "pagesize";
    private static final String MODEL_DISPLAYTAG_REQUEST_URI = "requestURI";
    private static final String DISPLAYTAG_TABLE_ID = "auditEntryTable";
    private static final int DISPLAYTAG_PAGESIZE = 10;
    AuditEntryDao auditEntryDao;
    
    public void setAuditEntryDao(AuditEntryDao auditEntryDao) {
		this.auditEntryDao = auditEntryDao;
	}

	@SuppressWarnings("unchecked")
    public Map doProcess(String formView, HttpServletRequest request, Map model) throws Exception {
        String idStr = request.getParameter(MODEL_ID);
        String pageParam = request.getParameter((new ParamEncoder(DISPLAYTAG_TABLE_ID).encodeParameterName(TableTagParameters.PARAMETER_PAGE)));
        Integer firstRecord;
        if (null != pageParam) {
        	firstRecord = (Integer.parseInt(pageParam) - 1) * DISPLAYTAG_PAGESIZE;
        } else {
        	firstRecord = 0;
        }
        Long id = new Long(idStr);
        Event event = new Event();
        event.setId(id);
        List<AuditEntry> auditEntries = auditEntryDao.getAuditEntries(event, firstRecord, DISPLAYTAG_PAGESIZE);
        Long totalResults = auditEntryDao.getAuditEntriesCount(event);
        model.put(MODEL_DISPLAYTAG_PAGESIZE, DISPLAYTAG_PAGESIZE);
        model.put(MODEL_DISPLAYTAG_REQUEST_URI, request.getContextPath() + '/' + formView);
        model.put(MODEL_AUDIT_ENTRIES, auditEntries);
        model.put(MODEL_ID, id);
        model.put(MODEL_ACTION_LABELS, AuditEntryFormat.ACTION_LABELS);
        model.put(MODEL_TOTAL_RESULTS, Math.round(totalResults));

        return model;
    }

}
