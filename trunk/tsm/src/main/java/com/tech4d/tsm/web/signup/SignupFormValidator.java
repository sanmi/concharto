package com.tech4d.tsm.web.signup;

import org.apache.commons.lang.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.tech4d.tsm.dao.UserDao;

public class SignupFormValidator implements Validator {
    private UserDao userDao;
    
    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    public boolean supports(Class clazz) {
        return SignupForm.class.equals(clazz);
    }

    public void validate(Object target, Errors errors) {
        SignupForm signupForm = (SignupForm) target;
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "empty.authForm.password");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "username", "empty.authForm.username");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email", "empty.signupForm.email");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "passwordConfirm", "empty.signupForm.passwordConfirm");
        
        if (!signupForm.getAgreeToTermsOfService()) {
            errors.rejectValue("agreeToTermsOfService", "mustAgree.signupForm.agreeToTermsOfService");
        }
        
        if (StringUtils.isEmpty(signupForm.getPassword()) && StringUtils.isEmpty(signupForm.getPasswordConfirm())) {
            if (!signupForm.getPassword().equals(signupForm.getPasswordConfirm())) {
                errors.rejectValue("password", "notSame.signupForm.password");
            }
        }
        
        //check to see that the username isn't already taken
        if (null != userDao.find(signupForm.getUsername())) {
            errors.rejectValue("username", "usernameTaken.signupForm.username");
        }
    }
}
