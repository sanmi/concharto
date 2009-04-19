/*******************************************************************************
 * Copyright 2009 Time Space Map, LLC
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package org.tsm.concharto.web.signup;

import org.tsm.concharto.web.util.AuthForm;

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
