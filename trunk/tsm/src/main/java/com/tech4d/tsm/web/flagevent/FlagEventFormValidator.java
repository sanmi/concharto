package com.tech4d.tsm.web.flagevent;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.tech4d.tsm.model.Flag;
import com.tech4d.tsm.web.util.ValidationHelper;

public class FlagEventFormValidator implements Validator{

	@SuppressWarnings("unchecked")
	public boolean supports(Class clazz) {
		return FlagEventForm.class.equals(clazz);
	}

	public void validate(Object target, Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "reason", "empty.flagEventForm.reason");
        ValidationHelper.rejectIfTooLong(errors, "comment", Flag.SZ_COMMENT, "tooLong");
	}
}
