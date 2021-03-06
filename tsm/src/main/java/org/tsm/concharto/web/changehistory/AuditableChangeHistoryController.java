/*******************************************************************************
 * Copyright 2009 Time Space Map, LLC
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package org.tsm.concharto.web.changehistory;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;
import org.tsm.concharto.model.Auditable;
import org.tsm.concharto.model.Flag;


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
