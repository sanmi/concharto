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
package com.tech4d.tsm.web.member;

import org.apache.commons.lang.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.tech4d.tsm.web.util.AuthFormValidatorHelper;

public class SettingsFormValidator implements Validator {

    @SuppressWarnings("unchecked")
	public boolean supports(Class clazz) {
        return SettingsForm.class.equals(clazz);
    }

    public void validate(Object target, Errors errors) {
        SettingsForm settingsForm = (SettingsForm) target;
        //the user can choose to fill out none or all of the password fields
        if (!allPasswordFieldsAreEmpty(settingsForm)) {
	        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "existingPassword", "empty.settingsForm.existingPassword");
	        AuthFormValidatorHelper.validateUsernamePasswordFields(settingsForm, errors);
        }
    }
    
    public static boolean allPasswordFieldsAreEmpty(SettingsForm settingsForm) {
    	return StringUtils.isEmpty(settingsForm.getPassword()) &&
			StringUtils.isEmpty(settingsForm.getPasswordConfirm()) &&
			StringUtils.isEmpty(settingsForm.getExistingPassword()); 
	
    }

}
