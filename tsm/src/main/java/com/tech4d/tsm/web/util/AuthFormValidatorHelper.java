package com.tech4d.tsm.web.util;

import org.apache.commons.lang.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;

public class AuthFormValidatorHelper {
    public static void validate(AuthForm authForm, Errors errors) {
    	validateEmail(authForm, errors);
    	validatePasswordFields(authForm, errors);
    }
    
    public static void validateEmail(AuthForm authForm, Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email", "empty.authForm.email");
    }

    public static void validatePasswordFields(AuthForm authForm, Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "username", "empty.authForm.username");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "empty.authForm.password");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "passwordConfirm", "empty.authForm.passwordConfirm");
        if (!StringUtils.isEmpty(authForm.getPassword()) && !StringUtils.isEmpty(authForm.getPasswordConfirm())) {
            if (!authForm.getPassword().equals(authForm.getPasswordConfirm())) {
                errors.rejectValue("password", "notSame.authForm.password");
            }
        }
    	
    }
    
}
