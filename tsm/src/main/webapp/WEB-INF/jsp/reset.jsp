<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tsm"%>


<tsm:page title="Settings">
	<jsp:attribute name="stylesheet">simpleform.css,header.css</jsp:attribute>

	<jsp:body>
	  <form:form name="settings" commandName="reset" > 
	  	<form:hidden path="username"/>
	  	<div class="memberForm">
				<h2>Reset your password</h2>
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
		  	</table>
			  <input type="submit" value="Update"/>
		  </div>
	  </form:form>
	  
	  
	</jsp:body>
</tsm:page>
