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
package org.tsm.concharto.dao;

import java.io.Serializable;
import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.transaction.annotation.Transactional;
import org.tsm.concharto.model.user.Role;
import org.tsm.concharto.model.user.User;
import org.tsm.concharto.model.user.UserNote;


@Transactional
public class UserDaoHib implements UserDao {
    private SessionFactory sessionFactory;

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public Serializable save(Object obj) {
        return this.sessionFactory.getCurrentSession().save(obj);
    }

    public void delete(User user) {
        this.sessionFactory.getCurrentSession().delete(user);
    }

    public void delete(Long id) {
       User user = new User();
       user.setId(id);
       delete(user);
    }

    public List<Role> getRolesForUser(Long id) {
        // TODO Auto-generated method stub
        return null;
    }

    public User find(Long id) {
        return (User) this.sessionFactory.getCurrentSession().get(
                User.class, id);
    }

    @SuppressWarnings("unchecked")
    public User find(String username) {
        return (User) DaoHelper.getOnlyFirst(
        		this.sessionFactory.getCurrentSession().createQuery(
                "select user from User user where user.username = ?").setParameter(0, username).list()
        );
    }

    @SuppressWarnings("unchecked")
    public Role getRole(String role) {
        List<Role> roles = this.sessionFactory.getCurrentSession().createQuery(
        "select role from Role role where role.name = ?").setParameter(0, role).list();
        return (Role) DaoHelper.getOnlyFirst(roles);
    }

    @SuppressWarnings("unchecked")
    public List<Role> getRoles() {
        return this.sessionFactory.getCurrentSession().createQuery(
        "select role from Role role").list();
    }

	@SuppressWarnings("unchecked")
	public User getUserFromPasswordRetrievalKey(String key) {
		List<UserNote> userNotes = this.sessionFactory.getCurrentSession().createQuery(
		"select user from User user where user.userNote.passwordRetrievalKey = ?")
		.setParameter(0, key)
		.list();
		return (User) DaoHelper.getOnlyFirst(userNotes);
	}

	@SuppressWarnings("unchecked")
	public User getUserFromRememberMeKey(String key) {
		List<UserNote> userNotes = this.sessionFactory.getCurrentSession().createQuery(
		"select user from User user where user.userNote.rememberMeKey = ?")
		.setParameter(0, key)
		.list();
		User user = (User) DaoHelper.getOnlyFirst(userNotes);
		//load everything in (kludge for the LoginFilter)
		if (null != user) {
			for (Role role : user.getRoles()) {
				role.getName();
			}
			user.getUserNote();
		}
		return user;
	}
    
}
