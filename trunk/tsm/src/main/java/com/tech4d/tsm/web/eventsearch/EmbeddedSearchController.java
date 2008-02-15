package com.tech4d.tsm.web.eventsearch;

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
