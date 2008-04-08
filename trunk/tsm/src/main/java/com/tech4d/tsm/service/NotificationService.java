package com.tech4d.tsm.service;

import org.springframework.context.support.ResourceBundleMessageSource;

import com.tech4d.tsm.dao.NotificationDao;
import com.tech4d.tsm.model.user.Notification;
import com.tech4d.tsm.model.user.User;
import com.tech4d.tsm.model.user.Notification.NotificationType;

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
