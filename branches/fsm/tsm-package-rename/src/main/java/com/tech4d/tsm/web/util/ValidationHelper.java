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
