<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tsm"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
request.setAttribute("basePath", basePath);
%>

<tsm:page title="Event">
	<jsp:attribute name="head">
		<script
			<%-- TODO: put the API key a properties file, probably best to use localization api <c:fmt> --%>
			src="http://maps.google.com/maps?file=api&amp;v=2.x&amp;key=ABQIAAAA1DZDDhaKApTfIDHGfvo13hSQekddw1ZVY1OywWYSY7GTmNOxgRQ1UKcA9cKipDAZNLJ5R_X-JJcYhw"
			type="text/javascript">
		</script>		
		<script type="text/javascript">
		//<![CDATA[
		

	<%-- global array and index for dealing with markers --%>
	var markers;
	var markerIndex = 0;
	var markerHtml;
	
	<%-- Create a base icon for all of our markers that specifies the
	     shadow, icon dimensions, etc. --%>
	var baseIcon = new GIcon();
	baseIcon.shadow = "http://www.google.com/mapfiles/shadow50.png";
	baseIcon.iconSize = new GSize(20, 34);
	baseIcon.shadowSize = new GSize(37, 34);
	baseIcon.iconAnchor = new GPoint(9, 34);
	baseIcon.infoWindowAnchor = new GPoint(9, 2);
	baseIcon.infoShadowAnchor = new GPoint(18, 25);
		
	function initialize() {
		initializeMap();
		<%-- map center and map zoom --%>
		var mapCenterJSON = document.getElementById("eventSearchForm").mapCenter.value;
		
		var mapZoom = parseInt(document.getElementById("eventSearchForm").mapZoom.value);

		<%-- set map type from the event --%>
		var mapType = document.getElementById("eventSearchForm").mapType.value;
		if (mapType != '') {
			map.setMapType(G_DEFAULT_MAP_TYPES[mapType]);
		}

		if (mapCenterJSON != "") {
			var mapCenter = mapCenterJSON.parseJSON();
			<%-- recenter the map --%>
			map.setCenter(new GLatLng(mapCenter.lat,mapCenter.lng), mapZoom);			
			drawOverlays();
		}

		adjustSidebarIE();
	}

	function editEvent(eventId) {
		document.getElementById("eventSearchForm").isEditEvent.value = "true"; 
		document.getElementById("eventSearchForm").eventId.value = eventId; 
		document.getElementById("eventSearchForm").mapType.value = getMapTypeIndex();
		
		
		<%-- don't geocode, but do everything else.  --%>
		saveAndSubmit(map.getCenter());		
	}

	function search() {
 		document.getElementById("eventSearchForm").isEditEvent.value = "false"; 
  	<%-- Geocode before submitting so that we can get the map extent first!	 --%>
		geocode(document.getElementById("eventSearchForm").where.value);
	}			
	
	function geocode(address) {
			<%-- geocoder uses a callback mechanism don't submit until we get the results --%>
	    geocoder.getLatLng(address, saveAndSubmit);
	}

	function saveAndSubmit (latLng) {
		map.setCenter(latLng);
		var boundingBox = map.getBounds();
		document.getElementById("eventSearchForm").boundingBoxSW.value = 
			gLatLngToJSON(boundingBox.getSouthWest());
		document.getElementById("eventSearchForm").boundingBoxNE.value = 
			gLatLngToJSON(boundingBox.getNorthEast());
		document.getElementById("eventSearchForm").mapCenter.value = 
			gLatLngToJSON(map.getCenter());
		document.getElementById("eventSearchForm").mapZoom.value = map.getZoom();
		document.event.submit();
	}
	
	<%-- TODO remove duplication with event.jsp --%>
	function drawOverlays() {
			var eventsJSON = document.getElementById("eventSearchForm").searchResults.value;
			var events = eventsJSON.parseJSON();
			<%-- make a new global array for storing the events --%>
			markers = new Array(events.length);
			markerHtml = new Array(events.length);
			
			<%-- create the overlays --%>
			for (var i =0; i<events.length; i++) {
				if (events[i].gtype == 'point') {
			  	map.addOverlay( createMarker(events[i]) );
			  } else if (events[i].gtype == 'line') {
			  	<%-- draw line overlay --%>
			  }
			} 
	}

	function createMarker(event) {
			  <%-- Create a lettered icon for this point using our icon class --%>
			  var letter = String.fromCharCode("A".charCodeAt(0) + markerIndex);
			  var letteredIcon = new GIcon(baseIcon);
			  letteredIcon.image = "http://www.google.com/mapfiles/marker" + letter + ".png";
			
			  <%-- Set up our GMarkerOptions object --%>
			  markerOptions = { icon:letteredIcon };
				var marker = new GMarker(new GLatLng(event.latLng.lat, event.latLng.lng), markerOptions);

				var html = createInfoWindowHtml(event);
				html +=  '<br/><a href="<c:out value="${basePath}"/>/event.htm?listid=' + event.id + '">edit</a>' +  
				' &nbsp; <a href="#">flag</a><br/></div>'
				;
				
				<%-- save this marker. --%>
				markers[markerIndex] = marker;
				markerHtml[markerIndex] = html;
				markerIndex++;

				//marker.bindInfoWindow(document.getElementById("result_A"));
				marker.bindInfoWindowHtml(html);

				return marker;
	}
	
	function adjustSidebarIE() {
		<%-- adjust the map --%>
		setMapExtent();
    	var top = document.getElementById("map").offsetTop;
    	var height = getHeight() - top - 45;
    	document.getElementById("results").style.height=height+"px";
	}

	function openMarker(index) {	
		markers[index].openInfoWindowHtml(markerHtml[index]);
	}

		//]]>
		</script>
	</jsp:attribute>
	<jsp:attribute name="script">map.js,json.js</jsp:attribute>
	<jsp:attribute name="bodyattr">onload="initialize()" onunload="GUnload();" class="mapedit" onresize="adjustSidebarIE();"</jsp:attribute>

	<jsp:body>
  	<div>
     	<span style="position:absolute; left:250px; top:34px;" >
	    <form:form name="event" id="eventSearchForm" commandName="eventSearch" onsubmit="search(); return false">
	    	<form:hidden path="boundingBoxSW" htmlEscape="true"/>
	    	<form:hidden path="boundingBoxNE" htmlEscape="true"/>
	    	<form:hidden path="mapCenter" htmlEscape="true"/>
	    	<form:hidden path="searchResults" htmlEscape="true"/>
	    	<form:hidden path="mapZoom"/>
	    	<form:hidden path="mapType"/>
	    	<form:hidden path="isEditEvent"/>
	    	<form:hidden path="eventId"/>
        <table class="searchbar">
          <tr>
            <td class="labelcell">Where 
                <small>e.g., "gettysburg, pa" </small><br/>
                <form:input path="where" size="22"/>
            </td>
            <td class="labelcell">
              When
              <small>
                e.g. "1962" or "March, 1064" or "1880 - 1886" 
              </small><br/>
              <form:input path="when" size="35"/>
            </td>
            <td class="labelcell">What<br/>
                <form:input path="what" size="22"/>
	          </tr>
	        </table>
	        <input type="submit" name="Search" value="Search" />
	        &nbsp;&nbsp;&nbsp;<input type="button" name="add" value="Add to the Map!" onclick="editEvent(null)"/>
		    </form:form>
        </span>
	  </div>
	  
	 <%-- Pull the center from the form object so we can center using javascript (see above) --%>
		<table><tbody><tr>			
			<td id="sidebar">
	  		<span class="resultcount">${fn:length(events)} Events found</span>
	    	<div id="results" >
		    	<c:set var="test" value="ABCDEFGHIJKLMNOPQRSTUVWXYZ"/>
			    <c:forEach items="${events}" var="event" varStatus="status">
			    	<div class="result">
            	<span class="letter">(<c:out value="${fn:substring(test,status.count-1,status.count)}"/>)</span>
		          <span class="when"><c:out value="${event.when.asText}"/></span>, 
		          <a class="summary" href="#" onclick="openMarker(<c:out value='${status.count-1}'/>)"><c:out value="${event.summary}"/></a><br/>
		          <span class="where"><c:out value="${event.where}"/></span>, <br/>
		          
		          <c:out value="${fn:substring(event.description,0,300)}"/> 
		          <c:if test="${fn:length(event.description) > 300}">
		          	<a class="more" href="#" onclick="openMarker(<c:out value='${status.count-1}'/>)"> ... more</a>
		          </c:if> 
							<br/>	
		          <a  class="links" href="#" onclick="editEvent(<c:out value='${event.id}'/>)">edit</a>
		          &nbsp; <a class="links" href="#">flag</a><br/>
		          
						</div>
			    </c:forEach>
		  	</div>
		  </td>
		  <td>
				<div id="map">
				  <br/><br/>Map coming...
				  <noscript>
				    <p>
				      JavaScript must be enabled to get the map.
				    </p>
				  </noscript>
				</div>
			</td>
		</tr></tbody></table>
	 
	</jsp:body>
</tsm:page>

