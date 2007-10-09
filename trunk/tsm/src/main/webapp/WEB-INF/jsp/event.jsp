<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tsm"%>

<tsm:page title="Event">
	<jsp:attribute name="head">
		<script
			src="http://maps.google.com/maps?file=api&amp;v=2.x&amp;key=ABQIAAAA1DZDDhaKApTfIDHGfvo13hSQekddw1ZVY1OywWYSY7GTmNOxgRQ1UKcA9cKipDAZNLJ5R_X-JJcYhw"
			type="text/javascript">
		</script>		
		<script type="text/javascript">
		//<![CDATA[
	function saveEvent() {
		document.getElementById("eventForm").lat.value = marker.getLatLng().lat();
		document.getElementById("eventForm").lng.value = marker.getLatLng().lng();
		document.getElementById("eventForm").zoomLevel.value = map.getZoom();
		var mapTypeIndex = 0;
		for (i=0; i<G_DEFAULT_MAP_TYPES.length; i++) {
			if (G_DEFAULT_MAP_TYPES[i].getName() == map.getCurrentMapType().getName()) {
				mapTypeIndex = i;
			}
		}
		document.getElementById("eventForm").mapType.value = mapTypeIndex;
		document.event.submit();
	}			

	/*
		Called by the main initialize function
	 */
	function initialize() {
			initializeMap();
			var editLocation = false;
			if (document.getElementById("eventForm").lat.value != "") {
				editLocation = true;
			}
			if (editLocation) {
				<%-- set zoom level from the event --%>
				var zoomStr = document.getElementById("eventForm").zoomLevel.value;
				var zoom;
				if (zoomStr == '') {
					zoom = 11;
				} else {
					zoom = parseInt (zoomStr);
				}

				<%-- set map type from the event --%>
				var mapType = document.getElementById("eventForm").mapType.value;
				if (mapType != '') {
					map.setMapType(G_DEFAULT_MAP_TYPES[mapType]);
				}

				<%-- set center from the event --%>
				map.setCenter(new GLatLng(
				document.getElementById("eventForm").lat.value,
				document.getElementById("eventForm").lng.value), zoom); 
			} else {
				<%-- TODO UI WORK: don't have a default starting point --%>
				map.setCenter(new GLatLng(40.879721,-76.998322),11);  //la la land, PA 
			}
			<%-- Add a marker in the center of the map --%>
			var point = map.getCenter();
			marker = new GMarker(point, {draggable: true});
			map.addOverlay(marker);
			marker.enableDragging();
			marker.openInfoWindowHtml("<b>Drag me</b> <br/>anywhere on the map");

			GEvent.addListener(marker, "dragstart", function() {
				map.closeInfoWindow();
			});
		
			GEvent.addListener(marker, "click", function() {
				marker.openInfoWindowHtml(html);
			});		
			
			adjustSidebarIE();
			
			drawPlacemarks();
		}
	
	// addAddressToMap() is called when the geocoder returns an
	// answer.  
	function addAddressToMap(response) {
	  if (!response || response.Status.code != 200) {
	    alert("Sorry, we can't find that location, " + response.Status.code);
	  } else {
	    place = response.Placemark[0];
	    point = new GLatLng(place.Point.coordinates[1],
	                        place.Point.coordinates[0]);
				map.setCenter(point, 13);
				marker.setLatLng(point);
	    marker.openInfoWindowHtml(place.address + '<br>' + '<br/><b>Drag me</b> anywhere on the map');
	  }
	}
	
	// showAddress() is called when you click on the Search button
	// in the form.  It geocodes the address entered into the form
	// and adds a marker to the map at that location.
	function showAddress(address) {
	    geocoder.getLocations(address, addAddressToMap);
	}
	
	
		// Create a base icon for all of our markers that specifies the
	// shadow, icon dimensions, etc.
	var baseIcon = new GIcon();
	baseIcon.shadow = "http://www.google.com/mapfiles/shadow50.png";
	baseIcon.iconSize = new GSize(20, 34);
	baseIcon.shadowSize = new GSize(37, 34);
	baseIcon.iconAnchor = new GPoint(9, 34);
	baseIcon.infoWindowAnchor = new GPoint(9, 2);
	baseIcon.infoShadowAnchor = new GPoint(18, 25);
		
	function drawPlacemarks() {
			var eventsJSON2 = document.getElementById("eventForm").searchResults.value;
			var events = eventsJSON2.parseJSON();
			alert("sdf");  //TODO DEBUG the parseJSON isn't working!!! and remove cut and paste too!
			
			//create the markers
			for (var i =0; i<events.length; i++) {
  			alert("sdf");
			  map.addOverlay( createMarker(events[i]) );
			} 
	}

	function createMarker(event) {
			  // Create a lettered icon for this point using our icon class
			  var letter = String.fromCharCode("A".charCodeAt(0) + markerIndex);
			  var letteredIcon = new GIcon(baseIcon);
			  letteredIcon.image = "http://www.google.com/mapfiles/marker" + letter + ".png";
			
			  // Set up our GMarkerOptions object
			  markerOptions = { icon:letteredIcon };
				var marker = new GMarker(new GLatLng(event.latLng.lat, event.latLng.lng));

				var html = '<b>' + event.summary+'</b><br/>' + event.when + '<br/>' +
				event.description + '<br/>' +
				'<br/><b>Tags: </b>' + event.tags + '<br/>' + 
				'<b>Source: </b>' + event.source + '<br/>' + 
				'<a href="<c:out value="${basePath}"/>/event.htm?listid=' + event.id + '">edit</a>' +  
				' &nbsp; <a href="#">flag</a><br/>'
				;
				//save this marker.
				markers[markerIndex] = marker;
				markerHtml[markerIndex] = html;
				markerIndex++;

				marker.bindInfoWindowHtml(html);

				return marker;
	}
	
	function changeHistory() {
		var id = document.getElementById("eventForm").id.value;
		if (id != '') {
			document.location="changehistory.htm?id=" + id;
		}
	}
		
		//]]>
		</script>
	</jsp:attribute>
	<jsp:attribute name="script">map.js</jsp:attribute>
	<jsp:attribute name="bodyattr">onload="initialize()" onunload="GUnload();" class="mapedit" onresize="setMapExtent();"</jsp:attribute>

	<jsp:body>
		<table><tr>			
			<td id="sidebar">
        <form:form name="event" id="eventForm" commandName="event"  method="post" onsubmit="saveEvent(); return false">
					<form:hidden path="id"/>
					<form:hidden path="lat"/>
					<form:hidden path="lng"/>
					<form:hidden path="zoomLevel"/>
					<form:hidden path="mapType"/>
					<form:hidden path="searchResults" htmlEscape="true"/>
					
   		    <div class="miniTabBar">
   		    	<span class="miniTabSelected">Edit</span>
   		    	<a class="miniTabUnselected" href="#" onclick="changeHistory(); return false;">Change History</a>
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

