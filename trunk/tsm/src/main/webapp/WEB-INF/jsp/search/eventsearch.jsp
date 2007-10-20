<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
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
			src="http://maps.google.com/maps?file=api&amp;v=2.x&amp;key=<spring:message code='map.key'/>"
			type="text/javascript">
		</script>		
		<script type="text/javascript">
		//<![CDATA[
		

	<%-- global array and index for dealing with overlays --%>
	var _overlays = [];
	var _overlayIndex = 0;
	
	<%-- Create a base icon for all of our markers that specifies the
	     shadow, icon dimensions, etc. --%>
	var _baseIcon = new GIcon();
	_baseIcon.shadow = "http://www.google.com/mapfiles/shadow50.png";
	_baseIcon.iconSize = new GSize(20, 34);
	_baseIcon.shadowSize = new GSize(37, 34);
	_baseIcon.iconAnchor = new GPoint(9, 34);
	_baseIcon.infoWindowAnchor = new GPoint(9, 2);
	_baseIcon.infoShadowAnchor = new GPoint(18, 25);
		
  <%-- BEGIN OBJECT DEFINITIONS ============================= --%>
  function overlayItem(overlay, html, type) {
  	this.overlay = overlay;
  	this.html = html;
  	this.type = type;
  }
  <%-- END OBJECT DEFINITIONS ============================= --%>
  <%-- BEGIN PRE FUNCTIONS (initialization) ============================= --%>
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

			var eventsJSON = document.getElementById("eventSearchForm").searchResults.value;
			if (eventsJSON != '') {
				var events = eventsJSON.parseJSON();
				createOverlays(events);
			}
		}

		adjustSidebarIE();
	}
	
	<%-- called by createOverlay --%>
	function createMarker(event) { 
	  <%-- Create a lettered icon for this point using our icon class --%>
	  var letter = String.fromCharCode("A".charCodeAt(0) + _overlayIndex);
	  var letteredIcon = new GIcon(_baseIcon);
	  letteredIcon.image = "http://www.google.com/mapfiles/marker" + letter + ".png";
	
	  <%-- Set up our GMarkerOptions object --%>
	  var markerOptions = { icon:letteredIcon };
		var marker = new GMarker(new GLatLng(event.geom.lat, event.geom.lng), markerOptions);
		var html = makeOverlayHtml(event);
		marker.bindInfoWindowHtml(html);
		map.addOverlay(marker);
		
		<%-- record so the user can click on the sidebar and see a popup in the map --%>
		recordOverlay( marker, html, "point")
	}
	
	<%-- called by createOverlay --%>
	function createPoly(event) {
		var points = [];
		var line = event.geom.line;
		
		for (i=0; i<line.length; i++) {
			var vertex = new GLatLng(line[i].lat, line[i].lng);
			points.push(vertex);
		}
		var poly = newPoly(points, event.geom.gtype);
		if (poly) {
			var html = makeOverlayHtml(event);
			GEvent.addListener(poly, "click", function(point) {		    
		    map.openInfoWindowHtml(point, html);
		  });
		  
			map.addOverlay(poly);
	
			<%-- record so the user can click on the sidebar and see a popup in the map --%>
			recordOverlay(poly, html, "line")
		}
	}
	
	<%-- record overlay and html so we can pop up a window when the user clicks
	     on info in the sidebar s--%>
	function recordOverlay( overlay, html, type) {
		var item = new overlayItem(overlay, html, type);
		_overlays[_overlayIndex] = item;
		_overlayIndex++;
	}

	<%-- create html for info bubbles --%>	
	function makeOverlayHtml(event) {
		return createInfoWindowHtml(event) +  
			'<br/><a href="#" onclick="editEvent(' + event.id + ')">edit</a>' +  
			' &nbsp; <a href="#">flag</a><br/></div>';
	}

  <%-- END PRE FUNCTIONS (initialization) ============================= --%>

  <%-- BEGIN WHILE FUNCTIONS  ============================= --%>
	function openMarker(index) {	
		if (_overlays[index].type == "point")	{
			_overlays[index].overlay.openInfoWindowHtml(_overlays[index].html);
		} else {
			overlay = _overlays[index].overlay;
			var point = findClosestVertex(map.getCenter(), overlay);
			map.openInfoWindow(point, _overlays[index].html);
		}
	}
	
	function findClosestVertex(point, overlay) {
		var minDistance = 9999999;
		var closest = 0;
		for (var i=0; i<overlay.getVertexCount(); i++) {
			distance = point.distanceFrom(overlay.getVertex(i));
			if (distance < minDistance) {
				closest = i;
				minDistance = distance;
			}
		}
		return overlay.getVertex(closest);
	}
	
	function adjustSidebarIE() {
		<%-- adjust the map --%>
		setMapExtent();
    	var top = document.getElementById("map").offsetTop;
    	var height = getHeight() - top - 45;
    	document.getElementById("results").style.height=height+"px";
	}
  <%-- END WHILEFUNCTIONS  ============================= --%>

  <%-- BEGIN POST FUNCTIONS  ============================= --%>
	<%-- user has clicked on 'add to map' --%>
	function editEvent(eventId) {
		document.getElementById("eventSearchForm").isEditEvent.value = "true"; 
		document.getElementById("eventSearchForm").eventId.value = eventId; 
		document.getElementById("eventSearchForm").mapType.value = getMapTypeIndex();
		
		<%-- don't geocode, but do everything else.  --%>
		saveAndSubmit(map.getCenter());		
	}

	<%-- user has clicked on 'search' --%>
	function search() {
 		document.getElementById("eventSearchForm").isEditEvent.value = "false"; 
  	<%-- Geocode before submitting so that we can get the map extent first!	 --%>
		geocode(document.getElementById("eventSearchForm").where.value);
	}			
	
	<%-- geocode using the search form --%>
	function geocode(address) {
		<%-- geocoder uses a callback mechanism don't submit until we get the results --%>
 		document.getElementById("eventSearchForm").isGeocodeSuccess.value = "true"; 
		if (address) {
    	geocoder.getLatLng(address, saveAndSubmit);
    } else {
    	saveAndSubmit(map.getCenter());
    }
	}

	<%-- callback user to submit form as soon as the geocode is complete --%>
	function saveAndSubmit (latLng) {
		<%-- set the center so we can calulate the map bounds to be passed to the geographic search --%>
		if (!latLng) {
	 		document.getElementById("eventSearchForm").isGeocodeSuccess.value = "false"; 
	 		document.getElementById("eventSearchForm").mapCenter.value = gLatLngToJSON(map.getCenter());
		} else {
			map.setCenter(latLng);
			document.getElementById("eventSearchForm").mapCenter.value = gLatLngToJSON(latLng);
		}
		
		var boundingBox = map.getBounds();
		document.getElementById("eventSearchForm").boundingBoxSW.value = 
			gLatLngToJSON(boundingBox.getSouthWest());
		document.getElementById("eventSearchForm").boundingBoxNE.value = 
			gLatLngToJSON(boundingBox.getNorthEast());
		<%-- if the geocode failed, we will get a null.  Pass this back to the controller to indicate
		     that the geocode failed --%>
	
		document.getElementById("eventSearchForm").mapZoom.value = map.getZoom();
		document.getElementById("eventSearchForm").currentRecord.value = 0;
		document.getElementById("eventSearchForm").pageCommand.value = '';
		document.event.submit();
	}
	
	<%-- user has clicked on next, prev or a page number --%>
	function nextPage(pageCommand) {
		document.getElementById("eventSearchForm").pageCommand.value = pageCommand;
		document.event.submit();
	}
  <%-- BEGIN POST FUNCTIONS  ============================= --%>

		//]]>
		</script>
	</jsp:attribute>
	<jsp:attribute name="script">map.js,json.js</jsp:attribute>
	<jsp:attribute name="bodyattr">onload="initialize()" onunload="GUnload();" class="mapedit" onresize="adjustSidebarIE();"</jsp:attribute>

	<jsp:body>
		<form:form name="event" id="eventSearchForm" commandName="eventSearch" onsubmit="search(); return false">
			<form:hidden path="boundingBoxSW" htmlEscape="true"/>
			<form:hidden path="boundingBoxNE" htmlEscape="true"/>
			<form:hidden path="mapCenter" htmlEscape="true"/>
			<form:hidden path="searchResults" htmlEscape="true"/>
			<form:hidden path="mapZoom"/>
			<form:hidden path="mapType"/>
			<form:hidden path="isGeocodeSuccess"/>
			<form:hidden path="isEditEvent"/>
			<form:hidden path="eventId"/>
			<form:hidden path="currentRecord"/>
			<form:hidden path="pageCommand"/>
	  	<div>
	     	<span style="position:absolute; left:250px; top:34px;" >
	        <table class="searchbar">
	          <tr>
	            <td class="labelcell">
				    		<form:errors path="where"><span class="errorLabel"></form:errors>
		  	          Where 
				    		<form:errors path="where"></span></form:errors>
                <small>e.g., "gettysburg, pa" </small><br/>
                <form:input path="where" size="22"/>
	            </td>
	            <td class="labelcell">
	              <form:errors path="when"><span class="errorLabel"></form:errors>
	              	When
	              <form:errors path="when"></span></form:errors>
	              <small>
	                e.g. "1962" or "Oct 14, 1066" or "1880-1886" 
	              </small><br/>
	              <form:input path="when" size="35"/>
	            </td>
	            <td class="labelcell">
	            	What
	              <small>
	                e.g. Battle 
	              </small><br/>
	                <form:input path="what" size="22"/>
	            </td>
	          </tr>
	        </table>
	        <input type="submit" name="Search" value="Search" />
	        &nbsp;&nbsp;&nbsp;<input type="button" name="add" value="Add to the Map!" onclick="editEvent('')"/>
	      </span>
		  </div>
		  
		 <%-- Pull the center from the form object so we can center using javascript (see above) --%>
			<table><tbody><tr>			
				<td id="sidebar">
		  		<span class="resultcount">
		  			<c:choose>
		  				<c:when test="${fn:length(events) > 0}">
				  			Displaying <b>${eventSearch.currentRecord} - ${eventSearch.currentRecord + fn:length(events)}</b> Events 
				  			<c:if test="${totalResults > fn:length(events)}"> 
				  				of <b><c:out value="${totalResults}"/></b>
				  			</c:if>
			 				</c:when>
		  				<c:otherwise>
			  				No Events found
		  				</c:otherwise>
		  			</c:choose>
		  		</span>
		    	<div id="results" >
		    		<form:errors path="where" cssClass="errorLabel" element="div"/>
						<form:errors path="when" cssClass="errorLabel" element="div"/>
						<form:errors path="when">
							<div class="errorHint">
								Examples of valid time ranges are:
								<ul>
								<li>2000-2006</li>
								<li>Feb 1884 - Jan 1886</li>
								<li>Jine 22, 1992 12am - June 30, 1992</li>
								<li>1992</li>
								<li>October 1992</li>
								<li>Oct 12, 1992, 12:23:05 am</li>
								</ul>
							</div>
						</form:errors>
						
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
				    <div class="nextPrev">
					    <c:if test="${eventSearch.currentRecord > 0}"> 
						    	<a class="nextPrev" href="#" onclick="nextPage('previous')">Previous</a>
					    </c:if>
					    <c:if test="${totalResults > eventSearch.currentRecord + fn:length(events)}"> 
						    	<a class="nextPrev" href="#" onclick="nextPage('next')">Next</a>
					    </c:if>
				    </div>
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
	  </form:form>
	 
	</jsp:body>
</tsm:page>

