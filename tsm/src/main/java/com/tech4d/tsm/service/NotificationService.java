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
	public void notifyNewTalk(User toUser, User fromUser) {
		Notification notification = new Notification();
		notification.setFromUser(fromUser);
		notification.setToUser(toUser);
		notification.setTitle(messageSource.getMessage(
				MSGKEY_NEW_TALK_TITLE, new Object[]{fromUser.getUsername(), 
						toUser.getUsername()}, null));
		notification.setType(NotificationType.TALK);
		notificationDao.save(notification);
	}


	/**
	 * Clear all notifications for a given user/notification type
	 * @param user
	 * @param type
	 */
	public void clearNotifications(User user, NotificationType type) {
		notificationDao.delete(user, type);
		
	}

}
