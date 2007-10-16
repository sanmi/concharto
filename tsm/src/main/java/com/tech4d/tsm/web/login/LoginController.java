package com.tech4d.tsm.web.login;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.propertyeditors.CustomBooleanEditor;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.web.util.WebUtils;

import com.tech4d.tsm.auth.AuthConstants;

/**
 * For authentication.  We aren't using the standard j2ee authentaction mechanisms because there
 * is zero control over the user experience and the user experience sucks.
 * 
 * @author frank
 *
 */
public class LoginController extends SimpleFormController {

    @Override
    protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception {
        binder.registerCustomEditor(Boolean.class, new CustomBooleanEditor("true", "false", true));
        super.initBinder(request, binder);
    }

    @Override
    protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
        LoginForm loginForm = (LoginForm) command;
        //if we got this far, the validator has matched username and password, we just 
        //need to process things.         
        //first save the username in the session            
        WebUtils.setSessionAttribute(request, AuthConstants.AUTH_USERNAME, loginForm.getUsername());
        String view = (String) WebUtils.getSessionAttribute(request, AuthConstants.AUTH_TARGET_URI);
        //now erase the target so we don't use it another time
        WebUtils.setSessionAttribute(request, AuthConstants.AUTH_TARGET_URI, null);
        if (view != null) {
            return new ModelAndView(new RedirectView(view));
        } else {
            return new ModelAndView("redirect:/");
        }
    }
    
    
}
