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
package org.tsm.concharto.service;

import java.util.Properties;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.event.ConnectionEvent;
import javax.mail.event.ConnectionListener;
import javax.mail.event.TransportEvent;
import javax.mail.event.TransportListener;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSenderImpl;

/**
 * Email sending service
 */
public class EmailService implements ConnectionListener, TransportListener, InitializingBean {
    private Log log = LogFactory.getLog(EmailService.class);
    private String smtpHost;
    private JavaMailSenderImpl mailSender;
    
    public EmailService() {
		super();
		mailSender = new JavaMailSenderImpl();
	}

	public void setSmtpHost(String smtpHost) {
        this.smtpHost = smtpHost;
    }

    public void messagePartiallyDelivered(TransportEvent e) {
    }
    
    public MimeMessage createMimeMessage() {
    	return mailSender.createMimeMessage();
    }

    /**
     *
     * @param message
     * @throws MessagingException e
     * @throws MailException e
     */
    public void sendMessage(MimeMessage message) {
        try {
			if (message.getAllRecipients() != null) {
			    mailSender.setHost(smtpHost);
				mailSender.send(message);
			} else {
			    log.warn("Email not sent, no recipients specified");
			}
		} catch (MailException e) {
			log.error("send mail exception ", e);
		} catch (MessagingException e) {
			log.error("messagign exception", e);
		}
    }

    /**
     *
     * @param subject
     * @param body
     * @param sender
     * @param recipients
     */
    public void SendIndividualMessages(String subject, String body, InternetAddress sender, String[] recipients) { 
        int emailErrorCount = 0;
        for (int i = 0; i < recipients.length; i++) {
            String recipientEmailAddress = recipients[i];
            MimeMessage message = new MimeMessage(Session.getDefaultInstance(new Properties()));
            try {
                message.setText(body);
                message.setSubject(subject);
                message.setFrom(sender);
                message.addRecipient(Message.RecipientType.TO, new InternetAddress(recipientEmailAddress));                
                sendMessage(message);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                emailErrorCount++;
            }
        }
        if (emailErrorCount > 0) {
            String message = emailErrorCount + " out of " + recipients.length + " emails could not be sent.";
            log.error(message);
        }
    }

	public void afterPropertiesSet() throws Exception {
		// TODO Auto-generated method stub
		
	}
    public void opened(ConnectionEvent e) {
        if (log.isDebugEnabled()) {
            log.debug("ConnectionListener.opened()");
        }
    }

    public void disconnected(ConnectionEvent e) {
    }

    public void closed(ConnectionEvent e) {
        if (log.isDebugEnabled()) {
            log.debug("ConnectionListener.closed()");
        }
    }

    public void messageDelivered(TransportEvent e) {
        Address[] validSentAddresses = e.getValidSentAddresses();
        String addressList = "TransportListener.messageDelivered. Valid Addresses: " + StringUtils.join(validSentAddresses, ',');
        log.info(addressList);
    }

    /**
     * @see TransportListener#messageNotDelivered(javax.mail.event.TransportEvent)
     */
    public void messageNotDelivered(TransportEvent e) {
        Address[] invalidSentAddresses = e.getInvalidAddresses();
        String addressList = "TransportListener.messageNotDelivered, Invalid Addresses: " + StringUtils.join(invalidSentAddresses, ',');
        log.warn(addressList);
    }

}
