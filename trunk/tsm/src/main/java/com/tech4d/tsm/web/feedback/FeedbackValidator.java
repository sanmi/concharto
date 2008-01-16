package com.tech4d.tsm.web.feedback;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

public class FeedbackValidator implements Validator {

	@SuppressWarnings("unchecked")
	public boolean supports(Class clazz) {
		return clazz == FeedbackForm.class;
	}

	public void validate(Object target, Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "empty.feedback.name");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email", "empty.feedback.email");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "subject", "empty.feedback.subject");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "body", "empty.feedback.body");
	}
}
