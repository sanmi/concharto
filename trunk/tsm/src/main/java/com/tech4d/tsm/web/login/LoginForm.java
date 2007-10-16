package com.tech4d.tsm.web.login;

public class LoginForm {
    private String username;
    private String password;
    private Boolean rememberMe;

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public Boolean getRememberMe() {
        return rememberMe;
    }
    public void setRememberMe(Boolean rememberMe) {
        this.rememberMe = rememberMe;
    }
    
}
