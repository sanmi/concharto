package com.tech4d.tsm.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;
import org.springframework.web.util.WebUtils;

import com.tech4d.tsm.dao.TsEventDao;
import com.tech4d.tsm.model.TsEvent;
import com.tech4d.tsm.model.geometry.TimeRange;
import com.tech4d.tsm.web.util.TimeRangePropertyEditor;

public class EventController extends SimpleFormController {
    private static final String SESSION_TSEVENT = "TSEVENT";
    TsEventDao tsEventDao;

    public void setTsEventDao(TsEventDao tsEventDao) {
        this.tsEventDao = tsEventDao;
    }
    
    @Override
    protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder)
            throws Exception {
        binder.registerCustomEditor(TimeRange.class, new TimeRangePropertyEditor());
        super.initBinder(request, binder);
    }
    
    private EventSearchForm getEventSearchForm(HttpServletRequest request) {
        return (EventSearchForm) WebUtils.getSessionAttribute(request, EventSearchController.SESSION_EVENT_SEARCH_FORM);
    }

    @Override
    protected Object formBackingObject(HttpServletRequest request) throws Exception {
        Long id = ServletRequestUtils.getLongParameter(request, "listid");
        TsEvent tsEvent;
        TsEventForm tsEventForm; 
        EventSearchForm eventSearchForm = getEventSearchForm(request);
        
        if (id != null) {
            tsEvent = this.tsEventDao.findById(id);
            //TODO don't use session if possible!!!
            //save it in the session
            WebUtils.setSessionAttribute(request, SESSION_TSEVENT, tsEvent);
            tsEventForm = com.tech4d.tsm.web.TsEventFormFactory.getTsEventForm(tsEvent);
            if (eventSearchForm != null) {
                tsEventForm.setSearchResults(eventSearchForm.getSearchResults());
            }
        } else {
            tsEventForm = new TsEventForm();
            if (eventSearchForm != null) {
                tsEventForm.setLng(eventSearchForm.getMapCenter().getX());
                tsEventForm.setLat(eventSearchForm.getMapCenter().getY());
                tsEventForm.setZoomLevel(eventSearchForm.getMapZoom());
                tsEventForm.setSearchResults(eventSearchForm.getSearchResults());
            } 
        }
        return tsEventForm;
    }

    @Override
    protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
        TsEventForm tsEventForm = (TsEventForm)command;
        TsEvent tsEvent;
        if (tsEventForm.getId() != null) {
            //get the tsEvent from the session
            tsEvent = (TsEvent) WebUtils.getSessionAttribute(request, SESSION_TSEVENT);
            tsEvent = com.tech4d.tsm.web.TsEventFormFactory.updateTsEvent(tsEvent, tsEventForm);
        } else {
            tsEvent = com.tech4d.tsm.web.TsEventFormFactory.createTsEvent(tsEventForm);
        }
        this.tsEventDao.saveOrUpdate(tsEvent);
        return new ModelAndView(getSuccessView());
    }

}
