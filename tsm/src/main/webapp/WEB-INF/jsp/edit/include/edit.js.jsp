<script type="text/javascript">
		//<![CDATA[
		
	<%-- Create a base icon for all of our markers that specifies the
	     shadow, icon dimensions, etc. --%>
	var _markerIcon = new GIcon();
	_markerIcon.image = "${basePath}images/icons/mm_20_red.png";
	_markerIcon.shadow = "${basePath}images/icons/mm_20_shadow.png";
	_markerIcon.iconSize = new GSize(12, 20);
	_markerIcon.shadowSize = new GSize(22, 20);
	_markerIcon.iconAnchor = new GPoint(6, 20);
	_markerIcon.infoWindowAnchor = new GPoint(6, 2);
	_markerIcon.infoShadowAnchor = new GPoint(18, 18);
	
	_entPointIcon = new GIcon();
	_entPointIcon.image = "${basePath}images/icons/square.png";
	_entPointIcon.shadow = "${basePath}images/icons/square_shadow.png";
	_entPointIcon.iconSize = new GSize(10, 10);
	_entPointIcon.shadowSize = new GSize(12, 12);
	_entPointIcon.iconAnchor = new GPoint(5, 5);
	_entPointIcon.infoWindowAnchor = new GPoint(0, 0);
	_entPointIcon.infoShadowAnchor = new GPoint(12, 12);
	
	var _editableMarker;
	var _editablePoly;
	var _polyMarkers = [];
	var _currMarker = 0;
	var _clickListener;
		
  <%-- BEGIN PRE FUNCTIONS (initialization) ============================= --%>
	<%-- the main initialize function --%>
	function initialize() {
		initializeMap();
		
		var mapType = document.getElementById("eventForm").mapType.value;
		<%-- set map type from the event --%>			
		if (mapType != '') {
			map.setMapType(G_DEFAULT_MAP_TYPES[mapType]);				
		}
		var eventsJSON = document.getElementById("eventForm").searchResults.value;
		if (eventsJSON != '') {
			var excludeEventId = document.getElementById("eventForm").id.value;
			var events = eventsJSON.parseJSON();
			createOverlays(events, excludeEventId);
		}
		createEditableOverlay();

		<%-- this is to fix render time rearranging on IE and firefox--%>		
		$('smaller').hide();
		$('smaller').removeClassName('hidden');
		$('larger').removeClassName('hidden');
		
	}
	
	function removeClickListener() {
		GEvent.removeListener(_clickListener);
		_clickListener = null;		
	}
	
	<%-- create a non-editable poly from an event --%>
	function createPoly(event) {
		var points = [];
		var line = event.geom.line;
		for (var i=0; i<line.length; i++) {
			var vertex = new GLatLng(line[i].lat, line[i].lng);
			points.push(vertex);
		}
		var poly = newPoly(points, event.gtype);
		
		var html = createInfoWindowHtml(event);
		GEvent.addListener(poly, "click", function(point) {		    
	    map.openInfoWindowHtml(point, html);
	  });
	  if (poly) {
			map.addOverlay(poly);
	  }
	}
	
	<%-- create a non-editable marker from an event --%>
	function createMarker(event) {
		var point = new GLatLng(event.geom.lat, event.geom.lng);
		var marker = new GMarker(point, {icon:_markerIcon});  
		marker.bindInfoWindowHtml(createInfoWindowHtml(event));
		map.addOverlay(marker);
	}
	
	<%-- create and draw the editable overlay --%>
	function createEditableOverlay() {
		var geometryType = getGeometryType();
		var geom = getEventFormGeom();
		if (geom == null) {
			<%-- this means we are adding to the map.  Default is "point" --%>
			var center = getEventFormCenter();
			createEditableMarker(new GLatLng(center.lat,center.lng));
			
		} else {
			if (geometryType == "point") {
				<%-- if we are switcing from one geometry type to the other, the geometry may not
				match.  TODO make this simpler --%>
				var point;
				if (geom.gtype == 'point') {
					point = new GLatLng(geom.lat, geom.lng);
				} else {
					<%-- we switched from a line or poly to a point - just use the map center --%>
					point = map.getCenter();
				}
				createEditableMarker(point);
			} else if ((geometryType == "line") || (geometryType == "polygon")) {
				createEditablePoly(geom);
				addClickListener();					
			}
		}
	}

	<%-- If we are editing a poly, add listener for clicking on the map --%>
	function addClickListener() {
		<%-- only add if it is missing --%>
		if (_clickListener == null) {
			_clickListener = GEvent.addListener(map,"click", function(overlay, point) {     
				addMarker(point);
				drawPoly();
			});
		}
	}

	<%-- create an editable marker from a json point object --%>
	function createEditableMarker(point) {
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
	
	<%-- create an editable poly from a json poly object --%>
	function createEditablePoly(jsonLine) {
		var points = [];
		var marker;
		var line = jsonLine.line;
		<%-- if we are adding, then line will be null --%>
		if (line) {
			for (var i=0; i<line.length; i++) {
				<%-- If this marker is a polygon clusure, i.e. it is the same as element 0, 
				then we don't want to drag it --%>
				if (!((i!=0) && (line[0].lat == line[i].lat) && (line[0].lng == line[i].lng))) {
					var vertex = new GLatLng(line[i].lat, line[i].lng);
					points.push(vertex);
					addMarker(vertex);
				}			
			}
			<%-- draw the line between them --%>
			//drawPoly();
	
			<%-- we create a poly here just so we can find the centroid --%>
			var poly = new newPoly(points, getGeometryType());
			goToPoly(poly);
		}
		
		showPolyMessage();
		drawPoly();
	}
	
	function goToPoly(poly) {
		if (poly) {
			map.setCenter(poly.getBounds().getCenter(), getZoom());
			<%-- if the map is too zoomed in, we should zoom out to fit the line --%>
			fitToPoly(poly);
		}
	}
	
	function showPolyMessage() {
		var html = " <b>Click anywhere</b> on the map to add a point<br/><b>Drag a point </b> to edit the line.<br/><b>Click a point</b> to delete it.";
    map.openInfoWindowHtml(map.getCenter(), html);
	}

	<%-- add a marker to the poly --%>
	function addMarker(point) {
		<%-- When the user clicks on an overlay, not the map, then the point is null 
				 In that case, we don't want to add apoint --%>
		if (point != null) {
			_polyMarkers[_currMarker] = new GMarker(point, {icon:_entPointIcon, draggable: true});
			drawMarker(_polyMarkers[_currMarker], _currMarker);
			_currMarker++;
		}
	}
	
	<%-- draw the marker, add drag listener, add infoWindow --%>
	function drawMarker(marker, index) {
		map.addOverlay(marker);
		marker.enableDragging();
		GEvent.addListener(marker,'drag',function(){
			drawPoly()
		});
		var html = '<a href="#" onclick="deleteMarker('+ 
			index +  ')">Delete</a> <a href="#">';
		marker.bindInfoWindowHtml(html);
	}

	<%-- delete an overlay marker --%>
	function deleteMarker(index) {
		var marker = _polyMarkers[index];
		map.removeOverlay(marker);
		removeFromArray(_polyMarkers, index); 
		if (_currMarker >0) {
			_currMarker--;
		}
		redrawOverlayMarkers();
		drawPoly();
	}
	
	<%-- redraw the overlay vertex markers --%>
	function redrawOverlayMarkers() {
		for (var i=0; i<_polyMarkers.length; i++) {
			map.removeOverlay(_polyMarkers[i]);
			drawMarker(_polyMarkers[i], i);
		}
	}

	<%-- remove item from an array and shift everything down --%>
	function removeFromArray(array, index) {
	  array.splice(index, 1);
	}

	<%-- redraw the line --%>
	function drawPoly() {
		if (_editablePoly) {
			map.removeOverlay(_editablePoly);
		}
		var points = [];
		for (var i=0; i<_polyMarkers.length; i++) {
			if (_polyMarkers[i]) {
				points.push(_polyMarkers[i].getPoint());
			}
		}		
		_editablePoly = newPoly(points, getGeometryType());
		if (_editablePoly) {
			map.addOverlay(_editablePoly);
		}
	}
	
	
	<%-- END PRE FUNCTIONS (initialization) ============================= --%>

	<%-- BEGIN WHILE FUNCTIONS (initialization) ============================= --%>
	<%-- user is creating a point --%>
	function setupNewPoint() {
		removeEditableOverlay();
		createEditableOverlay();
		removeClickListener();
	}
	
	<%-- user is creating a poly --%>
	function setupNewPoly() {
		removeEditableOverlay();
		for (var i=0; i<_polyMarkers.length; i++) {
			map.addOverlay(_polyMarkers[i]);
		}
		goToPoly(_editablePoly);
		drawPoly();
		showPolyMessage();
		addClickListener();
	}
	
	<%-- remove point AND poly overlay --%>
	function removeEditableOverlay() {
		if (_editableMarker) {
			map.removeOverlay(_editableMarker);
		} 		
		if (_editablePoly) {
			map.removeOverlay(_editablePoly);
			for (var i=0; i<_polyMarkers.length; i++) {
				map.removeOverlay(_polyMarkers[i]);
			}
		}
	}
	
	function smaller() {
		var elements = $('sidebar').getElementsByClassName('expando');
		for (var i=0; i<elements.length; i++) {
			elements[i].setStyle({width: '24em'});
		}
		$('smaller').hide();
		$('larger').show();
	}
		
	function larger() {
		var elements = $('sidebar').getElementsByClassName('expando');
		for (var i=0; i<elements.length; i++) {
			elements[i].setStyle({width: '40em'});
		}
		$('larger').hide();
		$('smaller').show();
	}
	<%-- END WHILE FUNCTIONS (initialization) ============================= --%>
	
  <%-- BEGIN MISC FUNCTIONS ============================= --%>
	function getEventFormGeom() {
			var geomJSON = document.getElementById("eventForm").geometry.value;
			if (geomJSON != '') {
				return geomJSON.parseJSON();			
			} else {
				return null;
			}
	}

	function getEventFormCenter() {
			var centerJSON = document.getElementById("eventForm").mapCenter.value;
			if (centerJSON != '') {
				return centerJSON.parseJSON();			
			} else {
				return null;
			}
	}
	
  function getGeometryType() {
		if (document.getElementById("eventForm").geometryType1.checked) {
			return "point";
		} else if (document.getElementById("eventForm").geometryType2.checked) {
			return "line";
		} else if (document.getElementById("eventForm").geometryType3.checked) {
			return "polygon";
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
		document.getElementById("eventForm").mapCenter.value = gLatLngToJSON(map.getCenter());
		document.event.submit();
	}			

	<%-- saves either point or line or poly depending on edit mode --%>
	function saveGeometry() {
		var geometryType = getGeometryType();
		if (geometryType == "point") {
			document.getElementById("eventForm").geometry.value = gLatLngToJSON(_editableMarker.getLatLng());
		} else if ((geometryType == "line") || (geometryType == "polygon")) {
			document.getElementById("eventForm").geometry.value = markersToJSON(_polyMarkers, geometryType);
		}
	}

	<%-- addAddressToMap() is called when the geocoder returns an answer.  --%>
	function addAddressToMap(response) {
	  if (!response || response.Status.code != 200) {
	    alert("Sorry, we can't find that location, " + response.Status.code);
	  } else {
	    place = response.Placemark[0];
	    point = new GLatLng(place.Point.coordinates[1],
	                        place.Point.coordinates[0]);
			map.setCenter(point);
			_editableMarker.setLatLng(point);
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
	function discuss() {
		var id = document.getElementById("eventForm").id.value;
		if (id != '') {
			document.location="$/event/discuss.htm?id=" + id;
		}
	}
  <%-- END POST FUNCTIONS ============================= --%>
		
		//]]>
		</script>