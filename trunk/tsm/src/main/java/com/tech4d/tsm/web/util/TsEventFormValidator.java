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
        ValidationUtils.rejectIfEmpty(errors, "summary", "summary.empty");
        ValidationUtils.rejectIfEmpty(errors, "when", "when.empty");
    }
    
}
