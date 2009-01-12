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
package org.tsm.concharto.web.signup;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;
import org.tsm.concharto.dao.UserDao;
import org.tsm.concharto.model.user.Role;
import org.tsm.concharto.model.user.User;
import org.tsm.concharto.service.EmailService;
import org.tsm.concharto.util.PasswordUtil;
import org.tsm.concharto.web.util.ConfirmationEmail;
import org.tsm.concharto.web.util.SessionHelper;

/**
 * Signup a new user
 */
public class SignupController extends SimpleFormController {
    private UserDao userDao;
    private EmailService emailService;
    private SessionHelper sessionHelper;
    
    public void setSessionHelper(SessionHelper sessionHelper) {
		this.sessionHelper = sessionHelper;
	}

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    public void setEmailService(EmailService emailService) {
		this.emailService = emailService;
	}

	@Override
    protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
        SignupForm form = (SignupForm) command;
        
        User user = saveUser(form);

        //now log them in
        sessionHelper.setUserInSession(request, user);

        //now go where we were originally heading
        return LoginSignupHelper.continueToRequestedUrl(request);
    }


    private User saveUser(SignupForm form) throws NoSuchAlgorithmException {
        String hashedPassword = PasswordUtil.encrypt(form.getPassword());
        User user = new User(form.getUsername(), hashedPassword, form.getEmail());
        //give them the default 'edit' role
        List<Role> roles = new ArrayList<Role>();
        roles.add(Role.ROLE_EDIT);
        user.setRoles(roles);
        userDao.save(user);
        
        sendConfirmation(user);
        
        return user;
    }

	private void sendConfirmation(User user) {
		MimeMessage message = emailService.createMimeMessage();
		ConfirmationEmail.makeNewAccountConfirmationMessage(message, user);
		emailService.sendMessage(message);
	}

}
