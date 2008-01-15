package com.tech4d.tsm.web.forgot;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;

import com.tech4d.tsm.auth.AuthHelper;
import com.tech4d.tsm.dao.UserDao;
import com.tech4d.tsm.model.user.User;
import com.tech4d.tsm.util.PasswordUtil;
import com.tech4d.tsm.web.util.AuthForm;
import com.tech4d.tsm.web.util.AuthFormValidatorHelper;

public class ResetController extends SimpleFormController{
	private static final String PARAM_KEY = "key";
    private static final Log log = LogFactory.getLog(ResetController.class);
	private UserDao userDao;

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

	
	@Override
	protected ModelAndView showForm(HttpServletRequest request,
			HttpServletResponse response, BindException errors)
			throws Exception {
		//get the user for whom this password applies
		User user = getUser(request);
		if (user == null) {
			//tell the user there was a problem 
            errors.rejectValue("username", "invalidKey.resetForm.username");
            return new ModelAndView(getFormView(), errors.getModel());
		}
		return super.showForm(request, response, errors);
	}


	private User getUser(HttpServletRequest request)
			throws ServletRequestBindingException {
		String key = ServletRequestUtils.getStringParameter(request, PARAM_KEY);
		User user = userDao.getUserFromPasswordRetrievalKey(key);
		return user;
	}


	@Override
	protected ModelAndView onSubmit(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors)
			throws Exception {

		User user = getUser(request);
		if (user == null) {
			//tell the user there was a problem 
            errors.rejectValue("username", "invalidKey.resetForm.username");
            return new ModelAndView(getFormView(), errors.getModel());
		}
		//validate the password fields
		AuthForm authForm = (AuthForm) command;
		AuthFormValidatorHelper.validatePasswordFields(authForm, errors);
		if (errors.hasErrors()) {
            return new ModelAndView(getFormView(), errors.getModel());
		}
		
		//ok, now we can reset the password
		user.setPassword(PasswordUtil.encrypt(authForm.getPassword()));

		//and remove the key so the email can't be used again
		user.getUserNote().setPasswordRetrievalKey(null);
		userDao.save(user);
		log.info("user " + user.getUsername() + " has reset their password");
		
		//now log in
        AuthHelper.setUserInSession(request, user);
		
		return super.onSubmit(request, response, command, errors);
	}
	
	

}
