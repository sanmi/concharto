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
package org.tsm.concharto.dao;

import java.io.Serializable;
import java.util.List;

import org.hibernate.SessionFactory;
import org.tsm.concharto.model.user.Notification;
import org.tsm.concharto.model.user.User;
import org.tsm.concharto.model.user.Notification.NotificationType;


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
