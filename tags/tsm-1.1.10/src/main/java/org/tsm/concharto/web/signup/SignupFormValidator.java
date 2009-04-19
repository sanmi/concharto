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

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.tsm.concharto.dao.UserDao;
import org.tsm.concharto.web.util.AuthFormValidatorHelper;


public class SignupFormValidator implements Validator {
    private UserDao userDao;
    
    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    @SuppressWarnings("unchecked")
	public boolean supports(Class clazz) {
        return SignupForm.class.equals(clazz);
    }

    public void validate(Object target, Errors errors) {
        SignupForm signupForm = (SignupForm) target;
        signupForm.setFromController(SignupForm.FORM_SIGNUP);
        AuthFormValidatorHelper.validate(signupForm, errors);
        
        if (!signupForm.getAgreeToTermsOfService()) {
            errors.rejectValue("agreeToTermsOfService", "mustAgree.signupForm.agreeToTermsOfService");
        }
        
        //check to see that the username isn't already taken
        if (null != userDao.find(signupForm.getUsername())) {
            errors.rejectValue("username", "usernameTaken.signupForm.username");
        }
    }
}
