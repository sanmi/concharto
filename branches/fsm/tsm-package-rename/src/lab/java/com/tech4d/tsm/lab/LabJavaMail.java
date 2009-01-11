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

	private static final String WELCOME_SUBJECT = "Confirm your registration with Concharto";
	private static String PARAM_NAME = ":name";
	private static String PARAM_CONFIRMATION = ":confirmation";
	private static String  WELCOME_MESSAGE = "Hello " + PARAM_NAME + ",\n\n" + 
	"Welcome to the Concharto community! \n\n" +
	"Please click on this link to confirm your registration: \n" +
	"http://www.concharto.com/member/confirm.htm?id=" + PARAM_CONFIRMATION + " \n\n" + 
	"You can find out more about us at http://wiki.concharto.com/wiki/About.\n\n" +
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
    	message.setFrom("<Concharto Notifications> notify@concharto.com");

    	JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
    	MimeMessage mimeMessage = mailSender.createMimeMessage();
    	InternetAddress from = new InternetAddress();
    	from.setAddress("notify@concharto.com");
    	InternetAddress to = new InternetAddress();
    	to.setAddress(user.getEmail());
    	try {
			from.setPersonal("Concharto Notifications");
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
Confirm your registration with Concharto

Hello sanmi,

Welcome to the Concharto community!

Please click on this link to confirm your registration: http://www.concharto.com/member/confirm.htm?id=:confirmation 

You can find out more about us at http://wiki.concharto.com/wiki/About.

If you were not expecting this email, just ignore it, no further action is required to terminate the request.
    	 */
    }

}
