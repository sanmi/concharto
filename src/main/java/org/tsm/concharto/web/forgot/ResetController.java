/*******************************************************************************
 * Copyright 2009 Time Space Map, LLC
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package org.tsm.concharto.web.forgot;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;
import org.tsm.concharto.dao.UserDao;
import org.tsm.concharto.model.user.User;
import org.tsm.concharto.util.PasswordUtil;
import org.tsm.concharto.web.util.AuthForm;
import org.tsm.concharto.web.util.AuthFormValidatorHelper;
import org.tsm.concharto.web.util.SessionHelper;


public class ResetController extends SimpleFormController{
	private static final String PARAM_KEY = "key";
    private static final Log log = LogFactory.getLog(ResetController.class);
	private UserDao userDao;
    private SessionHelper sessionHelper;
    
    public void setSessionHelper(SessionHelper sessionHelper) {
		this.sessionHelper = sessionHelper;
	}

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

	
	@Override
	protected ModelAndView showForm(HttpServletRequest request,
			HttpServletResponse response, BindException errors)
			throws Exception {
		//get the user for whom this password applies
		User user = getUser(request);
		if (user == null) {
			//tell the user there was a problem 
            errors.rejectValue("username", "invalidKey.resetForm.username");
            return new ModelAndView(getFormView(), errors.getModel());
		}
		return super.showForm(request, response, errors);
	}


	private User getUser(HttpServletRequest request)
			throws ServletRequestBindingException {
		String key = ServletRequestUtils.getStringParameter(request, PARAM_KEY);
		User user = userDao.getUserFromPasswordRetrievalKey(key);
		return user;
	}


	@Override
	protected ModelAndView onSubmit(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors)
			throws Exception {

		User user = getUser(request);
		if (user == null) {
			//tell the user there was a problem 
            errors.rejectValue("username", "invalidKey.resetForm.username");
            return new ModelAndView(getFormView(), errors.getModel());
		}
		//validate the password fields
		AuthForm authForm = (AuthForm) command;
		AuthFormValidatorHelper.validatePasswordFields(authForm, errors);
		if (errors.hasErrors()) {
            return new ModelAndView(getFormView(), errors.getModel());
		}
		
		//ok, now we can reset the password
		user.setPassword(PasswordUtil.encrypt(authForm.getPassword()));

		//and remove the key so the email can't be used again
		user.getUserNote().setPasswordRetrievalKey(null);
		userDao.save(user);
		log.info("user " + user.getUsername() + " has reset their password");
		
		//now log in
        sessionHelper.setUserInSession(request, user);
		
		return super.onSubmit(request, response, command, errors);
	}
	
	

}
