package com.tech4d.tsm.web.forgot;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;

import com.tech4d.tsm.dao.UserDao;
import com.tech4d.tsm.model.user.User;
import com.tech4d.tsm.model.user.UserNote;
import com.tech4d.tsm.service.EmailService;
import com.tech4d.tsm.util.PasswordUtil;
import com.tech4d.tsm.web.util.ConfirmationEmail;
import com.tech4d.tsm.web.util.UrlFormat;

/**
 * Help the user reset his/her password
 */
public class ForgotController extends SimpleFormController {
    private static final Log log = LogFactory.getLog(ForgotController.class);
	private UserDao userDao;
    private EmailService emailService;

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}
    public void setEmailService(EmailService emailService) {
		this.emailService = emailService;
	}

	@Override
	protected ModelAndView onSubmit(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors)
			throws Exception {
		ForgotForm forgotForm = (ForgotForm) command;
		User user = userDao.find(forgotForm.getUsername());

		/* Verify the username exists
    	 * NOTE we are doing validation here instead of the validator so that we don't have to 
    	 * go to the database twice to get the user object.
    	 */
		if (null == user) {
			//tell the user there was a problem 
            errors.rejectValue("username", "invalidUser.forgotForm.username",
            		new Object[]{forgotForm.getUsername()}, null);
            return new ModelAndView(getFormView(), errors.getModel());
		}
		//generate a retrieval key and save it in the user object
		String key = PasswordUtil.encrypt(forgotForm.getUsername() + Long.toString(System.currentTimeMillis()));
		if (null != user.getUserNote()) {
			user.getUserNote().setPasswordRetrievalKey(key);
		} else {
			UserNote userNote = new UserNote(key);
			user.setUserNote(userNote);
		}
		userDao.save(user);
		
		//create a link back 
		//first url encode the key
		key = urlEncode(key);
		StringBuffer resetLink = new StringBuffer(UrlFormat.getBasepath(request))
			.append("reset.htm?key=")
			.append(key);

		//send the notification email 
		MimeMessage message = emailService.createMimeMessage();
		ConfirmationEmail.makeForgotPassowrdMessage(message, user, resetLink.toString());
		emailService.sendMessage(message);
		
		return super.onSubmit(request, response, command, errors);
	}
	private String urlEncode(String key) {
		try {
			key = URLEncoder.encode(key, "US-ASCII");
		} catch (UnsupportedEncodingException e) {
			log.error(e);
		}
		return key;
	}
	
	

}
