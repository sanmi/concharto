<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="display" uri="http://displaytag.sf.net" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tsm"%>

<tsm:page title="Flags">
	<jsp:attribute name="head">
		<script type="text/javascript">
		//<![CDATA[

	function confirmAndSubmit(id, disposition) {
		var ok = true;
		if (disposition == 'deleted') {
			if (!confirm("Deleting an event is permanent. Are you sure?")) {
				ok=false;			
			}
		}
		if (ok) {
			document.location = '${basePath}admin/flagdisposition.htm?id=' + id + '&disposition=' + disposition;
		}
	}
		//]]>
		</script>
				
	</jsp:attribute>
	<jsp:attribute name="stylesheet">map.css,simpleform.css,header.css</jsp:attribute>
	<jsp:attribute name="bodyattr">class="mapedit"</jsp:attribute>

<jsp:body>
	<div class="simpleForm">

		<input class="action" type="submit" value="Return to Search" onclick="document.location='${basePath}search/eventsearch.htm'"/>
		
		<jsp:include page="include/showevent.jsp"/>
		
		<c:if test="${fn:length(event.flags) > 0}">
			<div class="infoBox">
				<h2>Flags on this Event</h2>
				<jsp:include page="include/showflaghistory.jsp"/>
			</div>
		</c:if>
		
		<div class="infoBox">
			<h2>Change History</h2>
			<jsp:include page="include/showchangehistory.jsp"/>
		</div>

		<input class="action" type="submit" value="Return to Search" onclick="document.location='${basePath}search/eventsearch.htm'"/>

	</div>
</jsp:body>
</tsm:page>

