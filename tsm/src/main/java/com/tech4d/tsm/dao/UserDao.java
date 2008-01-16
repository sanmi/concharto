package com.tech4d.tsm.dao;

import java.io.Serializable;
import java.util.List;

import com.tech4d.tsm.model.user.Role;
import com.tech4d.tsm.model.user.User;

public interface UserDao {
    public Serializable save(Object obj);
    public User find(Long id);
    public User find(String username);
    public List<Role> getRolesForUser(Long id);
    public void delete(User user);
    public void delete(Long id);
    public List<Role> getRoles();
    public Role getRole(String role);
	public User getUserFromPasswordRetrievalKey(String retrievalKey);
	public User getUserFromRememberMeKey(String value);
}
