<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tsm"%>


<tsm:page title="Signup">
	<jsp:attribute name="stylesheet">textpanel.css,header.css</jsp:attribute>
	<jsp:body>
		<form:form id="signupForm" name="signup" commandName="signup"> 
	  	<div class="textpanel">
	  		<h2>Sign Up</h2>
	  		<form:errors path="*" cssClass="errorLabel" element="div"/>
		  	<table>
		  		<tr>
		  			<td>Username</td>
		  			<td>
		  				<form:input path="username"/>
		  			</td>
		  		</tr>
		  		<tr>
		  			<td>Password</td>
		  			<td>
		  				<form:password path="password"/>
		  			</td>
		  		</tr>
		  		<tr>
		  			<td>Password (confirm)</td>
		  			<td>
		  				<form:password path="passwordConfirm"/>
		  			</td>
		  		</tr>
		  		<tr>
		  			<td>Email address</td>
		  			<td>
		  				<form:input path="email"/>
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
			  <input type="submit" value="Signup"/>
		  </div>
	  </form:form>
		
	  
	</jsp:body>
</tsm:page>

