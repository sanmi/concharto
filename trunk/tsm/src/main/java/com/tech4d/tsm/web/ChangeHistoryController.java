package com.tech4d.tsm.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import com.tech4d.tsm.dao.AuditEntryDao;
import com.tech4d.tsm.dao.EventDao;
import com.tech4d.tsm.model.Event;
import com.tech4d.tsm.model.audit.AuditEntry;
import com.tech4d.tsm.web.util.AuditEntryFormat;
import com.tech4d.tsm.web.util.EventAuditFormat;

public class ChangeHistoryController extends AbstractController {
    private static final int MAX_RESULTS = 20;
    EventDao eventDao;
    AuditEntryDao auditEntryDao;


    public void setEventDao(EventDao eventDao) {
        this.eventDao = eventDao;
    }

    public void setAuditEntryDao(AuditEntryDao auditEntryDao) {
        this.auditEntryDao = auditEntryDao;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String idStr = request.getParameter("id");
        Long id = new Long(idStr);
        System.out.println("--------- '" + idStr + "', " + id);
        Map model = new HashMap();
        if (id != null) {
            Event event = new Event();
            event.setId(id);
            List<AuditEntry> auditEntries = auditEntryDao.getAuditEntries(event, 0, MAX_RESULTS);
            System.out.println("num records " + auditEntries.size());
            model.put("auditEntries", auditEntries);
            model.put("id", id);
            model.put("actionLabels", AuditEntryFormat.ACTION_LABELS);
            model.put("propertyLabels", EventAuditFormat.PROPERTY_LABELS);
        }
        return new ModelAndView("changehistory", model);
    }
    
    

}
