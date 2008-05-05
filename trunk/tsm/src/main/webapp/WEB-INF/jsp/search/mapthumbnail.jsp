<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tsm"%>

<tsm:page title="Map Thumbnail">
	<jsp:attribute name="head">
		<jsp:include page="../include/mapkey.js.jsp"/>
		<script type="text/javascript">
		//<![CDATA[

	<%-- the main initialize function --%>
	function initialize_embedded() {
		initializeMap(new GSmallMapControl());
		addEvent();
   	var height = getHeight();
   	document.getElementById("map").style.height=height+"px";
	}		

	<%-- override this function to do nothing --%>
	function adjustSidebarIE() {
	}
	
	function addEvent() {
		var eventJSON = document.getElementById("mapForm").event.value;
		var event = eventJSON.evalJSON();
		if (event.gtype == 'point') {
		  createMarker(event);
		} else if ((event.gtype == 'line') || (event.gtype == 'polygon')) {
			createPoly(event);					
		}
	}
	
		<%-- called by createOverlay --%>
	function createMarker(event, totalEvents) {
		var point = new GLatLng(event.geom.lat, event.geom.lng);
		var marker = new GMarker(point);
		map.addOverlay(marker);
		map.setCenter(point);
		map.setZoom(11);
	}
	
	<%-- called by createOverlay --%>
	function createPoly(event, totalEvents) {
		var points = [];
		var line = event.geom.line;
		
		for (i=0; i<line.length; i++) {
			var vertex = new GLatLng(line[i].lat, line[i].lng);
			points.push(vertex);
		}
		var poly = newPoly(points, event.geom.gtype);
		if (poly) {
			map.addOverlay(poly);
		}
		fitToPoly(poly);
		map.setCenter(poly.getBounds().getCenter());
	}
		//]]>
		</script>
		<style type="text/css">
			body {
				background-image: none;
			}
		</style>
	</jsp:attribute>
	<jsp:attribute name="script">prototype.js,map.js</jsp:attribute>
	<jsp:attribute name="bodyattr">onload="initialize_embedded()" onunload="GUnload();" class="mapedit" onresize="adjustSidebarIE();"</jsp:attribute>
	<jsp:attribute name="stripped">true</jsp:attribute>

	<jsp:body>
		<form id="mapForm">
			<input type="hidden" name="event" value="<c:out escapeXml="true" value='${event}'/>" >
		</form>
		<div id="map">
			Map coming...
			<noscript>
			  <p>
			    JavaScript must be enabled to get the map.
			  </p>
			</noscript>
		</div>
		
	</jsp:body>
</tsm:page>

