package com.tech4d.tsm.web.login;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.web.util.WebUtils;

import com.tech4d.tsm.auth.AuthConstants;
import com.tech4d.tsm.dao.UserDao;
import com.tech4d.tsm.model.User;

/**
 * For authentication.  We aren't using the standard j2ee authentaction mechanisms because there
 * is zero control over the user experience and the user experience sucks.
 * 
 * @author frank
 *
 */
public class LoginController extends SimpleFormController {

    private UserDao userDao;
    
    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }


    @Override
    protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
        LoginForm loginForm = (LoginForm) command;
        
        //DEBUG
        String view;
        if ("cat".equals(loginForm.getPassword())) {
            //save the username in the session 
            WebUtils.setSessionAttribute(request, AuthConstants.AUTH_USERNAME, loginForm.getUsername());
            view = (String) WebUtils.getSessionAttribute(request, AuthConstants.AUTH_TARGET_URI);
            //now erase the target so we don't use it another time
            WebUtils.setSessionAttribute(request, AuthConstants.AUTH_TARGET_URI, null);
            if (view != null) {
                return new ModelAndView(new RedirectView(view));
            } else {
                return new ModelAndView("redirect:/");
            }
        }
        return super.onSubmit(request, response, command, errors);
    }

}
