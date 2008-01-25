package com.tech4d.tsm.web.signup;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.web.util.WebUtils;

import com.tech4d.tsm.auth.AuthConstants;

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
