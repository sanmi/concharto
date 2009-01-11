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
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="wiki" uri="wikiRender" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tsm"%>
<% pageContext.setAttribute("linefeed", "\n"); %>

<tsm:page title="${param.page}">
	<jsp:attribute name="stylesheet">textpanel.css,wiki.css,contributions.css,header.css</jsp:attribute>
	<jsp:attribute name="bodyattr">class="mapedit"</jsp:attribute>

<jsp:body>
<div class="wikipanel">
	<div class="miniTabBar">
		<span class="miniTabSelected" >Page</span>
	  <a class="miniTabUnselected miniTabLSpace" 
	  	href="${basePath}page/edit.htm?page=${param.page}">Edit This Page</a>
	  <c:if test="${wikiTextForm.newPage != true}">
		  <a class="miniTabUnselected" 
		  	href="${basePath}pagehistory.htm?id=${wikiTextForm.wikiText.id}&page=${param.page}">Changes</a>
	  </c:if>
	</div>
	<div class="changeBar"></div>

	<h1>${param.page}</h1>
	<c:choose>
		<c:when test="${wikiTextForm.newPage == true}">
			<div>There is currently no text in this page, you can <a href="${basePath}page/edit.htm?page=${param.page}">edit this page</a></div>
		</c:when>
		<c:otherwise>
			<div class="wikitext">
				<wiki:render wikiText="${wikiTextForm.wikiText.text}"/>
			</div>
		</c:otherwise>
	</c:choose>
	<div class="notifySpace"></div>
	<c:if test="${isAnonymous == true}">
		<div class="infoBox">
		<b>NOTE:</b> This is the discussion page for an anonymous user, identified by the user's numerical IP address. Some IP addresses change periodically, and may be shared by several users. If you are an anonymous user, you may create an account or log in to avoid future confusion with other anonymous users. Registering also hides your IP address.
		</div>
	</c:if>
</div>			
</jsp:body>
</tsm:page>

