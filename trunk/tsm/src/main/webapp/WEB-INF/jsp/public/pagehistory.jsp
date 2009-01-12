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

