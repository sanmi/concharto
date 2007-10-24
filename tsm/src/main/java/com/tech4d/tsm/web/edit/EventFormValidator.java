package com.tech4d.tsm.web.edit;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.tech4d.tsm.web.util.ValidationHelper;


public class EventFormValidator implements Validator {

    @SuppressWarnings("unchecked")
	public boolean supports(Class clazz) {
        return EventForm.class.equals(clazz);
    }

    public void validate(Object target, Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "summary", "empty.event.summary");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "when", "empty.event.when");
        
        ValidationHelper.rejectIfTooLong(errors, "description", EventForm.SZ_DESCRIPTION, "tooLong");

        //These should never trigger if the HTML is coded correctly
        ValidationHelper.rejectIfTooLong(errors, "summary", EventForm.SZ_SUMMARY, "tooLong");
        ValidationHelper.rejectIfTooLong(errors, "tags", EventForm.SZ_TAGS, "tooLong");
        ValidationHelper.rejectIfTooLong(errors, "where", EventForm.SZ_WHERE, "tooLong");
        ValidationHelper.rejectIfTooLong(errors, "source", EventForm.SZ_SOURCE, "tooLong");
    }
    
}
