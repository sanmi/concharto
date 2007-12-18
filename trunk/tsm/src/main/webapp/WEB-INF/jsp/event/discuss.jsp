<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="wiki" uri="wikiRender" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tsm"%>
<% pageContext.setAttribute("linefeed", "\n"); %>

<tsm:page title="Change History">
	<jsp:attribute name="stylesheet">map.css,simpleform.css,header.css,discuss.css</jsp:attribute>
	<jsp:attribute name="bodyattr">class="mapedit"</jsp:attribute>
	

<jsp:body>
			<div class="changes">
				<jsp:include page="include/tabs.jsp"/>
		  	
		  	<div class="simpleForm" >
		  		<div class="infoBox">
			  		<c:choose>
			  			<c:when test="${fn:length(discussion.wikiText.text) >0}">
						  	<input type="button" value="Edit" onclick="document.location='${basePath}edit/discussedit.htm?id=${param.id}'"/>
						  	<wiki:render wikiText="${discussion.wikiText.text}"/>
						  	
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

