package com.tech4d.tsm.web.util;

import org.springframework.validation.Errors;

public class ValidationHelper {

	public static void rejectIfTooLong(Errors errors, String field, int maxLength, String errorCode) {
		Object value =  errors.getFieldValue(field);
		if (value != null) {
			rejectIfTooLong(errors, field, maxLength, errorCode, new Object[]{maxLength, value.toString().length()}, null);
		}
	}
	
	public static void rejectIfTooLong(Errors errors, String field, int maxLength, String errorCode, Object[] errorArgs, String defaultMessage) {
		Object value =  errors.getFieldValue(field);
		
        if (value.toString().length() > maxLength) {
            errors.rejectValue(field, 
                    errorCode, 
                    errorArgs,
                    defaultMessage);
        }

	}
}
