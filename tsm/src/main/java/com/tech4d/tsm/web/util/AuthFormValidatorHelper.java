package com.tech4d.tsm.web.util;

import org.apache.commons.lang.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;

public class AuthFormValidatorHelper {
    private static final int MIN_PASSWORD_SIZE = 4;

	public static void validate(AuthForm authForm, Errors errors) {
    	validateEmail(authForm, errors);
    	validateUsernamePasswordFields(authForm, errors);
    }
    
    public static void validateEmail(AuthForm authForm, Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email", "empty.authForm.email");
    }

    public static void validateUsernamePasswordFields(AuthForm authForm, Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "username", "empty.authForm.username");
        validatePasswordFields(authForm, errors);
    }
    
    public static void validatePasswordFields(AuthForm authForm, Errors errors) {
    	ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "empty.authForm.password");
    	ValidationUtils.rejectIfEmptyOrWhitespace(errors, "passwordConfirm", "empty.authForm.passwordConfirm");
    	if (!StringUtils.isEmpty(authForm.getPassword()) && !StringUtils.isEmpty(authForm.getPasswordConfirm())) {
    		if (!authForm.getPassword().equals(authForm.getPasswordConfirm())) {
    			errors.rejectValue("password", "notSame.authForm.password");
    		}
        	if (authForm.getPassword().length() < MIN_PASSWORD_SIZE) {
        		errors.rejectValue("password", "tooSmall.authForm.password");
        	}
    	}
    }
    
}
