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
	<jsp:attribute name="stylesheet">wiki.css,textpanel.css,contributions.css,header.css</jsp:attribute>
	<jsp:attribute name="bodyattr">class="mapedit"</jsp:attribute>
	<jsp:attribute name="head">
		<script type="text/javascript">
		//<![CDATA[

	function preview() {
		document.getElementById("wikiTextForm").showPreview.value = 'true';
		document.wikipage.submit();
	}
	
	function submitDiscussion() {
		document.getElementById("wikiTextForm").showPreview.value = 'false';
		document.wikipage.submit();
	}
	
		//]]>
		</script>
	</jsp:attribute>

<jsp:body>
	<div class="wikipanel">
	
		<div class="miniTabBar">
			<a class="miniTabUnselected" href="${basePath}page.htm?page=${param.page}">Page</a>
			<span class="miniTabSelected  miniTabLSpace" >Edit This Page</span>
		  <c:if test="${wikiTextForm.newPage != true}">
			  <a class="miniTabUnselected" 
			  	href="${basePath}pagehistory.htm?id=${wikiTextForm.wikiText.id}&page=${param.page}">Changes</a>
		  </c:if>
		</div>
		<div class="changeBar"></div>
		<div id="wikiedit">
			<h1>Editing ${param.page}</h1>
	
			<c:if test="${fn:contains(param.page, 'User_talk:')}">
				<jsp:include page="../include/discussguidelines.jsp"/>
			</c:if>
	
	 		<c:if test="${wikiTextForm.showPreview == true}">
	  		<h1>Preview</h1>
	  		<span class="warning">This is only a preview; changes have not been saved! </span>
		  	<p>
		  	<hr/>
		  	<div class="preview wikitext">
				<wiki:render wikiText="${wikiTextForm.wikiText.text}"/> 
				</div> 	
	 		</c:if>
	
	
			<form:form name="wikipage" id="wikiTextForm" commandName="wikiTextForm" >
				<form:hidden path="showPreview" />
			
			
				<div>
					<form:textarea path="wikiText.text"/>
				</div>
			
				<div>
					<input type="button" value="Submit" onclick="submitDiscussion()"  accesskey="s" title="Save your changes [alt+shift+s]"/>
					<input type="button" value="Preview" onclick="preview()" accesskey="p" title="Preview your changes, please use this before saving! [alt+shift+p]"/>
					<input type="button" value="Cancel" onclick="document.location='${basePath}page.htm?page=${param.page}'"/>
				</div>
			</form:form>	
		
			<jsp:include page="../include/wikicopyright.jsp"/>
		</div>
	</div>
						
</jsp:body>
</tsm:page>

