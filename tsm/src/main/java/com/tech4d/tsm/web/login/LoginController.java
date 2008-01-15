package com.tech4d.tsm.web.login;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.propertyeditors.CustomBooleanEditor;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.web.util.WebUtils;

import com.tech4d.tsm.auth.AuthConstants;
import com.tech4d.tsm.auth.AuthHelper;
import com.tech4d.tsm.dao.UserDao;
import com.tech4d.tsm.model.User;
import com.tech4d.tsm.util.PasswordUtil;

/**
 * For authentication.  We aren't using the standard j2ee authentaction mechanisms because there
 * is zero control over the user experience and the user experience sucks.
 * 
 * @author frank
 *
 */
public class LoginController extends SimpleFormController {
    private static final Log log = LogFactory.getLog(LoginController.class);
    private UserDao userDao;
    
    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception {
        binder.registerCustomEditor(Boolean.class, new CustomBooleanEditor("true", "false", true));
        super.initBinder(request, binder);
    }

    @Override
    protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
        LoginForm loginForm = (LoginForm) command;
        User user = userDao.find(loginForm.getUsername());
        
        if ((user != null) && 
            (PasswordUtil.isPasswordValid(loginForm.getPassword(), user.getPassword()))) {
            //matched username and password, ok to proceed
            log.info("user " + loginForm.getUsername() + " signed in");

            //first save the username and roles in the session            
            AuthHelper.setUserInSession(request, user);
            
            //now go where we were originally heading
            String view = (String) WebUtils.getSessionAttribute(request, AuthConstants.SESSION_AUTH_TARGET_URI);
            //now erase the target so we don't use it another time
            WebUtils.setSessionAttribute(request, AuthConstants.SESSION_AUTH_TARGET_URI, null);
            if (view != null) {
                return new ModelAndView(new RedirectView(view));
            } else {
                return new ModelAndView("redirect:/");
            }
        } else {
        	/* NOTE we are doing validation here instead of the validator so that we don't have to 
        	 * go to the database twice to get the user object.
        	 */
            //tell the user there was a problem and let the default form handle the rest
            errors.rejectValue("username", "invalidUserPasswd.authForm.username");
            return new ModelAndView(getFormView(), errors.getModel());
        }
    }

}
