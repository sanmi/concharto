package com.tech4d.tsm.dao;

import java.io.Serializable;
import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.transaction.annotation.Transactional;

import com.tech4d.tsm.model.user.Notification;
import com.tech4d.tsm.model.user.User;
import com.tech4d.tsm.model.user.Notification.NotificationType;

@Transactional
public class NotificationDaoHib implements NotificationDao {
    private static final String FROM_SUBSQL = " from Notification notification where notification.toUser.username = ?";
	private SessionFactory sessionFactory;

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

	public Notification get(Long id) {
		return (Notification) this.sessionFactory.getCurrentSession().get(Notification.class, id);
	}

	@SuppressWarnings("unchecked")
	public Boolean notificationsExist(String username) {
		List ids = this.sessionFactory.getCurrentSession().createQuery(
				"select notification.id " + FROM_SUBSQL)
                .setParameter(0, username)
                .list();
		if (ids.size() > 0) {
			return true;
		} else {
			return false;
		}
	}

	public Serializable save(Notification notification) {
		return this.sessionFactory.getCurrentSession().save(notification);
	}
	
	public void saveOrUpdate(Notification notification) {
		this.sessionFactory.getCurrentSession().saveOrUpdate(notification);
	}

	public void delete(Long id) {
		Notification notification = new Notification();
		notification.setId(id);
		this.sessionFactory.getCurrentSession().delete(notification);
	}

	public void delete(User user, NotificationType type) {
		this.sessionFactory.getCurrentSession().createQuery(
				"delete Notification n where n.toUser = :toUser and n.type = :type")
				.setEntity("toUser", user)
				.setString("type", type.toString())
				.executeUpdate();
	}

	public List<Notification> find(User user) {
		return find(user.getUsername());
	}

	@SuppressWarnings("unchecked")
	public List<Notification> find(String username) {
		return this.sessionFactory.getCurrentSession().createQuery(
		"select notification " + FROM_SUBSQL)
        .setParameter(0, username)
        .list();
	}

}
