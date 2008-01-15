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
