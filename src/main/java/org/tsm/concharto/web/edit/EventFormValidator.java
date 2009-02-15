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
package org.tsm.concharto.web.edit;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import org.tsm.concharto.web.util.ValidationHelper;



public class EventFormValidator implements Validator {

    @SuppressWarnings("unchecked")
	public boolean supports(Class clazz) {
        return EventForm.class.equals(clazz);
    }

    public void validate(Object target, Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "summary", "empty.event.summary");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "when", "empty.event.when");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "geometry", "empty.event.geometry");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "source", "empty.event.source");
        
        ValidationHelper.rejectIfTooLong(errors, "description", EventForm.SZ_DESCRIPTION, "tooLong");

        //These should never trigger if the HTML is coded correctly
        ValidationHelper.rejectIfTooLong(errors, "summary", EventForm.SZ_SUMMARY, "tooLong");
        ValidationHelper.rejectIfTooLong(errors, "tags", EventForm.SZ_TAGS, "tooLong");
        ValidationHelper.rejectIfTooLong(errors, "where", EventForm.SZ_WHERE, "tooLong");
        ValidationHelper.rejectIfTooLong(errors, "source", EventForm.SZ_SOURCE, "tooLong");
        
        EventForm eventForm = (EventForm) target;
        if ((null != eventForm.getGeometry()) && (0 == eventForm.getGeometry().getNumPoints())) {
        	errors.rejectValue("geometry", "empty.event.geometry");
        }
    }
    
}
