package com.tech4d.tsm.web.util;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.tech4d.tsm.web.EventSearchForm;

public class EventSearchFormValidator implements Validator{

    public boolean supports(Class clazz) {
        
        return EventSearchForm.class.equals(clazz);
    }

    public void validate(Object target, Errors errors) {
        EventSearchForm eventSearchForm = (EventSearchForm) target;
        //if the geocode failed        
        if ((eventSearchForm.getIsGeocodeSuccess() != null) && !eventSearchForm.getIsGeocodeSuccess()) {
            errors.rejectValue("where", 
                    "failedGeocode.eventSearch.where", 
                    new Object[]{eventSearchForm.getWhere()}, null);
        }
    }

}
