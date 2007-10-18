package com.tech4d.tsm.auth;

/**
 * Container for holding username 
 * and auth roles
 * 
 * @author frank
 *
 */
public class UserContext {
    private String roles;
    private String username;
    
    public String getRoles() {
        return roles;
    }
    public void setRoles(String roles) {
        this.roles = roles;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    
}
