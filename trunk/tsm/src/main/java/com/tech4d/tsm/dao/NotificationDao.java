package com.tech4d.tsm.dao;

import java.io.Serializable;
import java.util.List;

import org.hibernate.SessionFactory;

import com.tech4d.tsm.model.user.Notification;
import com.tech4d.tsm.model.user.User;
import com.tech4d.tsm.model.user.Notification.NotificationType;

public interface NotificationDao {

	public abstract void setSessionFactory(SessionFactory sessionFactory);

	public abstract Notification get(Long id);

	public abstract List<Notification> find(User user);
	public abstract List<Notification> find(String username);

	public abstract Boolean notificationsExist(String username);

	public abstract Serializable save(Notification notification);

	public abstract void saveOrUpdate(Notification notification);

	public abstract void delete(Long id);

	/**
	 * Delete all notifications for a given user, type
	 * @param me
	 * @param type
	 */
	public abstract void delete(User user, NotificationType type);

}
