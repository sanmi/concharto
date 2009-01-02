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

<tsm:page title="Discuss Event">
	<jsp:attribute name="stylesheet">map.css,wiki.css,simpleform.css,header.css</jsp:attribute>
	<jsp:attribute name="bodyattr">class="mapedit"</jsp:attribute>
	

<jsp:body>
			<div class="pagepanel">
				<div class="miniTabBar">
					<a class="miniTabUnselected" href="${basePath}edit/event.htm?id=${param.id}">Event</a>
					<span class="miniTabSelected" >Discussion</span>
				  <a class="miniTabUnselected miniTabLSpace" href="${basePath}event/discusshistory.htm?id=${discussForm.event.discussion.id}&eventId=${param.id}">Changes</a>
				</div>
				<div class="changeBar"></div>
		  	
		  	<div class="simpleForm" >
			  	<div class="infoBox">
			  		<c:set var="event" scope="request" value="${discussForm.event}"/>
			  		<c:import url="../event/include/showsummary.jsp"/>
			  	</div>
		  		<div class="infoBox wikitext">
			  		<c:choose>
			  			<c:when test="${fn:length(discussForm.event.discussion.text) >0}">
						  	<input type="button" value="Edit" onclick="document.location='${basePath}edit/discussedit.htm?id=${param.id}'"/>
						  	<wiki:render wikiText="${discussForm.event.discussion.text}"/>
						  	
			  			</c:when>
			  			<c:otherwise>
			  				<p>There is not yet a discussion about this issue.</p>
			  				<p>If you would like to start a discussion, <a href="${basePath}edit/discussedit.htm?id=${param.id}">Create one now.</a>
			  				</p>
			  				<br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/>
			  			</c:otherwise>
			  		</c:choose>
		  		</div>
					<input type="button" value="Back to Search" onclick="javascript:document.location='${basePath}search/eventsearch.htm';" />
				</div>
			</div>
			
</jsp:body>
</tsm:page>

