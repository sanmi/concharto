package com.tech4d.tsm.web.signup;

import com.tech4d.tsm.web.util.AuthForm;

public class SignupForm extends AuthForm {
    public static final String FORM_SIGNUP = "signup";
    public static final String FORM_LOGIN = "login";
    private Boolean agreeToTermsOfService;
    private Boolean rememberMe;
    private String fromController;

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
    
    /**
     * To support the multi-form login/signup page
     * @return
     */
	public String getFromController() {
		return fromController;
	}
	public void setFromController(String fromController) {
		this.fromController = fromController;
	}
}
