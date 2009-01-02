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
package com.tech4d.tsm.web.feedback;

import java.io.UnsupportedEncodingException;
import java.util.Enumeration;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;

import com.tech4d.tsm.service.EmailService;

/**
 * Collect user feedback and send to a fixed email drop box
 *
 */
public class FeedbackController extends SimpleFormController {
	private static final Log log = LogFactory.getLog(FeedbackController.class);
	private EmailService emailService;
	private String sendFeedbackToAddress;

	public void setEmailService(EmailService emailService) {
		this.emailService = emailService;
	}
	public void setSendFeedbackToAddress(String sendFeedbackToAddress) {
		this.sendFeedbackToAddress = sendFeedbackToAddress;
	}

	@Override
	protected ModelAndView onSubmit(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors)
			throws Exception {
		FeedbackForm feedbackForm = (FeedbackForm) command;
			
		//send the feedback email 
		MimeMessage message = emailService.createMimeMessage();
		makeFeedbackMessage(message, feedbackForm, request);
		emailService.sendMessage(message);

		return super.onSubmit(request, response, command, errors);
	}

	@SuppressWarnings("unchecked")
	private String getBrowserInfo(HttpServletRequest request) {
		StringBuffer userRequestInfo = new StringBuffer()
			.append(request.getRemoteAddr()).append("\n") 
			.append(request.getRemoteHost()).append("\n");
		
		Enumeration headerNames = request.getHeaderNames();
		while (headerNames.hasMoreElements()) {
			String headerName = (String) headerNames.nextElement();
			//don't email cookies for security reasons
			if (!"cookie".equals(headerName)) {
				userRequestInfo.append(headerName).append(": ")
				.append(request.getHeader(headerName)).append("\n");
			}
			
		}
		return userRequestInfo.toString();
	}

	private static final String FEEDBACK_SUBJECT = "Feedback: ";
	
	public MimeMessage makeFeedbackMessage(
			MimeMessage message, FeedbackForm feedbackForm, HttpServletRequest request) {
		
		//prepare the user info
		String requestInfo = getBrowserInfo(request);

		StringBuffer messageText = new StringBuffer(feedbackForm.getBody())
			.append("\n\n=============================================================\n")
			.append(requestInfo);
		
    	InternetAddress from = new InternetAddress();
    	from.setAddress(feedbackForm.getEmail());
    	InternetAddress to = new InternetAddress();
    	to.setAddress(sendFeedbackToAddress);
    	try {
			from.setPersonal(feedbackForm.getName());
			message.addRecipient(Message.RecipientType.TO, to);
			message.setSubject(FEEDBACK_SUBJECT + feedbackForm.getSubject());
			message.setText(messageText.toString());
			message.setFrom(from);
    	} catch (UnsupportedEncodingException e) {
			log.error(e);
		} catch (MessagingException e) {
			log.error(e);
		}
		return message;
	}
	

}
