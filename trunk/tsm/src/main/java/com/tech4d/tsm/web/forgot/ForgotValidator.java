package com.tech4d.tsm.web.forgot;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

public class ForgotValidator implements Validator {

    @SuppressWarnings("unchecked")
	public boolean supports(Class clazz) {
        return ForgotForm.class.equals(clazz);
    }

    public void validate(Object target, Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "username", "empty.authForm.username");
    }
}