package com.tech4d.tsm.web.spotlight;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.tech4d.tsm.model.Spotlight;

public class SpotlightValidator implements Validator {

	@SuppressWarnings("unchecked")
	public boolean supports(Class clazz) {
		return clazz == Spotlight.class;
	}

	public void validate(Object target, Errors errors) {
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "link", "empty.spotlight.link");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "label", "empty.spotlight.label");
	}
}
