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
package org.tsm.concharto.web.eventsearch;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;

/**
 * Web controller for displaying the results of a search embedded in an iframe. 
 * @author frank
 *
 */
public class EmbeddedSearchController extends SimpleFormController {
	SearchHelper searchHelper;
	
	public void setSearchHelper(SearchHelper searchHelper) {
		this.searchHelper = searchHelper;
	}

	@Override
    protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder)
            throws Exception {
		searchHelper.initBinder(request, binder);
        super.initBinder(request, binder);
    }

	/**
     * Override the default setting.  Everything is a form submission.
     */
    @Override
	protected boolean isFormSubmission(HttpServletRequest request) {
        return true;
    }

	@SuppressWarnings("unchecked")
	@Override
	protected ModelAndView processFormSubmission(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors)
			throws Exception {

		EventSearchForm eventSearchForm = (EventSearchForm) command;
    	//this request contains enough information to do a search right now
    	//use the same binder to get params of the query string as we were using for the POST
        //populate the form with parameters off the URL query string
    	searchHelper.bindGetParameters(request, eventSearchForm);
    	
    	//search
    	Map model = errors.getModel(); 
		String mapKey = getMessageSourceAccessor().getMessage(searchHelper.makeMapKeyCode(request));
    	searchHelper.doSearch(mapKey, request, model, eventSearchForm);
    	return new ModelAndView(getSuccessView(), model);
	}

}
