<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tsm"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<tsm:page title="Settings">
	<jsp:body>
	  <form:form id="settingsForm" name="settings" commandName="settings"> 
	  	<form:hidden path="username"/>
	  	<div class="settingsForm">
				<h2>Your Account Settings</h2>
				<form:errors path="*" cssClass="errorLabel" element="div"/>
		  	<table>
		  		<tr>
		  			<td>Username</td>
		  			<td>
		  				${user.username}
		  			</td>
		  		</tr>
		  		<tr>
		  			<td>Email</td>
		  			<td>
		  				${user.email}
		  			</td>
		  		</tr>
		  		<tr>
		  			<td>Roles</td>
		  			<td>
		  				<c:forEach items="${user.roles}" var="role" varStatus="status">
			  				${role.name}<c:if test="${status.count < (fn:length(user.roles))}">, </c:if>
		  				</c:forEach>
		  			</td>
		  		</tr>
		  		<tr>
		  			<td><h2>Change Settings</h2></td>
		  		</tr>
		  		<c:if test="${success}">
			  		<tr >
			  			<td class="successMessage" colspan="2">Your changes were successful</td>
			  		</tr>
		  		</c:if>
		  		<tr>
		  			<td>
		  				<form:errors path="existingPassword"><span class="errorLabel"></form:errors>
		  				Verify Existing Password
		  				<form:errors path="existingPassword"></span></form:errors>
		  			</td>
		  			<td>
		  				<form:password path="existingPassword"/>
		  			</td>
		  		</tr>
		  		<tr>
		  			<td>
		  				<form:errors path="password"><span class="errorLabel"></form:errors>
		  				New Password
		  				<form:errors path="password"></span></form:errors>
		  			</td>
		  			<td>
		  				<form:password path="password"/>
		  			</td>
		  		</tr>
		  		<tr>
		  			<td>
		  				<form:errors path="passwordConfirm"><span class="errorLabel"></form:errors>
		  				Verify New Password
		  				<form:errors path="passwordConfirm"></span></form:errors>
		  			</td>
		  			<td>
		  				<form:password path="passwordConfirm"/>
		  			</td>
		  		</tr>
		  		<tr>
		  			<td>
		  				<form:errors path="email"><span class="errorLabel"></form:errors>
		  				Email
		  				<form:errors path="email"></span></form:errors>
		  			</td>
		  			<td>
		  				<form:input path="email"/>
		  			</td>
		  		</tr>
		  	</table>
			  <input type="submit" value="Save Changes"/>
			  <input type="button" value="Done" onclick="document.location='${pageContext.request.contextPath}'"/>
		  </div>
	  </form:form>
	  
	  
	</jsp:body>
</tsm:page>

