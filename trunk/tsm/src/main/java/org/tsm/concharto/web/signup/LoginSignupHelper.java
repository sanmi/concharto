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
