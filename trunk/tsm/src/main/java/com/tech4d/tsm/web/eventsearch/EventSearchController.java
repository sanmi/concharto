package com.tech4d.tsm.web.eventsearch;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractFormController;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.web.util.WebUtils;

import com.tech4d.tsm.model.Event;
import com.tech4d.tsm.util.KmlFormat;
import com.tech4d.tsm.web.util.DisplayTagHelper;
import com.tech4d.tsm.web.util.UrlFormat;

public class EventSearchController extends AbstractFormController {

	private static final String MODEL_KML = "kml";

	private static final String PARM_REAL_URI = "realURI";

	private static final Log log = LogFactory.getLog(EventSearchController.class);

    private String formView;
    private String successView;
    private String kmlView;
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
	public String getKmlView() {
		return kmlView;
	}
	public void setKmlView(String kmlView) {
		this.kmlView = kmlView;
	}

	public void setSearchHelper(SearchHelper searchHelper) {
		this.searchHelper = searchHelper;
	}

	@Override
    protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder)
            throws Exception {
		searchHelper.initBinder(request, binder);
        super.initBinder(request, binder);
    }

    private EventSearchForm getEventSearchForm(HttpServletRequest request) {
        return (EventSearchForm) WebUtils.getSessionAttribute(request, SearchHelper.SESSION_EVENT_SEARCH_FORM);
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
    
    /*
     * If there is data in the Where field, we try to geocode it
     * @see org.springframework.web.servlet.mvc.BaseCommandController#onBind(javax.servlet.http.HttpServletRequest, java.lang.Object, org.springframework.validation.BindException)
     */
    @Override
	protected void onBind(HttpServletRequest request, Object command, BindException errors) throws Exception {
    	EventSearchForm eventSearchForm = (EventSearchForm) command;
    	if ((!StringUtils.isEmpty(eventSearchForm.getWhere())) && (eventSearchForm.getMapCenter() == null)) {
        	searchHelper.geocode(makeMapKey(request), request, eventSearchForm);
    	}
		super.onBindAndValidate(request, command, errors);
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
    protected ModelAndView showForm(HttpServletRequest request,
			HttpServletResponse response, BindException errors)
			throws Exception {
		// if there is a form, we re-show the last search results
		EventSearchForm eventSearchForm = getEventSearchForm(request);
		if ((eventSearchForm != null) && (!errors.hasErrors())) {

			Map model = errors.getModel();
			// user clicks cancel from another screen. In that case, we just use the
			// last search
			Boolean doSearch = (Boolean) WebUtils.getSessionAttribute(request, SearchHelper.SESSION_DO_SEARCH_ON_SHOW);
			if (doSearch != null) {
                //we should do a search
                WebUtils.setSessionAttribute(request, SearchHelper.SESSION_DO_SEARCH_ON_SHOW, null);
				model = doSearch(request, errors, eventSearchForm);
			} else {
                //no search, just show what we did last time
                List<Event> events = (List<Event>) WebUtils
						.getSessionAttribute(request, SearchHelper.SESSION_EVENT_SEARCH_RESULTS);
				if (events != null) {
					searchHelper.prepareModel(model, events, (long) events.size(), 0);
				}
			}
			displayTagModelElements(model, request);

			return new ModelAndView(getFormView(), model);
		} else {
			//nothing to show
			Map model = errors.getModel();
            model.put(SearchHelper.MODEL_TOTAL_RESULTS, 0);
			return new ModelAndView(getFormView(), model);
		}
	}

	@SuppressWarnings("unchecked")
    @Override
    protected ModelAndView processFormSubmission(HttpServletRequest request, HttpServletResponse response,
                                                 Object command, BindException errors) throws Exception {
        EventSearchForm eventSearchForm = (EventSearchForm) command;
        //reset zoom and map center overrides, they are only used for get strings
        eventSearchForm.setMapCenterOverride(false);
        eventSearchForm.setZoomOverride(false);
        //reset the tag (which is a hidden form input) unless we are paging with display tag, in which
        //case we need to remember the tag
        if (null == DisplayTagHelper.getPageParameterId(request, SearchHelper.DISPLAYTAG_TABLE_ID)) {
            eventSearchForm.setUserTag(null);  
        }
        eventSearchForm.setKml(false);
        ModelAndView returnModelAndView;
        if (errors.hasErrors()) {
            if (log.isDebugEnabled()) {
                log.debug("Data binding errors: " + errors.getErrorCount());
            }
            //clear out the search results
            eventSearchForm.setSearchResults(null);
            Map model = errors.getModel();
            model.put(SearchHelper.MODEL_TOTAL_RESULTS, 0);
            returnModelAndView = new ModelAndView(getFormView(), model);
        } else {
            log.debug("No errors -> processing submit");
            if ((eventSearchForm.getIsAddEvent() != null) && (eventSearchForm.getIsAddEvent())) {
                //we are creating a new event
            	eventSearchForm.setIsAddEvent(false);
                //todo may want to inject the view here
                returnModelAndView = new ModelAndView(new RedirectView(request.getContextPath() + "/edit/event.htm?add"));
            } else if ((null != eventSearchForm.getEditEventId())) {
                //we are editing an event
            	long id = eventSearchForm.getEditEventId(); 
            	eventSearchForm.setEditEventId(null); //reset
                //todo may want to inject the view here
                returnModelAndView = new ModelAndView(new RedirectView(request.getContextPath() + "/edit/event.htm?id=" + id));
            } else {
            	//This is a normal search
            	//if this is a GET search, we must first process the query parameters
            	if ((!StringUtils.isEmpty(request.getQueryString()) && 
                		(null == DisplayTagHelper.getPageParameterId(request, SearchHelper.DISPLAYTAG_TABLE_ID)))) {
            		handleGet(request, eventSearchForm);
            	}
            	//now search
        		Map model = doSearch(request, errors, eventSearchForm);
        		
        		//fit the map to the search results if they specified a place name
        		if (!StringUtils.isEmpty(eventSearchForm.getWhere())) {
        			eventSearchForm.setLimitWithinMapBounds(false);
        		}
        		if (BooleanUtils.isTrue(eventSearchForm.getKml())) {
                	//they just want a kml file
        			ByteArrayOutputStream kmlOutputStream = new ByteArrayOutputStream();
        			OutputStreamWriter outputStreamWriter = new OutputStreamWriter(kmlOutputStream, "UTF-8");
        			List<Event> events = (List<Event>) model.get(SearchHelper.MODEL_EVENTS); 
        			KmlFormat.toKML(events, outputStreamWriter, 
        					"Concharto.com search results", 
        					"NOTE: all events are time coded, so you may need to adjust the time slider.",
        					UrlFormat.getBasepath(request));
        			//throw away the current model, we aren't rendering and just add the kml. 
        			//TODO - fix this hack
        			model = errors.getModel();
        			String utf8Kml = new String(kmlOutputStream.toByteArray(), "UTF8");
        			model.put(MODEL_KML, utf8Kml);
                    returnModelAndView = new ModelAndView(getKmlView(), model);
                } else {
                	//we are rendering to a normal page
            		// needed so the displaytag paging can work
            		displayTagModelElements(model, request);

                    // this is because of a wierd problem in our JSTL where ${pageContex.request.requestURI} yields
                    // WEB-INF/jsp/search/eventsearch.jsp instead of the expected /search/eventsearch.htm or 
                    // /list/event.htm.  So here we have to put it in the model for the jsp to use
                    model.put(PARM_REAL_URI, request.getRequestURI());
            		returnModelAndView = new ModelAndView(getSuccessView(), model);
                }
            }
        }
        
        //put the data into the session in case we are leaving to edit, and then want to come back
        WebUtils.setSessionAttribute(request, SearchHelper.SESSION_EVENT_SEARCH_FORM, eventSearchForm);
        return returnModelAndView;
    }

	@SuppressWarnings("unchecked")
	private void displayTagModelElements(Map model, HttpServletRequest request) {
		model.put(DisplayTagHelper.MODEL_PAGESIZE, SearchHelper.DISPLAYTAG_PAGESIZE);
		model.put(DisplayTagHelper.MODEL_REQUEST_URI, request.getRequestURI());
	}

	private void logSearchQuery(EventSearchForm eventSearchForm, long time) {
		if (log.isInfoEnabled()) {
        	StringBuffer msg = new StringBuffer("search,");
        	msg.append("where,\"").append(eventSearchForm.getWhere());
        	msg.append("\",when,\"");
        	if (eventSearchForm.getWhen() != null) {
        		msg.append(eventSearchForm.getWhen().getAsText());
        	}
        	msg.append("\",what,\"").append(eventSearchForm.getWhat()).append("\"");
        	msg.append(", time, ").append(time);
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
    private void handleGet(HttpServletRequest request, EventSearchForm eventSearchForm) throws Exception {
    	//this request contains enough information to do a search right now
    	//use the same binder to get params of the query string as we were using for the POST
        //populate the form with parameters off the URL query string
    	searchHelper.bindGetParameters(request, eventSearchForm);
    	
    	//geocode if there is no map center and we aren't looking up by ID
		if ((null == eventSearchForm.getDisplayEventId()) && (null == eventSearchForm.getMapCenter())) {
			String mapKey = makeMapKey(request);
	    	searchHelper.geocode(mapKey, request, eventSearchForm);
		}
    }

	private String makeMapKey(HttpServletRequest request) {
        return getMessageSourceAccessor().getMessage(searchHelper.makeMapKeyCode(request));
	}
    
    @SuppressWarnings("unchecked")
	private Map doSearch( HttpServletRequest request, BindException errors, EventSearchForm eventSearchForm) {
		long time = System.currentTimeMillis(); 
    	Map model = errors.getModel();
		String mapKey = makeMapKey(request);
    	List<Event> events = searchHelper.doSearch(mapKey, request, model, eventSearchForm);
    	//save the results for later "show forms", e.g. in the event we click edit but then hit 'cancel' 
        WebUtils.setSessionAttribute(request, SearchHelper.SESSION_EVENT_SEARCH_RESULTS, events);        
		logSearchQuery(eventSearchForm, System.currentTimeMillis()-time);
    	return model;
    }

}
