<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tsm"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
request.setAttribute("basePath", basePath);
%>

<tsm:page title="Event">
	<jsp:attribute name="head">
		<script
			src="http://maps.google.com/maps?file=api&amp;v=2.x&amp;key=ABQIAAAA1DZDDhaKApTfIDHGfvo13hSQekddw1ZVY1OywWYSY7GTmNOxgRQ1UKcA9cKipDAZNLJ5R_X-JJcYhw"
			type="text/javascript">
		</script>		
		<script type="text/javascript">
		//<![CDATA[
		
	<%-- Create a base icon for all of our markers that specifies the
	     shadow, icon dimensions, etc. --%>
	var _markerIcon = new GIcon();
	_markerIcon.image = "http://labs.google.com/ridefinder/images/mm_20_red.png";
	_markerIcon.shadow = "http://labs.google.com/ridefinder/images/mm_20_shadow.png";
	_markerIcon.iconSize = new GSize(12, 20);
	_markerIcon.shadowSize = new GSize(22, 20);
	_markerIcon.iconAnchor = new GPoint(6, 20);
	_markerIcon.infoWindowAnchor = new GPoint(6, 2);
	_markerIcon.infoShadowAnchor = new GPoint(18, 18);
	
	_entPointIcon = new GIcon();
	_entPointIcon.image = "<c:out value="${basePath}"/>images/icons/square.png";
	_entPointIcon.shadow = "<c:out value="${basePath}"/>images/icons/square_shadow.png";
	_entPointIcon.iconSize = new GSize(10, 10);
	_entPointIcon.shadowSize = new GSize(12, 12);
	_entPointIcon.iconAnchor = new GPoint(5, 5);
	_entPointIcon.infoWindowAnchor = new GPoint(0, 0);
	_entPointIcon.infoShadowAnchor = new GPoint(12, 12);
	
	var _editableMarker;
	var _editablePolyline;
	var _polylineMarkers = [];
	var _currMarker = 0;
		
  <%-- BEGIN PRE FUNCTIONS (initialization) ============================= --%>
	<%-- the main initialize function --%>
	function initialize() {
		initializeMap();

		<%-- check to see whether this is "add" or "edit" --%>
		if (document.getElementById("eventForm").id.value != "") {
			<%-- we are edititing --%>
			<%-- set map type from the event --%>
			var mapType = document.getElementById("eventForm").mapType.value;
			if (mapType != '') {
				map.setMapType(G_DEFAULT_MAP_TYPES[mapType]);
			}
		} 
		
		var eventsJSON = document.getElementById("eventForm").searchResults.value;
		var events = eventsJSON.parseJSON();
		var excludeEventId = document.getElementById("eventForm").id.value;
		createOverlays(events, excludeEventId);
		createEditableOverlay();
		clickListener();		
	}
	
	<%-- If we are editing a line, add listener for clicking on the map --%>
	function clickListener() {
		if (getGeometryType() == "line") {
			GEvent.addListener(map,"click", function(overlay, point) {     
				addMarker(point);
				drawLine();
			});
		}
	}
	
	<%-- create a non-editable polyline from an event --%>
	function createPolyline(event) {
		var points = [];
		var line = event.latLng;
		for (var i=0; i<line.length; i++) {
			var vertex = new GLatLng(line[i].lat, line[i].lng);
			points.push(vertex);
		}
		var polyline = new GPolyline(points,'#FF0000', 4, .5, {geodesic:true});
		
		var html = createInfoWindowHtml(event);
		GEvent.addListener(polyline, "click", function(point) {		    
	    map.openInfoWindowHtml(point, html);
	  });
		map.addOverlay(polyline);
	}
	
	<%-- create a non-editable marker from an event --%>
	function createMarker(event) {
		var point = new GLatLng(event.latLng.lat, event.latLng.lng);
		var marker = new GMarker(point, {icon:_markerIcon});  
		marker.bindInfoWindowHtml(createInfoWindowHtml(event));
		map.addOverlay(marker);
	}
	
	<%-- create and draw the editable overlay --%>
	function createEditableOverlay() {
		var geometryType = getGeometryType();
		if (geometryType == "point") {
			createEditableMarker(getEventFormPoint());
		} else if (geometryType == "line") {
			createEditablePolyline(getEventFormLine());
		}
	}

	<%-- create an editable marker from a json point object --%>
	function createEditableMarker(point) {
		if (point == null) {
			<%-- we are adding a new point here --%>
			point = map.getCenter();
		}
		var marker = new GMarker(point, {draggable: true});
		marker.enableDragging();
		var html = "<b>Drag me</b> <br/>anywhere on the map";
		marker.bindInfoWindowHtml(html);
		map.addOverlay(marker);
		GEvent.addListener(marker, "dragstart", function() {
			map.closeInfoWindow();
		});	
		map.setCenter(point, getZoom()); 
		marker.openInfoWindow(html);
		_editableMarker = marker;
	}
	
	<%-- create an editable polyline from a json line object --%>
	function createEditablePolyline(line) {
		var points = [];
		var marker;
		for (var i=0; i<line.length; i++) {
			var vertex = new GLatLng(line[i].lat, line[i].lng);
			points.push(vertex);
			addMarker(vertex);
		}
		<%-- draw the line between them --%>
		drawLine();

		<%-- we create a polyline here just so we can find the centroid --%>
		var polyline = new GPolyline(points, '#FF0000', 4, .5, {geodesic:true});
		map.setCenter(polyline.getBounds().getCenter(), getZoom());
		
		<%-- if the map is too zoomed in, we should zoom out to fit the line --%>
		fitToOverlay(polyline);
		
		var html = "<b>Drag a point </b> to edit the line.<br/> <b>Click anywhere</b> on the map to add a point";
		drawLine();
	}

	<%-- add a marker to the polyline --%>
	function addMarker(point) {
		<%-- When the user clicks on an overlay, not the map, then the point is null 
				 In that case, we don't want to add apoint --%>
		if (point != null) {
			_polylineMarkers[_currMarker] = new GMarker(point, {icon:_entPointIcon, draggable: true});
			drawMarker(_polylineMarkers[_currMarker], _currMarker);
			_currMarker++;
		}
	}
	
	<%-- draw the marker, add drag listener, add infoWindow --%>
	function drawMarker(marker, index) {
		map.addOverlay(marker);
		marker.enableDragging();
		GEvent.addListener(marker,'drag',function(){
			drawLine()
		});
		var html = '<a href="#" onclick="deleteMarker('+ 
			index +  ')">Delete</a> <a href="#">';
		marker.bindInfoWindowHtml(html);
	}

	<%-- delete an overlay marker --%>
	function deleteMarker(index) {
		var marker = _polylineMarkers[index];
		map.removeOverlay(marker);
		removeFromArray(_polylineMarkers, index); 
		if (_currMarker >0) {
			_currMarker--;
		}
		redrawOverlayMarkers();
		drawLine();
	}
	
	<%-- redraw the overlay vertex markers --%>
	function redrawOverlayMarkers() {
		for (var i=0; i<_polylineMarkers.length; i++) {
			map.removeOverlay(_polylineMarkers[i]);
			drawMarker(_polylineMarkers[i], i);
		}
	}

	<%-- remove item from an array and shift everything down --%>
	function removeFromArray(array, index) {
	  array.splice(index, 1);
	}

	<%-- redraw the line --%>
	function drawLine() {
		if (_editablePolyline) {
			map.removeOverlay(_editablePolyline);
		}
		var points = [];
		for (var i=0; i<_polylineMarkers.length; i++) {
			if (_polylineMarkers[i]) {
				points.push(_polylineMarkers[i].getPoint());
			}
		}		
		_editablePolyline = new GPolyline(points, '#FF0000', 4, .5, {geodesic:true})
		map.addOverlay(_editablePolyline);
	}
	<%-- END PRE FUNCTIONS (initialization) ============================= --%>
	
  <%-- BEGIN MISC FUNCTIONS ============================= --%>
  function getEventFormPoint() {
			var pointJSON = document.getElementById("eventForm").point.value;
			var pt = pointJSON.parseJSON();			
			return new GLatLng(pt.lat, pt.lng)
	}
	
	function getEventFormLine() {
			var lineJSON = document.getElementById("eventForm").line.value;
			return lineJSON.parseJSON();
	}
  
  function getGeometryType() {
		if (document.getElementById("eventForm").geometryType1.checked) {
			return "point";
		} else if (document.getElementById("eventForm").geometryType2.checked) {
			return "line";
		} else {
			return "none";
		}
  }
  
  function getZoom() {
		<%-- set zoom level from the event --%>
		var zoomStr = document.getElementById("eventForm").zoomLevel.value;
		var zoom;
		if (zoomStr == '') {
			zoom = 11;
		} else {
			zoom = parseInt (zoomStr);
		}
		return zoom;
	}

  <%-- END MISC FUNCTIONS ============================= --%>
  
  <%-- BEGIN POST FUNCTIONS ============================= --%>
	<%-- called on form submit --%>	
	function saveEvent() {
		saveGeometry();
		document.getElementById("eventForm").zoomLevel.value = map.getZoom();
		document.getElementById("eventForm").mapType.value = getMapTypeIndex();
		document.event.submit();
	}			

	<%-- saves either point or line or poly depending on edit mode --%>
	function saveGeometry() {
		var geometryType = getGeometryType();
		if (geometryType == "point") {
			document.getElementById("eventForm").point.value = gLatLngToJSON(_editableMarker.getLatLng());
		} else if (geometryType == "line") {
			//TODO FOR DEBUGGING
/*
			var points = [];
			points.push(_editableMarker.getLatLng());
			points.push(map.getCenter());
			points.push(map.getBounds().getSouthWest());
			_editablePolyline = new GPolyline(points);
*/
			document.getElementById("eventForm").line.value = markersToJSON(_polylineMarkers);
		}
	}

	function markersToJSON(markers) {
		var str = '{"line":[';
		for (var i=0; i<markers.length; i++) {
			str += gLatLngToJSON( markers[i].getPoint());
			if (i != markers.length-1) {
				str += ',';
			}
		}
		str +=']}';
		return str;
	}
	
	<%-- addAddressToMap() is called when the geocoder returns an answer.  --%>
	function addAddressToMap(response) {
	  if (!response || response.Status.code != 200) {
	    alert("Sorry, we can't find that location, " + response.Status.code);
	  } else {
	    place = response.Placemark[0];
	    point = new GLatLng(place.Point.coordinates[1],
	                        place.Point.coordinates[0]);
			map.setCenter(point, 13);
			_editableMarker.setLatLng(point);
	    _editableMarker.openInfoWindowHtml(place.address + '<br>' + '<br/><b>Drag me</b> anywhere on the map');
	  }
	}
	
	<%-- showAddress() is called when you click on the Search button
	     in the form.  It geocodes the address entered into the form
	     and adds a marker to the map at that location. --%>
	function showAddress(address) {
	    geocoder.getLocations(address, addAddressToMap);
	}
	
	function changeHistory() {
		var id = document.getElementById("eventForm").id.value;
		if (id != '') {
			document.location="changehistory.htm?id=" + id;
		}
	}
  <%-- END POST FUNCTIONS ============================= --%>
		
		//]]>
		</script>
	</jsp:attribute>
	<jsp:attribute name="script">map.js,json.js</jsp:attribute>
	<jsp:attribute name="bodyattr">onload="initialize()" onunload="GUnload();" class="mapedit" onresize="setMapExtent();"</jsp:attribute>

	<jsp:body>
		<table><tr>			
			<td id="sidebar">
        <form:form name="event" id="eventForm" commandName="event"  method="post" onsubmit="saveEvent(); return false">
					<form:hidden path="id"/>
					<form:hidden path="point" htmlEscape="true"/>
					<form:hidden path="line" htmlEscape="true"/>
					<form:hidden path="zoomLevel"/>
					<form:hidden path="mapType"/>
					<form:hidden path="searchResults" htmlEscape="true"/>
					
   		    <div class="miniTabBar">
   		    	<span class="miniTabSelected">Edit</span>
   		    	<a class="miniTabUnselected" href="#" onclick="changeHistory(); return false;">Change History</a>
	 		    </div>
   		    
   		    <div class="inputcell">
		        
		        <span class="radio">
							Point: <form:radiobutton path="geometryType" value="point" /> 
	        		Line: <form:radiobutton path="geometryType" value="line"/> 
        		</span>
   		    </div>
   		    <div class="inputcell">
		        Summary <br/>
		        <form:input path="summary" size="45" htmlEscape="true"/>
   		    </div>
   		    <div class="inputcell">
   		    	Where
	          <small>e.g., "gettysburg, pa" </small><br/>
	          <form:input path="where" size="45" htmlEscape="true"/>
	          <br/>
	          <input  type="button" name="Find" value="Go to Location" onclick="showAddress(document.event.where.value); return false"/>             
	          <small id="tip"><b>Tip:</b> drag and drop the lollypop!</small>
   		    </div>
   		    <div class="inputcell">
		         When
		         <small>
		           e.g. "1962" or "March, 1064" or "1880 - 1886" <a href="#">hints</a>
		         </small><br/>
		         <form:input path="when" size="45"/>
   		    </div>
   		    <div class="inputcell">
	   		    Description<br/>
						<form:textarea rows="5" cols="35" path="description"/>
   		    </div>
   		    <div class="inputcell">
   		    	Tags<br/>
		        <form:input path="tags" size="45" htmlEscape="true"/>
  		    </div>
   		    <div class="inputcell">
   		    	Source 
			      <form:input path="source" size="45" htmlEscape="true"/>
   		    </div>
					<div class="inputcell">
					 <input type="submit" name="Save" value="Save This Event" />
					 <input type="button" name="Cancel" value="Cancel" onclick="javascript:document.location='eventsearch.htm';"/>
					</div>
	      </form:form>
			</td>		   
			<td>
				<div id="map">
				     Map coming...
				     <noscript>
				       <p>
				         JavaScript must be enabled to get the map.
				       </p>
				     </noscript>
				</div>
			</td>
	 	</tr></table>
	
	</jsp:body>
</tsm:page>

