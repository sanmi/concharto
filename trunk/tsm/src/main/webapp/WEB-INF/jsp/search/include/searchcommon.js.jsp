<%-- 
	common javascript functions that may be shared among various views 
	(e.g. eventsearch, embeddedsearch) 
--%>
<script type="text/javascript">
//<![CDATA[

	<%-- global array and index for dealing with overlays --%>
	var _overlays = [];
	var _overlayIndex = 0;
	var _fitToPolygon = [];
	var _fitToPolygonIndex = 0;
	var _accuracy_to_zoom = [4, 5, 7, 10, 11, 12, 13, 14, 15];
	
	<%-- Create a base icon for all of our markers that specifies the
	     shadow, icon dimensions, etc. --%>
	var _baseIcon = new GIcon();
	_baseIcon.shadow = "${basePath}images/icons/shadow50.png";
	_baseIcon.iconSize = new GSize(20, 34);
	_baseIcon.shadowSize = new GSize(37, 34);
	_baseIcon.iconAnchor = new GPoint(9, 34);
	_baseIcon.infoWindowAnchor = new GPoint(9, 2);
	_baseIcon.infoShadowAnchor = new GPoint(18, 25);
		
  <%-- BEGIN OBJECT DEFINITIONS ============================= --%>
  function overlayItem(overlay, html, type, id ) {
  	this.overlay = overlay;
  	this.html = html;
  	this.type = type;
  	this.id = id;
  }
  <%-- END OBJECT DEFINITIONS ============================= --%>
  <%-- BEGIN PRE FUNCTIONS (initialization) ============================= --%>
	function initialize(mapControl) {
		adjustSidebarIE();
		
		initializeMap(mapControl);
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
		var where = document.getElementById("eventSearchForm").where.value
		var zoomOverride = document.getElementById("eventSearchForm").zoomOverride.value;
		if (limitWithinMapBounds() == false)  {
			<%-- fit map to the results --%>
			if (0 != _fitToPolygon.length) {
				var boundsPoly = new GPolyline(_fitToPolygon);
				if (zoomOverride == 'true') {
					document.getElementById("eventSearchForm").zoomOverride.value = 'false';
				}	else if (_fitToPolygon.length > 1){				
					var zoom = map.getBoundsZoomLevel(boundsPoly.getBounds());
					<%-- if they specified a place name, then we only want to zoom out to fit,
					     not zoom in (e.g. only one place matching the criteria in England, we still
					     want to show England --%>
					if ((where != '') && (zoom > map.getZoom())) {
						zoom = map.getZoom();
					}					
				}
				map.setZoom(zoom);
				map.setCenter(boundsPoly.getBounds().getCenter());
			}
		}
	}

	<%-- called by createOverlay --%>
	function createMarker(event) { 
		if ((limitWithinMapBounds() == false) ) {
			<%-- if we are trying to fit the map to the events, we will add 
			     all events to a large poly.  We only do this for events because
			     polygons and lines can span large areas (e.g. an ocean crossing that 
			     ends at Baltimore ) --%>
			_fitToPolygon[_fitToPolygonIndex] = new GLatLng(event.geom.lat, event.geom.lng);
			_fitToPolygonIndex++;
		}
	  <%-- Create a lettered icon for this point using our icon class --%>
	  var letter = String.fromCharCode("A".charCodeAt(0) + _overlayIndex);
	  var letteredIcon = new GIcon(_baseIcon);
	  letteredIcon.image = "${basePath}images/icons/marker" + letter + ".png";
	
	  <%-- Set up our GMarkerOptions object --%>
	  var markerOptions = { icon:letteredIcon };
		var marker = new GMarker(new GLatLng(event.geom.lat, event.geom.lng), markerOptions);
		var html = makeOverlayHtml(event);
		marker.bindInfoWindowHtml(html);
		map.addOverlay(marker);
		
		<%-- record so the user can click on the sidebar and see a popup in the map --%>
		recordOverlay( marker, html, "point", event.id)
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
			recordOverlay(poly, html, "line", event.id)
		}
	}
	
	<%-- record overlay and html so we can pop up a window when the user clicks
	     on info in the sidebar s--%>
	function recordOverlay( overlay, html, type, id) {
		var item = new overlayItem(overlay, html, type, id);
		_overlays[_overlayIndex] = item;
		_overlayIndex++;
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
	
	function zoomTo(id) {
		var index;
		for (var i=0; i<_overlays.length; i++) {
			if (_overlays[i].id == id) {
				index = i;
			}
		}
		overlay = _overlays[index].overlay;
		var point;
		if (_overlays[index].type == "point")	{
			point = overlay.getPoint();
		} else {
			point = overlay.getBounds().getCenter();
		}
		map.setCenter(point);
		if (map.getZoom() < 15) {
			map.setZoom(map.getZoom() +4); <%-- TODO infer this from something else, not sure what --%>
		} else {
			map.zoomIn();
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
		
  <%-- END WHILEFUNCTIONS  ============================= --%>

  <%-- BEGIN POST FUNCTIONS  ============================= --%>

	<%-- user has clicked on 'add to map' --%>
	function editEvent(eventId) {
		document.getElementById("eventSearchForm").editEventId.value = eventId; 
		document.getElementById("eventSearchForm").mapType.value = getMapTypeIndex();
		if (eventId == '') {
			document.getElementById("eventSearchForm").isAddEvent.value = 'true';
		}		
		<%-- don't geocode, but do everything else.  --%>
		document.getElementById("eventSearchForm").mapCenter.value = gLatLngToJSON(map.getCenter());
		saveAndSubmit();		
	}

	function limitWithinMapBounds() {
		return document.getElementById("eventSearchForm").limitWithinMapBounds.checked ; 
	}
	
	function setLimitWithinMapBounds(value) {
		document.getElementById("eventSearchForm").limitWithinMapBounds.value = value;
	}

	<%-- user has clicked on 'search' --%>
	function search() {
		<%-- set the map center, in case we aren't geocoding or the geocode 
		     doesn't succeed --%>
	 	document.getElementById("eventSearchForm").mapCenter.value = gLatLngToJSON(map.getCenter());
  	<%-- Geocode before submitting so that we can get the map extent first!	 --%>
		geocode(document.getElementById("eventSearchForm").where.value);
	}			
	
	<%-- geocode using the search form --%>
	function geocode(address) {
		<%-- geocoder uses a callback mechanism don't submit until we get the results --%>
 		document.getElementById("eventSearchForm").isGeocodeSuccess.value = "true"; 
		if (address) {
    	geocoder.getLocations(address, processGeocode);
    } else {
    	saveAndSubmit(map.getCenter());
    }
	}
	
	function processGeocode(response) {
	  if (!response || response.Status.code != 200) {
	 		document.getElementById("eventSearchForm").isGeocodeSuccess.value = "false"; 
	 		document.getElementById("eventSearchForm").mapCenter.value = gLatLngToJSON(map.getCenter());
	  } else {
	    place = response.Placemark[0];
	    latLng = new GLatLng(place.Point.coordinates[1],
	                        place.Point.coordinates[0]);
			accuracy = place.AddressDetails.Accuracy; 	                        
			<%-- set the center so we can calulate the map bounds to be passed to the geographic search --%>
			map.setCenter(latLng);
			var where = document.getElementById("eventSearchForm").where.value;
			var isEdit = (document.getElementById("eventSearchForm").editEventId.value != null);
			if ((where != '') && (isEdit != "true")) {
				map.setZoom(_accuracy_to_zoom[accuracy]); <%-- TODO infer this from the geocode results!! --%>
			}
			document.getElementById("eventSearchForm").mapCenter.value = gLatLngToJSON(latLng);
		}
		saveAndSubmit();
	}
	
	<%-- callback user to submit form as soon as the geocode is complete --%>
	function saveAndSubmit() {
		var boundingBox = map.getBounds();
		document.getElementById("eventSearchForm").boundingBoxSW.value = 
			gLatLngToJSON(boundingBox.getSouthWest());
		document.getElementById("eventSearchForm").boundingBoxNE.value = 
			gLatLngToJSON(boundingBox.getNorthEast());
		<%-- if the geocode failed, we will get a null.  Pass this back to the controller to indicate
		     that the geocode failed --%>

		document.getElementById("eventSearchForm").mapType.value = getMapTypeIndex();
		document.getElementById("eventSearchForm").mapZoom.value = map.getZoom();
		document.event.submit();
	  
	}

  <%-- END POST FUNCTIONS  ============================= --%>
  
//]]>
</script>
