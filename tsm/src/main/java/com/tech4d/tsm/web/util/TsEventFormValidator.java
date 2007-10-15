package com.tech4d.tsm.web.util;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.tech4d.tsm.web.TsEventForm;

public class TsEventFormValidator implements Validator {

    public boolean supports(Class clazz) {
        
        return TsEventForm.class.equals(clazz);
    }

    public void validate(Object target, Errors errors) {
        TsEventForm tsEventForm = (TsEventForm) target; 
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "summary", "summary.empty");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "when", "when.empty");
        
        if (tsEventForm.getDescription().length() >= TsEventForm.SZ_DESCRIPTION) {
            errors.rejectValue("description", 
                    "exceededMaxSize.event.description", 
                    new Object[]{TsEventForm.SZ_DESCRIPTION, tsEventForm.getDescription().length()}, 
                    null);
        }
    }
    
}
