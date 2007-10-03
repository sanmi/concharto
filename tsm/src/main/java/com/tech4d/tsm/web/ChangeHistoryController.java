package com.tech4d.tsm.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import com.tech4d.tsm.dao.AuditEntryDao;
import com.tech4d.tsm.dao.TsEventDao;
import com.tech4d.tsm.model.TsEvent;
import com.tech4d.tsm.model.audit.AuditEntry;
import com.tech4d.tsm.web.util.AuditEntryFormat;
import com.tech4d.tsm.web.util.TsEventAuditFormat;

public class ChangeHistoryController extends AbstractController {
    private static final int MAX_RESULTS = 20;
    TsEventDao tsEventDao;
    AuditEntryDao auditEntryDao;


    public void setTsEventDao(TsEventDao tsEventDao) {
        this.tsEventDao = tsEventDao;
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
            TsEvent event = new TsEvent();
            event.setId(id);
            List<AuditEntry> auditEntries = auditEntryDao.getAuditEntries(event, 0, MAX_RESULTS);
            System.out.println("num records " + auditEntries.size());
            model.put("auditEntries", auditEntries);
            model.put("id", id);
            model.put("actionLabels", AuditEntryFormat.ACTION_LABELS);
            model.put("propertyLabels", TsEventAuditFormat.PROPERTY_LABELS);
        }
        return new ModelAndView("changehistory", model);
    }
    
    

}
