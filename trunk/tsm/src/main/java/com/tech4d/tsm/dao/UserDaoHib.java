package com.tech4d.tsm.dao;

import java.io.Serializable;
import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.transaction.annotation.Transactional;

import com.tech4d.tsm.model.user.Role;
import com.tech4d.tsm.model.user.User;
import com.tech4d.tsm.model.user.UserNote;

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
        List<User> users = this.sessionFactory.getCurrentSession().createQuery(
        "select user from User user where user.username = ?").setParameter(0, username).list();
        if (users.size() == 1) {
            return users.get(0);
        } else {
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    public Role getRole(String role) {
        List<Role> roles = this.sessionFactory.getCurrentSession().createQuery(
        "select role from Role role where role.name = ?").setParameter(0, role).list();
        return (Role) getOnlyFirst(roles);
    }

	private Object getOnlyFirst(List<?> roles) {
		if (roles.size() == 1) {
            return roles.get(0);
        } else {
            return null;
        }
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
		return (User) getOnlyFirst(userNotes);
	}

	@SuppressWarnings("unchecked")
	public User getUserFromRememberMeKey(String key) {
		List<UserNote> userNotes = this.sessionFactory.getCurrentSession().createQuery(
		"select user from User user where user.userNote.rememberMeKey = ?")
		.setParameter(0, key)
		.list();
		User user = (User) getOnlyFirst(userNotes);
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
