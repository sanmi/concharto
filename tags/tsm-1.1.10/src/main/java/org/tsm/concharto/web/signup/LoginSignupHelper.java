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
package org.tsm.concharto.web.signup;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.web.util.WebUtils;
import org.tsm.concharto.auth.AuthConstants;


public class LoginSignupHelper {

	/**
	 * Go where we were originally heading.  The redirect view must already be in the 
	 * session at AuthConstants.SESSION_AUTH_TARGET_URI
	 * 
	 * @param request
	 * @return where we were originally heading
	 */
	public static ModelAndView continueToRequestedUrl(HttpServletRequest request) {
		String view = (String) WebUtils.getSessionAttribute(request, AuthConstants.SESSION_AUTH_TARGET_URI);        
        //now erase the target so we don't use it another time
        WebUtils.setSessionAttribute(request, AuthConstants.SESSION_AUTH_TARGET_URI, null);
        if (view != null) {
            return new ModelAndView(new RedirectView(view));
        } else {
            return new ModelAndView("redirect:/");
        }
	}

}
