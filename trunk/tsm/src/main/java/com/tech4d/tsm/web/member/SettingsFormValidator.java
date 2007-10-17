package com.tech4d.tsm.web.member;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.tech4d.tsm.web.util.AuthFormValidatorHelper;

public class SettingsFormValidator implements Validator {

    public boolean supports(Class clazz) {
        return SettingsForm.class.equals(clazz);
    }

    public void validate(Object target, Errors errors) {
        SettingsForm settingsForm = (SettingsForm) target;
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "existingPassword", "empty.settingsForm.existingPassword");
        AuthFormValidatorHelper.validate(settingsForm, errors);
    }

}
