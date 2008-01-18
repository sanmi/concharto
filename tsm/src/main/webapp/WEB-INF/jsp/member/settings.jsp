<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tsm"%>


<tsm:page title="Settings">
	<jsp:attribute name="stylesheet">textpanel.css,simpleform.css,header.css</jsp:attribute>

	<jsp:body>
	  <form:form name="settings" commandName="settings" > 
	  	<form:hidden path="username"/>
	  	<div class="textpanel">
				<h2>Your Account Settings</h2>
				
		  	<table class="infoBox">
					<colgroup span="2">
						<col class="col1"></col>
						<col class="col2"></col>
					</colgroup>
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
		  	</table>

  			<h2>Change Settings</h2>
				<form:errors path="*" cssClass="errorBox" element="div"/>
  			<c:if test="${success}">
		  		<div class="successMessage" colspan="2">Your changes were successful</div>
				</c:if>

		  	<table class="infoBox">
					<colgroup span="2">
						<col class="col1"></col>
						<col class="col2"></col>
					</colgroup>
					
		  		<tr>
		  			<td>
		  				<form:errors path="email"><span class="errorLabel"></form:errors>
		  				Email
		  				<form:errors path="email"></span></form:errors>
		  			</td>
		  			<td>
		  				<form:input path="email" cssClass="loginText"/>
		  			</td>
		  		</tr>
		  	</table>

		  	<table class="infoBox">
					<colgroup span="2">
						<col class="col1"></col>
						<col class="col2"></col>
					</colgroup>
		  		<tr>
		  		</tr>
		  		<tr>
		  			<td>
		  				<form:errors path="existingPassword"><span class="errorLabel"></form:errors>
		  				Verify Existing Password
		  				<form:errors path="existingPassword"></span></form:errors>
		  			</td>
		  			<td>
		  				<form:password path="existingPassword" cssClass="loginText"/>
		  			</td>
		  		</tr>
		  		<tr>
		  			<td>
		  				<form:errors path="password"><span class="errorLabel"></form:errors>
		  				New Password
		  				<form:errors path="password"></span></form:errors>
		  			</td>
		  			<td>
		  				<form:password path="password" cssClass="loginText"/>
		  			</td>
		  		</tr>
		  		<tr>
		  			<td>
		  				<form:errors path="passwordConfirm"><span class="errorLabel"></form:errors>
		  				Verify New Password
		  				<form:errors path="passwordConfirm"></span></form:errors>
		  			</td>
		  			<td>
		  				<form:password path="passwordConfirm" cssClass="loginText"/>
		  			</td>
		  		</tr>
		  	</table>
			  <input type="submit" value="Update"/>
			  <input type="button" value="Done" onclick="document.location='${pageContext.request.contextPath}/'"/>
		  </div>
	  </form:form>
	  
	  
	</jsp:body>
</tsm:page>

