package com.tech4d.tsm.dao;

import java.util.List;

import com.tech4d.tsm.model.Role;
import com.tech4d.tsm.model.User;

public interface UserDao {

    public User getUser(Long id);
    public List<Role> getRolesForUser(Long id);
}
