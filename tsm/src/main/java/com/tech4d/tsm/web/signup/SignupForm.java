package com.tech4d.tsm.web.signup;

import com.tech4d.tsm.web.util.AuthForm;

public class SignupForm extends AuthForm {
    private Boolean agreeToTermsOfService;
    private Boolean rememberMe;

    public Boolean getAgreeToTermsOfService() {
        return agreeToTermsOfService;
    }
    public void setAgreeToTermsOfService(Boolean agreeToTermsOfService) {
        this.agreeToTermsOfService = agreeToTermsOfService;
    }
    public Boolean getRememberMe() {
        return rememberMe;
    }
    public void setRememberMe(Boolean rememberMe) {
        this.rememberMe = rememberMe;
    }
    
}
