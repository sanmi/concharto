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
package org.tsm.concharto.web.eventsearch;

import org.apache.commons.lang.BooleanUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;


public class EventSearchFormValidator implements Validator{

    @SuppressWarnings("unchecked")
	public boolean supports(Class clazz) {
        return EventSearchForm.class.equals(clazz);
    }

    public void validate(Object target, Errors errors) {
        EventSearchForm eventSearchForm = (EventSearchForm) target;
        //if the geocode failed        
        if (BooleanUtils.isFalse(eventSearchForm.getIsGeocodeSuccess())) {
            errors.rejectValue("where", 
                    "failedGeocode.eventSearch.where", 
                    new Object[]{eventSearchForm.getWhere()}, null);
        }
        //begin can't be after end 
        if (eventSearchForm.getWhen() != null) {
            if (eventSearchForm.getWhen().getBegin().getDate().after(
            		eventSearchForm.getWhen().getEnd().getDate())) {
                errors.rejectValue("when", 
                        "beginAfterEnd.when", 
                        new Object[]{eventSearchForm.getWhen()}, null);
            }
        }
    }

}
