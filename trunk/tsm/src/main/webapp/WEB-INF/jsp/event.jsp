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
//	_entPointIcon.shadow = "http://labs.google.com/ridefinder/images/mm_20_shadow.png";
	_entPointIcon.iconSize = new GSize(10, 10);
	_entPointIcon.shadowSize = new GSize(12, 12);
	_entPointIcon.iconAnchor = new GPoint(5, 5);
	_entPointIcon.infoWindowAnchor = new GPoint(0, 0);
	_entPointIcon.infoShadowAnchor = new GPoint(12, 12);
	
	var _editableMarker;
	var _editablePolyline;
		
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
	}
	
	function createPolyline(event) {
		var points = [];
		var line = event.latLng;
		for (i=0; i<line.length; i++) {
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

	function createEditableMarker(point) {
		if (point == null) {
			<%-- we are adding a new point here --%>
			point = map.getCenter();
		}
		marker = new GMarker(point, {draggable: true});
		marker.enableDragging();
		html = "<b>Drag me</b> <br/>anywhere on the map";
		marker.bindInfoWindowHtml(html);
		map.addOverlay(marker);
		GEvent.addListener(marker, "dragstart", function() {
			map.closeInfoWindow();
		});	
		map.setCenter(point, getZoom()); 
		marker.openInfoWindow(html);
		_editableMarker = marker;
	}
	
	function createEditablePolyline(line) {
		var points = [];
		for (i=0; i<line.length; i++) {
			var vertex = new GLatLng(line[i].lat, line[i].lng);
			points.push(vertex);
			marker = new GMarker(vertex, {icon:_entPointIcon});			
			map.addOverlay(marker);
		}
		var polyline = new GPolyline(points,'#FF0000', 4, .5, {geodesic:true});
		
		html = "<b>Drag a point </b> to edit the line";
		GEvent.addListener(polyline, "click", function(point) {		    
	    map.openInfoWindowHtml(point, html);
	  });
		map.addOverlay(polyline);
		map.setCenter(polyline.getBounds().getCenter(), getZoom());
		map.openInfoWindow(map.getCenter(), html);
		_editablePolyline = polyline;
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
			alert ("not implemented");
			//TODO FOR DEBUGGING
/*			var points = [];
			points.push(_editableMarker.getLatLng());
			points.push(map.getCenter());
			var gPolyline = new GPolyline(points);
			*/
			document.getElementById("eventForm").line.value = gPolylineToJSON(_line);
		}
	}

	function gPolylineToJSON(polyline) {
		var str = '{"line":[';
		for (i=0; i<polyline.getVertexCount(); i++) {
			str += gLatLngToJSON( polyline.getVertex(i));
			if (i != polyline.getVertexCount()-1) {
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

