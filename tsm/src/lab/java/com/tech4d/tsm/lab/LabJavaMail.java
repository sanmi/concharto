package com.tech4d.tsm.lab;

import java.io.UnsupportedEncodingException;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.commons.lang.StringUtils;
import org.junit.Test;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import com.tech4d.tsm.model.user.User;
import com.tech4d.tsm.util.PasswordUtil;

public class LabJavaMail {

	private static final String WELCOME_SUBJECT = "Confirm your registration with Time Space Map";
	private static String PARAM_NAME = ":name";
	private static String PARAM_CONFIRMATION = ":confirmation";
	private static String  WELCOME_MESSAGE = "Hello " + PARAM_NAME + ",\n\n" + 
	"Welcome to the Time Space Map community! \n\n" +
	"Please click on this link to confirm your registration: \n" +
	"http://www.timespacemap.com/member/confirm.htm?id=" + PARAM_CONFIRMATION + " \n\n" + 
	"You can find out more about us at http://wiki.timespacemap.com/wiki/About.\n\n" +
	"If you were not expecting this email, just ignore it, no further action is required to terminate the request.\n";
	
	@Test public void confirmation() {
		User user = new User("frank","cat","frank@fsanmiguel.com");
		sendConfirmationEmail(user);
	}

	@Test public void confirmationNumber() {
        String confirmation = PasswordUtil.encrypt(Long.toString(System.currentTimeMillis()));
        System.out.println(confirmation);
	}
	private void sendConfirmationEmail(User user) {
    	//mailSender.setHost("skipper");
    	SimpleMailMessage message = new SimpleMailMessage();
    	message.setTo(user.getEmail());
    	String messageText = StringUtils.replace(WELCOME_MESSAGE, PARAM_NAME, user.getUsername());
    	message.setText(messageText);
    	message.setSubject(WELCOME_SUBJECT);
    	message.setFrom("<Time Space Map Notifications> notify@timespacemap.com");

    	JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
    	MimeMessage mimeMessage = mailSender.createMimeMessage();
    	InternetAddress from = new InternetAddress();
    	from.setAddress("notify@timespacemap.com");
    	InternetAddress to = new InternetAddress();
    	to.setAddress(user.getEmail());
    	try {
			from.setPersonal("Time Space Map Notifications");
			mimeMessage.addRecipient(Message.RecipientType.TO, to);
			mimeMessage.setSubject(WELCOME_SUBJECT);
			mimeMessage.setText(messageText);
			mimeMessage.setFrom(from);
    	} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        mailSender.setHost("localhost");
    	mailSender.send(mimeMessage);

    	/*
Confirm your registration with Time Space Map

Hello sanmi,

Welcome to the Time Space Map community!

Please click on this link to confirm your registration: http://www.timespacemap.com/member/confirm.htm?id=:confirmation 

You can find out more about us at http://wiki.timespacemap.com/wiki/About.

If you were not expecting this email, just ignore it, no further action is required to terminate the request.
    	 */
    }

}

