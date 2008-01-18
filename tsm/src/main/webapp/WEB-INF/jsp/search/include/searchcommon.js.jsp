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
	var _initialZoom;
	var _initialCenter;
	
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
		
		var zoomTxt = $('mapZoom').value;
		var mapZoom;
		if (!isEmpty(zoomTxt)) {
			mapZoom = parseInt(zoomTxt);
		}
		<%-- set map type from the event --%>
		var mapType = $('mapType').value;
		if (mapType != '') {
			map.setMapType(G_DEFAULT_MAP_TYPES[mapType]);
		}
		
		var mapCenter = getMapCenterFromJSON();
		if (null != mapCenter) {
			<%-- recenter the map --%>
			map.setCenter(new GLatLng(mapCenter.lat,mapCenter.lng), mapZoom);			

			var eventsJSON = $('searchResults').value;
			if (eventsJSON != '') {
				var events = eventsJSON.parseJSON();
				createOverlays(events);
			}
		}
		adjustSidebarIE();
		if ($('embed').value == 'true') {
			<%-- always fit to results for embedded maps --%>
			fitToResults();
		} else if (limitWithinMapBounds() == false)  {
			if (!($('mapCenterOverride').value == 'true') || !($('zoomOverride').value == 'true')) {
				fitToResults();
			} else {
				if (($('mapCenterOverride').value == 'true') && ((0 != _fitToPolygon.length))) {
					$('mapCenterOverride').value = 'false';
				} 
				if ($('zoomOverride').value == 'true') {
					<%-- reset it --%>
					$('zoomOverride').value = 'false'; 
				}
			}
		}
		<%-- This is for creating links.  We only override zoom and center if the user has
		     changed the map since the page was rendered (e.g. initialize() was called) --%>
		_initialZoom = map.getZoom();
		_initialCenter = map.getCenter();
	}
	
	function getMapCenterFromJSON() {
		var mapCenterJSON = $('mapCenter').value;
		if (mapCenterJSON != "") {
			return mapCenterJSON.parseJSON();
		} else {
			return null;
		} 			
	}
	
	function fitToResults() {
		var boundsPoly = new GPolyline(_fitToPolygon);
		var zoom; 
		if (_fitToPolygon.length >= 2){							
			zoom = map.getBoundsZoomLevel(boundsPoly.getBounds());
			<%-- if they specified a place name, then we only want to zoom out to fit,
			     not zoom in (e.g. only one place matching the criteria in England, we still
			     want to show England --%>
			if (!isEmpty($('where').value) && (zoom > map.getZoom())) {
				zoom = map.getZoom();
			}
		} else if (_fitToPolygon.length == 1) {
			zoom=12; //city level
		}
		if (_fitToPolygon.length >0) {
			map.setZoom(zoom);
			map.setCenter(getBoundsCenter(boundsPoly));
		}
	}
	
	function getBoundsCenter(boundsPoly) {
		<%-- if there is only one point, we don't do a fit, we just zoom to the point --%>		
		if (_fitToPolygon == 1) {
			return _fitToPolygon[0];
		} else {
			return boundsPoly.getBounds().getCenter();
		}
	}
	
	<%-- called by createOverlay --%>
	function createMarker(event) { 
		updateFitToPolygon(new GLatLng(event.geom.lat, event.geom.lng));
		
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
	
	function updateFitToPolygon(gll) {
		if ((limitWithinMapBounds() == false) ) {
			<%-- if we are trying to fit the map to the events, we will add 
			     all events to a large poly.  We only do this for events because
			     polygons and lines can span large areas (e.g. an ocean crossing that 
			     ends at Baltimore ) --%>
			_fitToPolygon[_fitToPolygonIndex] = gll;
			_fitToPolygonIndex++;
		}
	}
	<%-- called by createOverlay --%>
	function createPoly(event) {
		var points = [];
		var line = event.geom.line;
		
		for (i=0; i<line.length; i++) {
			var vertex = new GLatLng(line[i].lat, line[i].lng);
			points.push(vertex);
			updateFitToPolygon(vertex);
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
	
  function isEmpty(value) {
		return ((null == value) || ('' == value));
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
