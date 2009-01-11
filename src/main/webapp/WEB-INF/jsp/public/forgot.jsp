<%-- 
 ***** BEGIN LICENSE BLOCK *****
 Version: MPL 1.1
 
 The contents of this file are subject to the Mozilla Public License Version 
 1.1 (the "License"); you may not use this file except in compliance with 
 the License. You may obtain a copy of the License at 
 http://www.mozilla.org/MPL/
 
 Software distributed under the License is distributed on an "AS IS" basis,
 WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 for the specific language governing rights and limitations under the
 License.
 
 The Original Code is Concharto.
 
 The Initial Developer of the Original Code is
 Time Space Map, LLC
 Portions created by the Initial Developer are Copyright (C) 2007 - 2009
 the Initial Developer. All Rights Reserved.
 
 Contributor(s):
 Time Space Map, LLC
 
 ***** END LICENSE BLOCK *****
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

