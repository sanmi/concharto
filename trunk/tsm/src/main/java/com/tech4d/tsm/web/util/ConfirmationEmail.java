package com.tech4d.tsm.web.util;

import java.io.UnsupportedEncodingException;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.commons.lang.StringUtils;

import com.tech4d.tsm.model.User;

/**
 * Create an new account confirmation email.
 * TODO internationalize the text here.
 */
public class ConfirmationEmail {
	private static final String WELCOME_SUBJECT = "Welcome to Time Space Map";
	private static String PARAM_NAME = ":name";
	private static String  WELCOME_MESSAGE = "Hello " + PARAM_NAME + ",\n\n" + 
	"Welcome to the Time Space Map community! \n\n" +
	"The username you registered with is: :name\n\n" +
//	private static String PARAM_CONFIRMATION = ":confirmation";
//	"Please click on this link to confirm your registration: \n" +
//	"http://www.timespacemap.com/member/confirm.htm?id=" + PARAM_CONFIRMATION + " \n\n" +
//	"If the above link is not clickable, you will need to manually copy it into your browser.\n\n" +
	"You can find out more about us at http://wiki.timespacemap.com/About.\n\n"; 
//  "If you were not expecting this email, just ignore it, no further action is required to terminate the request.\n"
	;
	
	
	public static MimeMessage makeNewAccountConfirmationMessage(MimeMessage message, User user) {
    	InternetAddress from = new InternetAddress();
    	from.setAddress("notify@timespacemap.com");
    	InternetAddress to = new InternetAddress();
    	to.setAddress(user.getEmail());
    	try {
			from.setPersonal("Time Space Map Notifications");
			message.addRecipient(Message.RecipientType.TO, to);
			message.setSubject(WELCOME_SUBJECT);
	    	String messageText = StringUtils.replace(WELCOME_MESSAGE, PARAM_NAME, user.getUsername());
			message.setText(messageText);
			message.setFrom(from);
    	} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return message;
	}
}
