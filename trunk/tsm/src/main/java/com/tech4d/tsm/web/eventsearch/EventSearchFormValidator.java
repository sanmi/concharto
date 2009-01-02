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
package com.tech4d.tsm.web.eventsearch;

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
