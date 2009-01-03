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

<tsm:page title="Change History">
	<jsp:attribute name="stylesheet">map.css,wiki.css,header.css</jsp:attribute>
	<jsp:attribute name="bodyattr">class="mapedit"</jsp:attribute>	
	<jsp:attribute name="head">
		<script type="text/javascript">
		//<![CDATA[

	function preview() {
		document.getElementById("discussForm").showPreview.value = 'true';
		document.discussion.submit();
	}
	
	function submitDiscussion() {
		document.getElementById("discussForm").showPreview.value = 'false';
		document.discussion.submit();
	}
	
		//]]>
		</script>
	</jsp:attribute>
	

	<jsp:body>
		<div class="pagepanel">
			<div class="miniTabBar">
				<a class="miniTabUnselected" href="${basePath}edit/event.htm?id=${param.id}">Event</a>
			  <a class="miniTabUnselected miniTabLSpace" href="${basePath}event/discusshistory.htm?id=${discussForm.event.discussion.id}&eventId=${param.id}">Changes</a>
				<span class="miniTabSelected" >Discussion</span>
			</div>
			<div class="changeBar"></div>

	  	
	  	<div id="wikiedit">
	  	
	  		<jsp:include page="../include/discussguidelines.jsp"/>
	  		
	  		<div class="infoBox">
		  		<c:set var="event" scope="request" value="${discussForm.event}"/>
		  		<jsp:include page="../event/include/showsummary.jsp"/>
		  	</div>
	  		
	  		<c:if test="${discussForm.showPreview == true}">
		  		<h1>Preview</h1>
		  		<span class="warning">This is only a preview; changes have not been saved! </span>
			  	<p>
			  	<hr/>
			  	<div class="preview wikitext">
					<wiki:render wikiText="${discussForm.event.discussion.text}"/> 
					</div> 	
	  		</c:if>

				<form:form name="discussion" id="discussForm" commandName="discussForm" >
					<form:hidden path="showPreview" />
					<div>
						<form:textarea path="event.discussion.text"/>
					</div>
		  		<input type="button" value="Submit" onclick="submitDiscussion()"  accesskey="s" title="Save your changes [alt+shift+s]"/>
		  		<input type="button" value="Preview" onclick="preview()" accesskey="p" title="Preview your changes, please use this before saving! [alt+shift+p]"/>
		  		<input type="button" value="Cancel" onclick="document.location='${basePath}event/discuss.htm?id=${param.id}'"/>
	  		</form:form>
	  		
	  		<jsp:include page="../include/wikicopyright.jsp"/>
	  		
			</div>
		</div>
	</jsp:body>
</tsm:page>
