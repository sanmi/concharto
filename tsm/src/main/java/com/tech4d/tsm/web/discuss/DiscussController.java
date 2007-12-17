package com.tech4d.tsm.web.discuss;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;

import com.tech4d.tsm.dao.EventDao;
import com.tech4d.tsm.model.Event;
import com.tech4d.tsm.model.WikiText;

public class DiscussController extends SimpleFormController {
	
	private static final String PARAM_ID = "id"; 
	private EventDao eventDao;
	
	public void setEventDao(EventDao eventDao) {
		this.eventDao = eventDao;
	}

	@Override
	protected Object formBackingObject(HttpServletRequest request)
			throws Exception {
        Long id = ServletRequestUtils.getLongParameter(request, PARAM_ID);
        WikiText discussion = eventDao.getDiscussion(id);
        if (null != discussion) {
        	return new WikiTextForm(discussion);
        } else {
        	return new WikiTextForm();
        }
	}


	@SuppressWarnings("unchecked")
	@Override
	protected ModelAndView showForm(HttpServletRequest request,
			HttpServletResponse response, BindException errors, Map controlModel)
			throws Exception {
		return new ModelAndView(getFormView(), errors.getModel());
	}

	@Override
	protected ModelAndView onSubmit(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors)
			throws Exception {
		
		//save the wikitext
		WikiTextForm discussion = (WikiTextForm) command;
		if (discussion.getShowPreview()) {
			return new ModelAndView(getFormView(), errors.getModel());
		}else {
			if (null == discussion.getWikiText().getId()) {
				//this is a new discussion and we need to add it to the event
				//todo put this in a transaction?
		        Long id = ServletRequestUtils.getLongParameter(request, PARAM_ID);
		        Event event = eventDao.findById(id);
		        event.setDiscussion(discussion.getWikiText());
		        eventDao.saveOrUpdate(event);
			} else {
				//this isn't new we just need to save the discussion
				eventDao.saveOrUpdate(discussion.getWikiText());
			}
			return super.onSubmit(request, response, command, errors);
		}
	}
	
	
}