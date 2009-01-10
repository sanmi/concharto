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
