package com.tech4d.tsm.web.login;

import java.security.NoSuchAlgorithmException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.tech4d.tsm.dao.UserDao;
import com.tech4d.tsm.model.User;
import com.tech4d.tsm.util.PasswordUtil;

public class LoginFormValidator implements Validator {
    private static final Log log = LogFactory.getLog(LoginFormValidator.class);
    private UserDao userDao;
    
    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    public boolean supports(Class clazz) {
        return LoginForm.class.equals(clazz);
    }

    public void validate(Object target, Errors errors) {
        LoginForm loginForm = (LoginForm) target;
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "empty.authForm.password");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "username", "empty.authForm.username");
        
        String hashedPassword = null;
        try {
            hashedPassword = PasswordUtil.encrypt(loginForm.getPassword());            
        } catch (NoSuchAlgorithmException e) {
            log.error("Exception while encrypting: " + e);
        }
        User user = userDao.find(loginForm.getUsername());
        
        if ((user == null) || (hashedPassword == null) || !user.getPassword().equals(hashedPassword)) {
            errors.rejectValue("username", "invalidUserPasswd.loginForm.username");
        }
    }
}
