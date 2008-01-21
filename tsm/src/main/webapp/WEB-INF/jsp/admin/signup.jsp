<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tsm"%>
<%@ page import="com.tech4d.tsm.model.user.User" %>
<%
	request.setAttribute("SZ_USERNAME", User.SZ_USERNAME);
	request.setAttribute("SZ_PASSWORD", User.SZ_PASSWORD);
	request.setAttribute("SZ_EMAIL", User.SZ_EMAIL);
 %>

<tsm:page title="Signup">
	<jsp:attribute name="stylesheet">textpanel.css,simpleform.css,header.css</jsp:attribute>
	<jsp:body>
		<form:form id="signupForm" name="signup" commandName="signup"> 
	  	<div class="textpanel">
	  		<h2>Sign Up</h2>
	  		<form:errors path="*" cssClass="errorBox" element="div"/>
				<%--  IE Hack - tables don't support padding in IE6 or 7--%>
				<table><tr><td>
					<div class="infoBox">
				  	<table>
				  		<tr>
				  			<td>Username</td>
				  			<td>
				  				<form:input path="username" maxlength="${SZ_USERNAME}" cssClass="loginText"/>
				  			</td>
				  		</tr>
				  		<tr>
				  			<td>Password</td>
				  			<td>
				  				<form:password path="password" maxlength="${SZ_PASSWORD}" cssClass="loginText"/>
				  			</td>
				  		</tr>
				  		<tr>
				  			<td>Password (confirm)</td>
				  			<td>
				  				<form:password path="passwordConfirm" maxlength="${SZ_PASSWORD}" cssClass="loginText"/>
				  			</td>
				  		</tr>
				  		<tr>
				  			<td>Email address</td>
				  			<td>
				  				<form:input path="email" maxlength="${SZ_EMAIL}" cssClass="loginText"/>
				  			</td>
				  		</tr>
				  		<tr>
				  			<td colspan="2">
				  				<form:checkbox path="agreeToTermsOfService"/>
		  						I agree with the Time Space Map <a href="${basePath}info/legal.htm">Terms of Service</a>
				  			</td>
				  		</tr>
				  		<tr>
				  			<td colspan="2">
				  				<form:checkbox path="rememberMe"/>
		  						Remember me next time
				  			</td>
				  		</tr>
				  	</table>
			  	</div>
		  	</td></tr></table>
			  <input type="submit" value="Signup"/>
		  </div>
	  </form:form>
		
	  
	</jsp:body>
</tsm:page>

