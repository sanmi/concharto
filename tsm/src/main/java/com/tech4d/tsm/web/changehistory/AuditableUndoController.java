package com.tech4d.tsm.web.changehistory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;
import org.springframework.web.servlet.view.RedirectView;

import com.tech4d.tsm.model.Auditable;
import com.tech4d.tsm.service.RevertEventService;

public class AuditableUndoController extends SimpleFormController {
    private Class<Auditable> auditableClass;
	private static final String PARAM_EVENT_ID = "eventId";
	private static final String PARAM_PAGE = "page";
    private RevertEventService revertEventService;
	
	public void setAuditableClass(Class<Auditable> auditableClass) {
		this.auditableClass = auditableClass;
	}
	public void setRevertEventService(RevertEventService revertEventService) {
		this.revertEventService = revertEventService;
	}
	
	@Override
	protected ModelAndView processFormSubmission(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors)
			throws Exception {

		UndoForm undoForm = (UndoForm) command;
    	Long id = undoForm.getId();
    	Integer revision = undoForm.getToRev();

    	if ((id != null) && (revision != null)) {
    		revertEventService.revertToRevision(this.auditableClass, revision, id);
    	}
        //TODO UGH...this is a UI hack : auditables are event or event.discussion or wiki pages
    	Long eventId = ServletRequestUtils.getLongParameter(request, PARAM_EVENT_ID);
    	String page = ServletRequestUtils.getStringParameter(request, PARAM_PAGE);
    	StringBuffer redirect = new StringBuffer(request.getContextPath())
    		.append('/')
    		.append(getSuccessView())
    		.append(".htm?id=")
    		.append(id);
        if (null != eventId) {
        	redirect.append('&')
        	.append(PARAM_EVENT_ID)
        	.append('=')
        	.append(eventId);
        } 
        if (!StringUtils.isEmpty(page)) {
        	redirect.append('&')
        	.append(PARAM_PAGE)
        	.append('=')
        	.append(page);
        } 

        //redirect back to the list
        return new ModelAndView(new RedirectView(redirect.toString(), true));
		
	}

}
