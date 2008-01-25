package com.tech4d.tsm.web.login;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.tech4d.tsm.web.signup.SignupForm;

public class LoginFormValidator implements Validator {
	

    @SuppressWarnings("unchecked")
	public boolean supports(Class clazz) {
        return (SignupForm.class.equals(clazz));
    }

    public void validate(Object target, Errors errors) {
        ((SignupForm)target).setFromController(SignupForm.FORM_LOGIN);
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "empty.authForm.password");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "username", "empty.authForm.username");
        
    }
}
