package com.tech4d.tsm.web;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractFormController;

import com.tech4d.tsm.dao.AuditEntryDao;
import com.tech4d.tsm.model.Event;
import com.tech4d.tsm.model.audit.AuditEntry;
import com.tech4d.tsm.web.util.AuditEntryFormat;
import com.tech4d.tsm.web.util.EventAuditFormat;
import com.tech4d.tsm.web.util.PaginatingFormHelper;

public class ChangeHistoryController extends AbstractFormController {
    private static final String MODEL_TOTAL_RESULTS = "totalResults";
    private static final String MODEL_PROPERTY_LABELS = "propertyLabels";
    private static final String MODEL_ACTION_LABELS = "actionLabels";
    private static final String MODEL_ID = "id";
    private static final String MODEL_AUDIT_ENTRIES = "auditEntries";
    private static final int MAX_RESULTS = 5;
    private AuditEntryDao auditEntryDao;
    private String successView;
    private String formView;
    
    public String getFormView() {
        return formView;
    }

    public void setFormView(String formView) {
        this.formView = formView;
    }

    public String getSuccessView() {
        return successView;
    }

    public void setSuccessView(String successView) {
        this.successView = successView;
    }

    public void setAuditEntryDao(AuditEntryDao auditEntryDao) {
        this.auditEntryDao = auditEntryDao;
    }

    @Override
    protected ModelAndView processFormSubmission(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
        ChangeHistoryForm changeHistoryForm = (ChangeHistoryForm) command;
        Map model = errors.getModel();
        if (errors.hasErrors()) {
            //not sure how this could happen
            if (logger.isDebugEnabled()) {
                logger.debug("Data binding errors: " + errors.getErrorCount());
            }
            return new ModelAndView(getFormView(), model);
        } else {
            int firstRecord = PaginatingFormHelper.calculateFirstRecord(changeHistoryForm, MAX_RESULTS);
            changeHistoryForm.setCurrentRecord(firstRecord);
            return doProcess(changeHistoryForm, request, errors);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    protected ModelAndView showForm(HttpServletRequest request, HttpServletResponse response, BindException errors) throws Exception {
        return doProcess((ChangeHistoryForm) getCommand(request), request, errors);
    }
    
    @SuppressWarnings("unchecked")
    private ModelAndView doProcess(ChangeHistoryForm changeHistoryForm, HttpServletRequest request, BindException errors) throws Exception {
        String idStr = request.getParameter(MODEL_ID);
        Long id = new Long(idStr);
        Map model = errors.getModel();
        Integer firstRecord = changeHistoryForm.getCurrentRecord();
        if (firstRecord == null ){
            //first time, this will be null
            firstRecord = 0;
        }

        Event event = new Event();
        event.setId(id);
        List<AuditEntry> auditEntries = auditEntryDao.getAuditEntries(event, firstRecord, MAX_RESULTS);
        //if the number of results = the max, there are probably more
        Long totalResults = 0L;
        if (auditEntries.size() >= MAX_RESULTS) {
            totalResults = auditEntryDao.getAuditEntriesCount(event);
        } else {
            totalResults += auditEntries.size();
        }
        model.put(MODEL_AUDIT_ENTRIES, auditEntries);
        model.put(MODEL_ID, id);
        model.put(MODEL_ACTION_LABELS, AuditEntryFormat.ACTION_LABELS);
        model.put(MODEL_PROPERTY_LABELS, EventAuditFormat.PROPERTY_LABELS);
        model.put(MODEL_TOTAL_RESULTS, totalResults);

        return new ModelAndView("changehistory", model);
    }
}
