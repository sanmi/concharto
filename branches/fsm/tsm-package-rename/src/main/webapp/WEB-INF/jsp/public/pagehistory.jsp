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
<%@ taglib tagdir="/WEB-INF/tags" prefix="tsm"%>

<tsm:page title="Page History">
	<jsp:attribute name="stylesheet">wiki.css,simpleform.css,header.css</jsp:attribute>
	<jsp:attribute name="bodyattr">class="mapedit"</jsp:attribute>

<jsp:body>
	<div class="wikipanel">
		<div class="miniTabBar">
			<a class="miniTabUnselected" href="${basePath}page.htm?page=${param.page}">Page</a>
			<a class="miniTabUnselected  miniTabLSpace" 
		 		href="${basePath}page/edit.htm?page=${param.page}">Edit This Page</a>
			<span class="miniTabSelected" >Changes</span>
		</div>
		<div class="changeBar"></div>
     
    <jsp:include page="../include/wikihistory.jsp"/>
		
	</div>	  	
</jsp:body>
</tsm:page>

