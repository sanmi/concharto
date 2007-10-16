package com.tech4d.tsm.web.signup;

public class SignupForm {
    private String username;
    private String password;
    private String passwordConfirm;
    private String email;
    private Boolean agreeToTermsOfService;
    private Boolean rememberMe;

    public Boolean getAgreeToTermsOfService() {
        return agreeToTermsOfService;
    }
    public void setAgreeToTermsOfService(Boolean agreeToTermsOfService) {
        this.agreeToTermsOfService = agreeToTermsOfService;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getPasswordConfirm() {
        return passwordConfirm;
    }
    public void setPasswordConfirm(String passwordConfirm) {
        this.passwordConfirm = passwordConfirm;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public Boolean getRememberMe() {
        return rememberMe;
    }
    public void setRememberMe(Boolean rememberMe) {
        this.rememberMe = rememberMe;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    
}
