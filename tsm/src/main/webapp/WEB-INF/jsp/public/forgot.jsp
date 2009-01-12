<%--
Copyright 2009 Time Space Map, LLC

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
--%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tsm"%>


<tsm:page title="Lost Password">
	<jsp:attribute name="bodyattr">onload="document.getElementById('forgotForm').username.focus()"</jsp:attribute>
	<jsp:attribute name="stylesheet">textpanel.css,header.css</jsp:attribute>

	<jsp:body>
	  <form:form id="forgotForm" name="forgot" commandName="forgot"> 
	  	<div class="textpanel">
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

