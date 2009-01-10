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
	public abstract void delete(String toUsername, NotificationType type);

}
