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
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tsm"%>


<tsm:page title="Login">
	<jsp:attribute name="bodyattr">onload="document.getElementById('loginForm').username.focus()"</jsp:attribute>
	<jsp:attribute name="stylesheet">textpanel.css,header.css,simpleform.css</jsp:attribute>

	<jsp:body>
	
 	<div class="textpanel">
		<div class="title"><h1>Sign in to Concharto</h1></div>
		<%--  if we have been redirected to this page from the loginfilter because the page requires
				  authentication and authorization, it will add '?r' to the URL.  This tells us to display the following message --%>
		<c:if test="${pageContext.request.queryString == 'r'}">
			<div class="infoMessage" >You need to sign up as a member to use this feature</div>
			<div class="clearfloat"></div>
		</c:if>
			<table style="margin:0;padding:0"><tr>
				<td class="infoBox" style="width:25em;vertical-align: top">
					<form:form id="loginForm" name="login" commandName="login" action="login.htm"> 
						<h2>Login</h2>
						
						<div class="inputcell">
							<div class="inputlabel">Username</div>
							<div class="textinput"><form:input path="username" cssClass="loginText"/></div>
							<c:if test="${login.fromController == 'login'}">
								<form:errors path="username" cssClass="errorText" element="div"/>
							</c:if>
						</div>
						<div class="inputcell">
							<div class="inputlabel">Password</div>
							<div class="textinput"><form:password path="password" cssClass="loginText"/></div>
							<c:if test="${login.fromController == 'login'}">
								<form:errors path="password" cssClass="errorText" element="div"/>
							</c:if>
						</div>
						<div class="inputcell">
							<div class="inputlabel">
                <form:checkbox path="rememberMe"/>
                Remember me next time
              </div>
							<div class="inputlabel"><a href="${basePath}forgot.htm">Lost your password?</a></div>
						</div>		  	
					  <input type="submit" value="Login"/>
				  </form:form>
				</td>
				<%-- signup form --%>
				<td class="infoBox" style="width:30em;vertical-align: top">
					<form:form id="signupForm" name="signup" commandName="login" action="signup.htm"> 
			  		<h2>Sign Up</h2>
			  		<p>If you aren't already a member, please create an account.  It's easy and it's free.</p>
						<div class="inputcell">
							<div class="inputlabel">Username</div>
							<div class="textinput">
								<form:input path="username" maxlength="${SZ_USERNAME}" cssClass="loginText"/>
							</div>
							<c:if test="${login.fromController == 'signup'}">
								<form:errors path="username" cssClass="errorText" element="div"/>
							</c:if>
						</div>
						<div class="inputcell">
							<div class="inputlabel">Password</div>
							<div class="textinput">
								<form:password path="password" maxlength="${SZ_PASSWORD}" cssClass="loginText"/>
							</div>
							<c:if test="${login.fromController == 'signup'}">
								<form:errors path="password" cssClass="errorText" element="div"/>
							</c:if>
						</div>
						<div class="inputcell">
							<div class="inputlabel">Password (confirm)</div>
							<div class="textinput">
								<form:password path="passwordConfirm" maxlength="${SZ_PASSWORD}" cssClass="loginText"/>
							</div>
							<c:if test="${login.fromController == 'signup'}">
								<form:errors path="passwordConfirm" cssClass="errorText" element="div"/>
							</c:if>
						</div>
						<div class="inputcell">
							<div class="inputlabel">Email address</div>
							<div class="textinput">
								<form:input path="email" maxlength="${SZ_EMAIL}" cssClass="loginText"/>
							</div>
							<c:if test="${login.fromController == 'signup'}">
								<form:errors path="email" cssClass="errorText" element="div"/>
							</c:if>
						</div>
						<div class="inputcell">
							<div class="inputlabel">
								<form:checkbox path="agreeToTermsOfService"/>
				  			I agree with the Concharto <a href="http://wiki.concharto.com/wiki/Legal">Terms of Service</a>								
							</div>
							<c:if test="${login.fromController == 'signup'}">
								<form:errors path="agreeToTermsOfService" cssClass="errorText" element="div"/>
							</c:if>
							<div class="inputlabel">
			  				<form:checkbox path="rememberMe"/>
	  						Remember me next time
							</div>
						</div>		  	
						
					  <input type="submit" value="Signup"/>
				  </form:form>				
				</td>
			</tr></table>
			  
	  </div>
	  
	</jsp:body>
</tsm:page>

