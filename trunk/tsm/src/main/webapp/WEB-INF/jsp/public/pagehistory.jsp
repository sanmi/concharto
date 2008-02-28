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

