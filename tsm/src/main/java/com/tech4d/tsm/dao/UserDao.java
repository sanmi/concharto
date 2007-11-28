package com.tech4d.tsm.dao;

import java.io.Serializable;
import java.util.List;

import com.tech4d.tsm.model.Role;
import com.tech4d.tsm.model.User;

public interface UserDao {
    public Serializable save(User user);
    public User find(Long id);
    public User find(String username);
    public List<Role> getRolesForUser(Long id);
    public void delete(User user);
    public void delete(Long id);
    public List<Role> getRoles();
    public Role getRole(String role);
}