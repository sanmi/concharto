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
package org.tsm.concharto.service;

import org.springframework.context.support.ResourceBundleMessageSource;
import org.tsm.concharto.dao.NotificationDao;
import org.tsm.concharto.model.user.Notification;
import org.tsm.concharto.model.user.User;
import org.tsm.concharto.model.user.Notification.NotificationType;


/**
 * Service for submitting and clearing notifications
 * 
 */
public class NotificationService {
	private static final String MSGKEY_NEW_TALK_TITLE = "notify.talk.title";
	private ResourceBundleMessageSource messageSource;
	NotificationDao notificationDao;
	
	public void setNotificationDao(NotificationDao notificationDao) {
		this.notificationDao = notificationDao;
	}

	public void setMessageSource(ResourceBundleMessageSource messageSource) {
		this.messageSource = messageSource;
	}

	/**
	 * Notify a user that someone wrote on his/her talk page
	 * @param toUser
	 * @param fromUser
	 */
	public void notifyNewTalk(String toUsername, String fromUsername) {
		Notification notification = new Notification();
		notification.setFromUsername(fromUsername);
		notification.setToUsername(toUsername);
		notification.setTitle(messageSource.getMessage(
				MSGKEY_NEW_TALK_TITLE, new Object[]{fromUsername, 
						toUsername}, null));
		notification.setType(NotificationType.TALK);
		notificationDao.save(notification);
	}


	/**
	 * Clear all notifications for a given user/notification type
	 * @param user
	 * @param type
	 */
	public void clearNotifications(String toUsername, NotificationType type) {
		notificationDao.delete(toUsername, type);
		
	}

}
