package com.tech4d.tsm.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;
import org.springframework.web.util.WebUtils;

import com.tech4d.tsm.dao.EventDao;
import com.tech4d.tsm.model.Event;
import com.tech4d.tsm.support.EventFactory;

public class EventController extends SimpleFormController {
    private static final String SESSION_EVENT = "EVENT";
    EventDao eventDaoHib;

    public void setEventDao(EventDao eventDaoHib) {
        this.eventDaoHib = eventDaoHib;
    }
    
    @Override
    protected Object formBackingObject(HttpServletRequest request) throws Exception {
        Long id = ServletRequestUtils.getLongParameter(request, "listid");
        Event event;
        EventForm eventForm;
        
        if (id != null) {
            event = this.eventDaoHib.findById(id);
            //save it in the session
            WebUtils.setSessionAttribute(request, SESSION_EVENT, event);
            eventForm = EventFactory.getEventForm(event);
        } else {
            eventForm = new EventForm();
        }
        return eventForm;
    }

    @Override
    protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
        EventForm eventForm = (EventForm)command;
        Event event = null;
        if (eventForm.getId() != null) {
            //get the event from the session
            event = (Event) WebUtils.getSessionAttribute(request, SESSION_EVENT);
            event = EventFactory.updateEvent(event, eventForm);
        } else {
            event = EventFactory.createEvent(eventForm);
        }
        this.eventDaoHib.saveOrUpdate(event);
        return new ModelAndView(getSuccessView());
    }

}
