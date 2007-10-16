<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tsm"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<tsm:page title="Login">
	<jsp:body>
	  <form:form id="loginForm" name="login" commandName="login"> 
	  	<div class="loginForm">
				<h2>Welcome to the Time Space Map Pilot <br/>Please Login</h2>
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
		  			<td colspan="2">
		  				<form:checkbox path="rememberMe"/>
  						Remember me next time
		  			</td>
		  		</tr>
		  	</table>
			  <input type="submit" value="Login"/>
		  </div>
	  </form:form>
	  
	  
	</jsp:body>
</tsm:page>

