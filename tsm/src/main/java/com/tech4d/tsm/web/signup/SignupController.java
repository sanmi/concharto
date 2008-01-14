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
import com.tech4d.tsm.model.Role;
import com.tech4d.tsm.model.User;
import com.tech4d.tsm.service.EmailService;
import com.tech4d.tsm.util.PasswordUtil;
/**
 * Signup a new user
 */
public class SignupController extends SimpleFormController {
    private static final String ROLE_EDIT = "edit";
    private UserDao userDao;
    private EmailService emailService;

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    public void setEmailService(EmailService emailService) {
		this.emailService = emailService;
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
        //give them the default 'edit' role
        List<Role> roles = new ArrayList<Role>();
        Role editRole = userDao.getRole(ROLE_EDIT);
        roles.add(editRole);
        user.setRoles(roles);
        userDao.save(user);
        
        sendConfirmation(user);
    }

	private void sendConfirmation(User user) {
		MimeMessage message = emailService.createMimeMessage();
		NewAccountConfirmationEmail.makeConfirmationMessage(message, user);
		emailService.sendMessage(message);
	}

}
