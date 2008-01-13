<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tsm"%>

<tsm:page title="Change History">
	<jsp:attribute name="stylesheet">map.css,simpleform.css,header.css</jsp:attribute>
	<jsp:attribute name="bodyattr">class="mapedit"</jsp:attribute>
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
	

<jsp:body>
			<%-- not sure why I need this -3px jog, can't get it to work from css file either --%>
			<table style="margin:-3px;padding:0"><tr><td>
			<div class="changes">
				<div class="miniTabBar">
					<a class="miniTabSelected" href="${basePath}edit/event.htm?id=${param.id}">Event</a>
 		    	<a class="miniTabUnselected" href="${basePath}event/discuss.htm?id=${param.id}">Discussion</a>
					<span class="miniTabSelected miniTabLSpace" >Changes</span>
	      </div>
	      <div class="changeBar"></div>
	      <div class="simpleForm">
			  	<div class="infoBox">
			  		<c:import url="../event/include/showsummary.jsp"/>
			  	</div>
		
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
			  	<input type="button" value="Back to Search" onclick="javascript:document.location='${basePath}search/eventsearch.htm';" />
	    	</div>
	  	</div>  
	  	</td></tr></table>
</jsp:body>
</tsm:page>

