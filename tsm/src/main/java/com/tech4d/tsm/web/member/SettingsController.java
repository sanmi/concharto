package com.tech4d.tsm.web.member;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;
import org.springframework.web.util.WebUtils;

import com.tech4d.tsm.auth.AuthConstants;
import com.tech4d.tsm.dao.UserDao;
import com.tech4d.tsm.model.User;
import com.tech4d.tsm.util.PasswordUtil;

public class SettingsController extends SimpleFormController {
    private static final String MODEL_SUCCESS = "success";
    private static final String MODEL_USER = "user";
    private UserDao userDao;

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }
  
    @Override
    protected Object formBackingObject(HttpServletRequest request) throws Exception {
        SettingsForm settingsForm = new SettingsForm();
        User user = userDao.find((String) WebUtils.getSessionAttribute(request, AuthConstants.SESSION_AUTH_USERNAME));
        if (user != null) {
            //user can be null when navigating directly to this page without logging in.  
            //It shouldn't normally happen
            settingsForm.setEmail(user.getEmail());
            settingsForm.setUsername(user.getUsername());
        }
        return settingsForm;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected ModelAndView showForm(HttpServletRequest request, HttpServletResponse response, BindException errors, Map controlModel) throws Exception {
        //get the user object so we can show it to them
        Map model = errors.getModel();
        User user = userDao.find((String) WebUtils.getSessionAttribute(request, AuthConstants.SESSION_AUTH_USERNAME));
        if (user != null) {
            //user can be null when navigating directly to this page without logging in.  
            //It shouldn't normally happen
            model.put(MODEL_USER, safeUser(user));
        }
        return new ModelAndView(getFormView(), model);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
        Map model = errors.getModel();
        SettingsForm settingsForm = (SettingsForm) command;
        //TODO fix this we are getting the user three times from the database! 
        //Once in formBackingObject and once showForm and now here.
        User user = userDao.find((String) WebUtils.getSessionAttribute(request, AuthConstants.SESSION_AUTH_USERNAME));
        
    	//the user can choose to fill out none or all of the password fields
    	//if they don't enter any password fields, we don't need to validate them
        boolean passwordOk;
    	if (SettingsFormValidator.allPasswordFieldsAreEmpty(settingsForm)) {
    		//no need to validate
    		passwordOk = true;
    	} else {
            //verify the old password is correct
    		passwordOk = (PasswordUtil.isPasswordValid(settingsForm.getExistingPassword(), user.getPassword()));
    	}

        if ((user != null) && (passwordOk)) {
            //ok we can change the information
            user.setPassword(PasswordUtil.encrypt(settingsForm.getPassword()));
            user.setEmail(settingsForm.getEmail());
            userDao.save(user);
            model.put(MODEL_SUCCESS, true);
            model.put(MODEL_USER, safeUser(user));
            return new ModelAndView(getSuccessView(),model);
        } else {
            //tell the user there was a problem and let the default form handle the rest
            errors.rejectValue("password", "invalidUserPasswd.authForm.username");
            model.put(MODEL_USER, safeUser(user));
            model.put(MODEL_SUCCESS, false);
            return super.onSubmit(request, response, command, errors);
        }  
    }
    
    private User safeUser(User user) {
        user.setPassword(null);
        return user;
    }

}
