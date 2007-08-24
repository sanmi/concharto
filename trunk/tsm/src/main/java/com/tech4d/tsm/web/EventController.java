package com.tech4d.tsm.web;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;

import com.tech4d.tsm.dao.EventDao;
import com.tech4d.tsm.model.Event;

public class EventController extends SimpleFormController {
    EventDao eventDao;

    public void setEventDao(EventDao eventDao) {
        this.eventDao = eventDao;
    }
    
    @Override
    protected Object formBackingObject(HttpServletRequest request) throws Exception {
        Long id = ServletRequestUtils.getLongParameter(request, "listid");
        Event event;
        
        if (id != null) {
            event = this.eventDao.findById(id);
        } else {
            event = new Event();
        }
        return event; 
    }

    @Override
    protected ModelAndView onSubmit(Object command) throws Exception {
        Event event = (Event)command;
        this.eventDao.saveOrUpdate(event);
        return new ModelAndView(getSuccessView());
    }
    

}
