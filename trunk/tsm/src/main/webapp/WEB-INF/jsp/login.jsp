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
	  		<h2>You need to login or sign up as a memeber in order to use this feature</h2>
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
		  	</table>
			  <input type="submit" value="Login"/>
		  </div>
	  </form:form>
	  
	</jsp:body>
</tsm:page>

