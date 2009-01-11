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
    private static final String FROM_SUBSQL = " from Notification notification where notification.toUsername = ?";
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

	public void delete(String toUsername, NotificationType type) {
		this.sessionFactory.getCurrentSession().createQuery(
				"delete Notification n where n.toUsername = :toUsername and n.type = :type")
				.setString("toUsername", toUsername)
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
