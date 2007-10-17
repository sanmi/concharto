package com.tech4d.tsm.web.signup;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.tech4d.tsm.dao.UserDao;
import com.tech4d.tsm.web.util.AuthFormValidatorHelper;

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
        AuthFormValidatorHelper.validate(signupForm, errors);
        
        if (!signupForm.getAgreeToTermsOfService()) {
            errors.rejectValue("agreeToTermsOfService", "mustAgree.signupForm.agreeToTermsOfService");
        }
        
        //check to see that the username isn't already taken
        if (null != userDao.find(signupForm.getUsername())) {
            errors.rejectValue("username", "usernameTaken.signupForm.username");
        }
    }
}
