<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tsm"%>

<tsm:page title="embedded search">
	<jsp:attribute name="stylesheet">map.css</jsp:attribute>
	<jsp:attribute name="script">prototype-1.7.0.js,map.js,json.js</jsp:attribute>
	<jsp:attribute name="bodyattr">onload="initialize_embedded()" onunload="GUnload();" class="mapedit" onresize="adjustSidebarIE();"</jsp:attribute>
	<jsp:attribute name="stripped">true</jsp:attribute>
	<jsp:attribute name="head">
		<%-- we use includes so we can comment the javascript --%>
		<jsp:include page="../include/mapkey.js.jsp"/>
		<script type="text/javascript">
		//<![CDATA[
		<%-- the main initialize function --%>
		function initialize_embedded() {
			initialize(new GSmallMapControl());
		}
	
		<%-- override create html for info bubbles --%>	
		function makeOverlayHtml(event) {
			return createInfoWindowHtml(event, 350, 150);
		}
	
		<%-- override this function to do nothing --%>
		function adjustSidebarIE() {
		}
		
		//]]>
		</script>
		<jsp:include page="include/searchcommon.js.jsp"/>
	</jsp:attribute>

	<jsp:body>
		<form:form name="event" id="eventSearchForm" commandName="eventSearch" onsubmit="search(); return false">
			<form:hidden path="boundingBoxSW" htmlEscape="true"/>
			<form:hidden path="boundingBoxNE" htmlEscape="true"/>
			<form:hidden path="mapCenter" htmlEscape="true"/>
			<form:hidden path="searchResults" htmlEscape="true"/>
			<form:hidden path="mapZoom"/>
			<form:hidden path="mapType"/>
			<form:hidden path="isGeocodeSuccess"/>
			<form:hidden path="isAddEvent"/>
			<form:hidden path="editEventId"/>
			<form:hidden path="displayEventId"/>
			<form:hidden path="currentRecord"/>
			<form:hidden path="pageCommand"/>
			<form:hidden path="isFitViewToResults"/>
			<form:hidden path="where"/>
			<form:hidden path="when"/>
			<form:hidden path="what"/>
	  
					<div id="map">
					  <br/><br/>Map coming...
					  <noscript>
					    <p>
					      JavaScript must be enabled to get the map.
					    </p>
					  </noscript>
					</div>
			<table><tbody><tr>			
				
			  <td>
					
				</td>
			</tr></tbody></table>
		</form:form>	

	</jsp:body>
</tsm:page>

