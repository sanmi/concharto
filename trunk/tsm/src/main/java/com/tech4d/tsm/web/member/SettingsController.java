/*******************************************************************************
 * ***** BEGIN LICENSE BLOCK *****
 * Version: MPL 1.1
 * 
 * The contents of this file are subject to the Mozilla Public License Version 
 * 1.1 (the "License"); you may not use this file except in compliance with 
 * the License. You may obtain a copy of the License at 
 * http://www.mozilla.org/MPL/
 * 
 * Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 * for the specific language governing rights and limitations under the
 * License.
 * 
 * The Original Code is Concharto.
 * 
 * The Initial Developer of the Original Code is
 * Time Space Map, LLC
 * Portions created by the Initial Developer are Copyright (C) 2007 - 2009
 * the Initial Developer. All Rights Reserved.
 * 
 * Contributor(s):
 * Time Space Map, LLC
 * 
 * ***** END LICENSE BLOCK *****
 ******************************************************************************/
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
import com.tech4d.tsm.model.user.User;
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
        boolean passwordChanged = false;
        if (!SettingsFormValidator.allPasswordFieldsAreEmpty(settingsForm)) {
    		passwordChanged = true;
        	//ok we have to validate the original password
        	if (!PasswordUtil.isPasswordValid(settingsForm.getExistingPassword(), user.getPassword())) {
                //tell the user there was a problem and let the default form handle the rest
                errors.rejectValue("existingPassword", "invalidUserPasswd.authForm.existingPassword");
                model.put(MODEL_USER, safeUser(user));
                model.put(MODEL_SUCCESS, false);
                return new ModelAndView(getFormView(),model);
        	} 
        }
        if (user != null) {
            //ok we can change the information
        	if (passwordChanged) {
                user.setPassword(PasswordUtil.encrypt(settingsForm.getPassword()));
        	}
            user.setEmail(settingsForm.getEmail());
            userDao.save(user);
            model.put(MODEL_SUCCESS, true);
            model.put(MODEL_USER, safeUser(user));
        }  
        return new ModelAndView(getSuccessView(),model);
    }
    
    private User safeUser(User user) {
        user.setPassword(null);
        return user;
    }

}
