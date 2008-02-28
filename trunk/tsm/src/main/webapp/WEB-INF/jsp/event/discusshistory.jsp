<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="display" uri="http://displaytag.sf.net" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tsm"%>

<tsm:page title="Discussion History">
	<jsp:attribute name="stylesheet">map.css,simpleform.css,header.css</jsp:attribute>
	<jsp:attribute name="bodyattr">class="mapedit"</jsp:attribute>

<jsp:body>
	<div class="pagepanel">
		<div class="miniTabBar">
			<a class="miniTabUnselected" href="${basePath}edit/event.htm?id=${param.eventId}">Event</a>
	   	<a class="miniTabSelected" href="${basePath}event/discuss.htm?id=${param.eventId}">Discussion</a>
			<span class="miniTabSelected miniTabLSpace" >Changes</span>
    </div>
    <div class="changeBar"></div>

		<jsp:include page="../include/wikihistory.jsp"/>
		
	</div>	  	
</jsp:body>
</tsm:page>

