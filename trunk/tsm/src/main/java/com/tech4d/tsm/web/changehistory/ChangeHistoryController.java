package com.tech4d.tsm.web.changehistory;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import com.tech4d.tsm.dao.AuditEntryDao;

public class ChangeHistoryController extends AbstractController {
    private static final int DEFAULT_PAGE_SIZE = 10;
	private String formView;
    private ChangeHistoryControllerHelper changeHistoryControllerHelper = new ChangeHistoryControllerHelper();
    private int pageSize = DEFAULT_PAGE_SIZE;
    
    public String getFormView() {
        return formView;
    }
    public void setPageSize(int pageSize) {
    	this.pageSize = pageSize;
    }
    public void setFormView(String formView) {
        this.formView = formView;
    }
    
    public void setAuditEntryDao(AuditEntryDao auditEntryDao) {
        changeHistoryControllerHelper.setAuditEntryDao(auditEntryDao, pageSize);
    }

	@SuppressWarnings("unchecked")
	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map model = new HashMap();
        changeHistoryControllerHelper.doProcess(getFormView(), request, model);
        return new ModelAndView(getFormView(), model);
	}
    
}
