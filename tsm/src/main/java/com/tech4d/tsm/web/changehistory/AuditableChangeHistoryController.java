package com.tech4d.tsm.web.changehistory;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import com.tech4d.tsm.model.Auditable;
import com.tech4d.tsm.model.Flag;

/**
 * Controller for change history for any auditable class
 * @author frank
 */
public class AuditableChangeHistoryController extends AbstractController {
	private String formView;
    private ChangeHistoryControllerHelper changeHistoryControllerHelper;
    private Class<Auditable> auditableClass;
    private static final int DEFAULT_PAGE_SIZE = 10;
	private static final String MODEL_DISPOSITIONS = "dispositions";
    private int pageSize = DEFAULT_PAGE_SIZE;

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	public void setAuditableClass(Class<Auditable> auditableClass) {
		this.auditableClass = auditableClass;
	}
	public void setFormView(String formView) {
		this.formView = formView;
	}
	public void setChangeHistoryControllerHelper(
			ChangeHistoryControllerHelper changeHistoryControllerHelper) {
		this.changeHistoryControllerHelper = changeHistoryControllerHelper;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Map model = new HashMap();
    	model.put(MODEL_DISPOSITIONS, Flag.DISPOSITION_CODES);
        changeHistoryControllerHelper.doProcess(this.auditableClass, this.formView, request, model, pageSize);
        return new ModelAndView(this.formView, model);
	}

	
}
