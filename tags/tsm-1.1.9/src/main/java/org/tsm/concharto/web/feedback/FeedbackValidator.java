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
package org.tsm.concharto.web.feedback;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

public class FeedbackValidator implements Validator {

	@SuppressWarnings("unchecked")
	public boolean supports(Class clazz) {
		return clazz == FeedbackForm.class;
	}

	public void validate(Object target, Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "empty.feedback.name");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email", "empty.feedback.email");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "subject", "empty.feedback.subject");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "body", "empty.feedback.body");
	}
}
