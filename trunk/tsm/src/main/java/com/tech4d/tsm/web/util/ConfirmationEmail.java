package com.tech4d.tsm.web.util;

import java.io.UnsupportedEncodingException;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.tech4d.tsm.model.user.User;

/**
 * Create an new account confirmation email.
 * TODO internationalize the text here.
 */
public class ConfirmationEmail {
	private static final Log log = LogFactory.getLog(ConfirmationEmail.class);

    private static final String FROM_NAME = "Concharto Notifications";
	private static final String FROM_ADDRESS = "notify@concharto.com";
	private static final String WELCOME_SUBJECT = "Welcome to Concharto";
	private static String PARAM_NAME = ":name";
	private static String CONTACT_US = 
		"If you would like to contact us, our feedback form can be found at http://www.concharto.com/feedback.htm\n\n";
	private static String  WELCOME_MESSAGE = "Hello " + PARAM_NAME + ",\n\n" + 
	"Welcome to the Concharto community! \n\n" +
	"The username you registered with is: :name\n\n" +
//	private static String PARAM_CONFIRMATION = ":confirmation";
//	"Please click on this link to confirm your registration: \n" +
//	"http://www.concharto.com/member/confirm.htm?id=" + PARAM_CONFIRMATION + " \n\n" +
//	"If the above link is not clickable, you will need to manually copy it into your browser.\n\n" +
	"You can find out more about us at http://wiki.concharto.com/wiki/About.\n\n" + 
	CONTACT_US;
//  "If you were not expecting this email, just ignore it, no further action is required to terminate the request.\n"
	;
	public static MimeMessage makeNewAccountConfirmationMessage(MimeMessage message, User user) {
    	String messageText = StringUtils.replace(WELCOME_MESSAGE, PARAM_NAME, user.getUsername());
    	return makeMessage(message, user, WELCOME_SUBJECT, messageText);
	}
	
	private static final String FORGOT_SUBJECT = "Change password on Concharto";
	private static final String PARAM_FORGOT_RESET_LINK = ":resetlink";
	private static final String FORGOT_MESSAGE = 
		"Hello " + PARAM_NAME + ",\n\n" + 
		"We are sorry you are having trouble logging in.  Please visit the following link " +
		"to reset your password\n\n " + PARAM_FORGOT_RESET_LINK + 
		"\n\n" + CONTACT_US;
	
	public static MimeMessage makeForgotPassowrdMessage(
			MimeMessage message, User user, String resetLink) {
    	String messageText = StringUtils.replace(FORGOT_MESSAGE, PARAM_NAME, user.getUsername());
    	messageText = StringUtils.replace(messageText, PARAM_FORGOT_RESET_LINK, resetLink);
    	return makeMessage(message, user, FORGOT_SUBJECT, messageText);
	}
	
	public static MimeMessage makeMessage(MimeMessage message, User user, 
			String subject, String messageText) {
    	InternetAddress from = new InternetAddress();
    	from.setAddress(FROM_ADDRESS);
    	InternetAddress to = new InternetAddress();
    	to.setAddress(user.getEmail());
    	try {
			from.setPersonal(FROM_NAME);
			message.addRecipient(Message.RecipientType.TO, to);
			message.setSubject(subject);
			message.setText(messageText);
			message.setFrom(from);
    	} catch (UnsupportedEncodingException e) {
			log.error(e);
		} catch (MessagingException e) {
			log.error(e);
		}
		return message;
	}
}
