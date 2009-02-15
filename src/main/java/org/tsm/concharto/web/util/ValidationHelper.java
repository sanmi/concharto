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
package org.tsm.concharto.web.util;

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
