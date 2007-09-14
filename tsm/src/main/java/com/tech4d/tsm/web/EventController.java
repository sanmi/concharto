package com.tech4d.tsm.web;

import com.tech4d.tsm.dao.TsEventDao;
import com.tech4d.tsm.model.TsEvent;
import com.tech4d.tsm.support.TsEventFactory;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class EventController extends SimpleFormController {
    private static final String SESSION_TSEVENT = "TSEVENT";
    TsEventDao tsEventDao;

    public void setTsEventDao(TsEventDao tsEventDao) {
        this.tsEventDao = tsEventDao;
    }
    
    @Override
    protected Object formBackingObject(HttpServletRequest request) throws Exception {
        Long id = ServletRequestUtils.getLongParameter(request, "listid");
        TsEvent tsEvent;
        TsEventForm tsEventForm;
        
        if (id != null) {
            tsEvent = this.tsEventDao.findById(id);
            //save it in the session
            WebUtils.setSessionAttribute(request, SESSION_TSEVENT, tsEvent);
            tsEventForm = TsEventFactory.getTsEventForm(tsEvent);
        } else {
            tsEventForm = new TsEventForm();
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
            tsEvent = TsEventFactory.updateTsEvent(tsEvent, tsEventForm);
        } else {
            tsEvent = TsEventFactory.createTsEvent(tsEventForm);
        }
        this.tsEventDao.saveOrUpdate(tsEvent);
        return new ModelAndView(getSuccessView());
    }

}
