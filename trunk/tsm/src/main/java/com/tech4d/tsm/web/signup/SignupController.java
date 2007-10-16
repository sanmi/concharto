package com.tech4d.tsm.web.signup;

import java.security.NoSuchAlgorithmException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;

import com.tech4d.tsm.dao.UserDao;
import com.tech4d.tsm.model.User;
import com.tech4d.tsm.util.PasswordUtil;

public class SignupController extends SimpleFormController {
    private UserDao userDao;

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
        SignupForm form = (SignupForm) command;
        
        saveUser(form);
        return super.onSubmit(request, response, command, errors);
    }

    private void saveUser(SignupForm form) throws NoSuchAlgorithmException {
        String hashedPassword = PasswordUtil.encrypt(form.getPassword());
        User user = new User(form.getUsername(), hashedPassword, form.getEmail());
        userDao.save(user);
    }

}
