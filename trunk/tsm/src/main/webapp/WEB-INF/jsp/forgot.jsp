<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tsm"%>


<tsm:page title="Lost Password">
	<jsp:attribute name="bodyattr">onload="document.getElementById('forgotForm').username.focus()"</jsp:attribute>
	<jsp:attribute name="stylesheet">header.css</jsp:attribute>

	<jsp:body>
	  <form:form id="forgotForm" name="forgot" commandName="forgot"> 
	  	<div class="memberForm">
				<h2>Lost password</h2>
				<p>Please enter your username. You will receive an email to help you reset your password.</p>
				<form:errors path="*" cssClass="errorBox" element="div"/>
		  	
				<table>
		  		<tr>
		  			<td>Username</td>
		  			<td>
		  				<form:input path="username"/>
		  			</td>
		  		</tr>
		  	</table>
		  		
			  <input type="submit" value="Submit"/>
		  </div>
	  </form:form>
	  
	</jsp:body>
</tsm:page>

