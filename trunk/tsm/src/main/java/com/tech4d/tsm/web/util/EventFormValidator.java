package com.tech4d.tsm.web.util;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.tech4d.tsm.web.EventForm;

public class EventFormValidator implements Validator {

    public boolean supports(Class clazz) {
        
        return EventForm.class.equals(clazz);
    }

    public void validate(Object target, Errors errors) {
        EventForm eventForm = (EventForm) target;
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "summary", "summary.empty");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "when", "when.empty");
        
        if (eventForm.getDescription().length() >= EventForm.SZ_DESCRIPTION) {
            errors.rejectValue("description", 
                    "exceededMaxSize.event.description", 
                    new Object[]{EventForm.SZ_DESCRIPTION, eventForm.getDescription().length()},
                    null);
        }
    }
    
}
