package com.tech4d.tsm.web.signup;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;

import com.tech4d.tsm.dao.UserDao;
import com.tech4d.tsm.model.user.Role;
import com.tech4d.tsm.model.user.User;
import com.tech4d.tsm.service.EmailService;
import com.tech4d.tsm.util.PasswordUtil;
import com.tech4d.tsm.web.util.ConfirmationEmail;
import com.tech4d.tsm.web.util.SessionHelper;
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
