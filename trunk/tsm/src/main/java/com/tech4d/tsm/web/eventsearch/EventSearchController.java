package com.tech4d.tsm.web.eventsearch;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractFormController;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.web.util.WebUtils;

import com.tech4d.tsm.auth.AuthConstants;
import com.tech4d.tsm.model.Event;
import com.tech4d.tsm.service.EventSearchService;
import com.tech4d.tsm.util.JSONFormat;

public class EventSearchController extends AbstractFormController {
	private static final Log log = LogFactory.getLog(EventSearchController.class);
	public static final String SESSION_EVENT_SEARCH_FORM = "eventSearchForm";
    public static final String MODEL_EVENTS = "events";
    public static final String MODEL_TOTAL_RESULTS = "totalResults";
    public static final String MODEL_CURRENT_RECORD = "currRecord";
    public static final String MODEL_PAGESIZE = "pageSize";

    public static final int MAX_RECORDS = 25;
    private String formView;
    private String successView;
    private SearchHelper searchHelper;
    
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

    public void setEventSearchService(EventSearchService eventSearchService) {
        this.searchHelper = new SearchHelper(eventSearchService);
    }
    
	@Override
    protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder)
            throws Exception {
		searchHelper.initBinder(request, binder);
        super.initBinder(request, binder);
    }

    private EventSearchForm getEventSearchForm(HttpServletRequest request) {
        return (EventSearchForm) WebUtils.getSessionAttribute(request, SESSION_EVENT_SEARCH_FORM);
    }

    @Override
    protected Object formBackingObject(HttpServletRequest request) throws Exception {
        //check to see if there is a form object in the session, if so it
        //is there for us to use right now (e.g. we are supposed to remember where
        //we were when we left here
        EventSearchForm eventSearchForm = getEventSearchForm(request);
        if (eventSearchForm != null) {
            return eventSearchForm;
        } else {
            return super.formBackingObject(request);
        }
    }
    /**
     * Override the default setting.  If there are query parameters in the URL, we assume
     * this is a form submission (e.g. when=, where=, what=)
     */
    @Override
	protected boolean isFormSubmission(HttpServletRequest request) {
        return !StringUtils.isEmpty(request.getQueryString()) || super.isFormSubmission(request);
    }

    @SuppressWarnings("unchecked")
	@Override
    protected ModelAndView showForm(
            HttpServletRequest request, HttpServletResponse response, BindException errors)
            throws Exception {

        //if there is a form, we should redo the search, just in case things have been
        //added since we left (for instance the user just added a point to the map)
        //don't search if there are errors
        EventSearchForm eventSearchForm = getEventSearchForm(request);
        if ((eventSearchForm != null) && (!errors.hasErrors())){
            Map model = doSearch(request, errors, eventSearchForm);
            return new ModelAndView(getFormView(), model);
        } else {
            return showForm(request, errors, getFormView());
        }
    }

	@SuppressWarnings("unchecked")
    @Override
    protected ModelAndView processFormSubmission(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
		//TODO DEBUG don't do anything if the user isn't logged in.  Remove this kludge after we go live.  It is because
		//of redirects causing havoc with our LoginFilter - I can't figure out a better way at the moment
		//fsm 11-13-07
		if (null == WebUtils.getSessionAttribute(request, AuthConstants.SESSION_AUTH_USERNAME)) {
			return showForm(request, response, errors);
		}
		
        EventSearchForm eventSearchForm = (EventSearchForm) command;
        logSearchQuery(eventSearchForm);
        ModelAndView returnModelAndView;
        if (errors.hasErrors()) {
            if (logger.isDebugEnabled()) {
                logger.debug("Data binding errors: " + errors.getErrorCount());
            }
            //clear out the search results
            eventSearchForm.setSearchResults(null);
            returnModelAndView = showForm(request, response, errors);
        } else if (!StringUtils.isEmpty(request.getQueryString())) {
        	//if this is a form submission via query params, we will have to geocode first 
        	//then do a redirect
            eventSearchForm = new EventSearchForm();
        	returnModelAndView = handleGet(request, eventSearchForm);
        } else {
            logger.debug("No errors -> processing submit");
            if ((null != eventSearchForm.getIsEditEvent()) && eventSearchForm.getIsEditEvent()) {
            	eventSearchForm.setIsEditEvent(false); //turn this flag back off
                if (eventSearchForm.getEventId() != null) {
                	returnModelAndView = new ModelAndView(new RedirectView(request.getContextPath() + "/edit/event.htm?listid=" + eventSearchForm.getEventId()));
                } else {
                    //we are creating a new event
                	returnModelAndView = new ModelAndView(new RedirectView(request.getContextPath() + "/edit/event.htm"));
                }
            } else {
                Map model = doSearch(request, errors, eventSearchForm);
            	returnModelAndView = new ModelAndView(getSuccessView(), model);
            }
        }
        //put the data into the session in case we are leaving to edit, and then want to come back
        WebUtils.setSessionAttribute(request, SESSION_EVENT_SEARCH_FORM, eventSearchForm);
         
        return returnModelAndView;
    }

	private void logSearchQuery(EventSearchForm eventSearchForm) {
		if (log.isInfoEnabled()) {
        	StringBuffer msg = new StringBuffer("search,");
        	msg.append("where,\"").append(eventSearchForm.getWhere());
        	msg.append("\",when,");
        	if (eventSearchForm.getWhen() != null) {
        		msg.append(eventSearchForm.getWhen().getAsText());
        	}
        	msg.append("\",what,\"").append(eventSearchForm.getWhat()).append("\"");
        	log.info(msg);
        }
	}
	
    /**
     * They have put query string data on the request, so we ignore all else
     * @param request servlet request
     * @param eventSearchForm form object
     * @return ModelAndView a new ModelAndView
     * @throws Exception exception
     */
    private ModelAndView handleGet(HttpServletRequest request, EventSearchForm eventSearchForm) throws Exception {
    	//this request contains enough information to do a search right now
    	//use the same binder to get params of the query string as we were using for the POST
        //populate the form with parameters off the URL query string
    	searchHelper.bindGetParameters(request, eventSearchForm);
    	
    	//geocode
		String mapKey = getMessageSourceAccessor().getMessage(searchHelper.makeMapKeyCode(request));
    	searchHelper.geocode(mapKey, request, eventSearchForm);
    	
    	//save the form for redirect
    	//WebUtils.setSessionAttribute(request, SESSION_EVENT_SEARCH_FORM, eventSearchForm);
    	
    	return new ModelAndView(new RedirectView(request.getContextPath() + "/" + getSuccessView() + ".htm"));
    }
    
    @SuppressWarnings("unchecked")
	private Map doSearch( HttpServletRequest request, BindException errors, EventSearchForm eventSearchForm) {
    	Map model = errors.getModel();
    	List<Event> events = searchHelper.doSearch(request, model, eventSearchForm);
    	//if they were editing, we need to add the most recently edited point, regardless of whether
    	//it matches the search criteria, otherwise the user will be confused. TODO UI issue here
    	searchHelper.addNewlyCreatedEvent(request, events);
    	eventSearchForm.setSearchResults(JSONFormat.toJSON(events));
    	return model;
    }

}
