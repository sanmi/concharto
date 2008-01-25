package com.tech4d.tsm.web.login;

import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.propertyeditors.CustomBooleanEditor;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;
import org.springframework.web.util.WebUtils;

import com.tech4d.tsm.auth.AuthHelper;
import com.tech4d.tsm.dao.UserDao;
import com.tech4d.tsm.model.user.User;
import com.tech4d.tsm.model.user.UserNote;
import com.tech4d.tsm.util.PasswordUtil;
import com.tech4d.tsm.web.signup.LoginSignupHelper;
import com.tech4d.tsm.web.signup.SignupForm;

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
    
	@SuppressWarnings("unchecked")
	@Override
	protected Map referenceData(HttpServletRequest request, Object command,
			Errors errors) throws Exception {

		Cookie userCookie = WebUtils.getCookie(request, AuthHelper.COOKIE_REMEMBER_ME_USERNAME);
		if ((userCookie != null) && !StringUtils.isEmpty(userCookie.getValue())) {
			SignupForm signupForm = (SignupForm) command;
			signupForm.setUsername(userCookie.getValue());
			signupForm.setRememberMe(true);
		}
		return super.referenceData(request, command, errors);
	}

	@Override
    protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
        SignupForm signupForm = (SignupForm) command;
        User user = userDao.find(signupForm.getUsername());
        
        if ((user != null) && 
            (PasswordUtil.isPasswordValid(signupForm.getPassword(), user.getPassword()))) {
            //matched username and password, ok to proceed
            log.info("user " + signupForm.getUsername() + " signed in");

            //first save the username and roles in the session            
            AuthHelper.setUserInSession(request, user);
            
            //if they checked "remember me" we set a cookie
            if (BooleanUtils.isTrue(signupForm.getRememberMe())) {
                setRemeberMeCookie(response, user, AuthHelper.COOKIE_REMEMBER_ME_MAX_AGE);
            } else {
                setRemeberMeCookie(response, user, 0);
            }
            
            //now go where we were originally heading
            return LoginSignupHelper.continueToRequestedUrl(request);
        } else {
        	/* NOTE we are doing validation here instead of the validator so that we don't have to 
        	 * go to the database twice to get the user object.
        	 */
            //tell the user there was a problem and let the default form handle the rest
            errors.rejectValue("username", "invalidUserPasswd.authForm.username");
            return new ModelAndView(getFormView(), errors.getModel());
        }
    }

	/**
	 * Remember me cookie allows the user to come back to the site without having to log in again.
	 * It works for a predetermined amount of time (e.g. 20 days)
	 * @param response servlet response
	 * @param user user
	 * @param maxAge max cookie age
	 */
	private void setRemeberMeCookie(HttpServletResponse response, User user, int maxAge) {
		if ((null == user.getUserNote()) || (null == user.getUserNote().getRememberMeKey())) {
			String rememberMeKey = PasswordUtil.encrypt(user.getUsername() + Long.toString(System.currentTimeMillis()));
			UserNote userNote = new UserNote();
			userNote.setRememberMeKey(rememberMeKey);
			user.setUserNote(userNote);
			userDao.save(user);
		}
		AuthHelper.setCookie(response, AuthHelper.COOKIE_REMEMBER_ME, maxAge, user.getUserNote().getRememberMeKey());
		AuthHelper.setCookie(response, AuthHelper.COOKIE_REMEMBER_ME_USERNAME, maxAge, user.getUsername());
	}

}
