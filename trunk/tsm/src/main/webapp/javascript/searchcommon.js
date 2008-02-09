/* 
	common javascript functions that may be shared among various views 
	(e.g. eventsearch, embeddedsearch) 
*/

	/* global array and index for dealing with overlays */
	var _overlays = [];
	var _overlayIndex = 0;
	var _fitToPolygon = [];
	var _fitToPolygonIndex = 0;
	var _accuracy_to_zoom = [4, 5, 7, 10, 11, 12, 13, 14, 15];
	var _initialZoom;
	var _initialCenter;
	var _basePath;
	
	/* Create a base icon for all of our markers that specifies the
	     shadow, icon dimensions, etc. */
	var _baseIcon = new GIcon();
		
	function initializeVars() {
		_basePath = $('basePath').value;
		_baseIcon.shadow = _basePath+"images/icons/shadow50.png";
		_baseIcon.iconSize = new GSize(20, 34);
		_baseIcon.shadowSize = new GSize(37, 34);
		_baseIcon.iconAnchor = new GPoint(9, 34);
		_baseIcon.infoWindowAnchor = new GPoint(9, 2);
		_baseIcon.infoShadowAnchor = new GPoint(18, 25);
	}
	
  /* BEGIN OBJECT DEFINITIONS ============================= */
  function overlayItem(overlay, html, type, id ) {
  	this.overlay = overlay;
  	this.html = html;
  	this.type = type;
  	this.id = id;
  	this.isHighlighted = false;
  }
  
  /* line object */
  function Line(vertex0, vertex1) {
	  this.v0 = vertex0;
	  this.v1 = vertex1;
  } 
  
  /* END OBJECT DEFINITIONS ============================= */
  /* BEGIN PRE FUNCTIONS (initialization) ============================= */
  
  /* Main initialization function */
	function initialize(mapControl) {
		initializeVars();
		adjustSidebarIE();
		initializeMap(mapControl);
		/* map center and map zoom */
		
		var zoomTxt = $('mapZoom').value;
		var mapZoom;
		if (!isEmpty(zoomTxt)) {
			mapZoom = parseInt(zoomTxt);
		}
		/* set map type from the event */
		var mapType = $('mapType').value;
		if (mapType != '') {
			map.setMapType(G_DEFAULT_MAP_TYPES[mapType]);
		}
		
		var mapCenter = getMapCenterFromJSON();
		if (null != mapCenter) {
			/* recenter the map */
			map.setCenter(new GLatLng(mapCenter.lat,mapCenter.lng), mapZoom);			

			var eventsJSON = $('searchResults').value;
			if (eventsJSON != '') {
				var events = eventsJSON.evalJSON();
				createOverlays(events);
			}
		}
		adjustSidebarIE();
		if ($('embed').value == 'true') {
			/* always fit to results for embedded maps */
			fitToResults();
		} else if (limitWithinMapBounds() == false)  {
			if (!($('mapCenterOverride').value == 'true') || !($('zoomOverride').value == 'true')) {
				fitToResults();
			} else {
				if (($('mapCenterOverride').value == 'true') && ((0 != _fitToPolygon.length))) {
					$('mapCenterOverride').value = 'false';
				} 
				if ($('zoomOverride').value == 'true') {
					/* reset it */
					$('zoomOverride').value = 'false'; 
				}
			}
		}
		/* This is for creating links.  We only override zoom and center if the user has
		     changed the map since the page was rendered (e.g. initialize() was called) */
		_initialZoom = map.getZoom();
		_initialCenter = map.getCenter();
		
		/* to unhighlight polygons if there are any */
		GEvent.addListener(map, "infowindowclose", function() {
		  unhighlightOverlay();
		});

		//listeners for hiding polygons when you are zoomed way in
		GEvent.addListener(map, "zoomend", function() {
		  zoomendListener();
		});
		GEvent.addListener(map, "moveend", function() {
		  zoomendListener();
		});
		
	}
	
	/* Don't show polygons when we are zoomed so far in that we can't see them */
	function zoomendListener() { 
		_overlays.each( function(item, index){
			if (item.type == 'polygon') {
				//alert('The item in the position #' + index + ' is:' + item.type);
				var overlay = item.overlay;
				for (var i=0; i<overlay.getVertexCount(); i++) {
					var vertex = overlay.getVertex(i);
					//if vertex within the map OR
					//if a line between this vertex and the last intersects the map
					if ((map.getBounds().contains(vertex)) ||
							((i+1 < overlay.getVertexCount()) && intersectsMap(vertex, overlay.getVertex(i+1)))) 
					{
						overlay.show();
						break;
					} 
					//ok, there are no vertexes within the map, so we should hide this overlay
					overlay.hide();
				}
			}
		});
	}
	
	/* returns true if a line from v1 to v2 intersects the current map bounds at any point */
	function intersectsMap(v0, v1) {
		var testLine = new Line(v0, v1);
		var bounds = map.getBounds();
		
		var sw = bounds.getSouthWest();
		var ne = bounds.getNorthEast()
		var nw = new GLatLng(ne.lat(), sw.lng());
		var se = new GLatLng(sw.lat(), ne.lng());
		var diag1 = new Line(sw, ne);
		var diag2 = new Line(nw, se);
		//if the line intersects either diagonal line, then it intersects the rectangle
		return intersectsLine(testLine, diag1) || intersectsLine(testLine, diag2);  
	}
	
	/* returns true if line1 intersects line2 
	 * from: http://en.wikipedia.org/wiki/Line-line_intersection */
	function intersectsLine(l0, l1) {
		
		var a1 = new Point2D(l0.v0.lng(), l0.v0.lat());  
		var a2 = new Point2D(l0.v1.lng(), l0.v1.lat());  
		var b1 = new Point2D(l1.v0.lng(), l1.v0.lat());  
		var b2 = new Point2D(l1.v1.lng(), l1.v1.lat());  

		var intersection = intersectLineLine(a1, a2, b1, b2);		

		return intersection.status == 'Intersection';
	}

	
	/* Get the map center from the html form, return null if none was provided */
	function getMapCenterFromJSON() {
		var mapCenterJSON = $('mapCenter').value;
		if (mapCenterJSON != "") {
			return mapCenterJSON.evalJSON();
		} else {
			return null;
		} 			
	}
	
	/* Fit the map center and zoom level to the search results */
	function fitToResults() {
		var boundsPoly = new GPolyline(_fitToPolygon);
		var zoom; 
		if (_fitToPolygon.length >= 2){							
			zoom = map.getBoundsZoomLevel(boundsPoly.getBounds());
			/* if they specified a place name, then we only want to zoom out to fit,
			     not zoom in (e.g. only one place matching the criteria in England, we still
			     want to show England */
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
	
	/* return the center of the given GOverlay object */
	function getBoundsCenter(boundsPoly) {
		/* if there is only one point, we don't do a fit, we just zoom to the point 
		   TODO - fix this hack */		
		if (_fitToPolygon == 1) {
			return _fitToPolygon[0];
		} else {
			return boundsPoly.getBounds().getCenter();
		}
	}
	
	/* called by createOverlay */
	function createMarker(event) { 
		updateFitToPolygon(new GLatLng(event.geom.lat, event.geom.lng));
		
	  /* Create a lettered icon for this point using our icon class */
	  var letter = String.fromCharCode("A".charCodeAt(0) + _overlayIndex);
	  var letteredIcon = new GIcon(_baseIcon);
	  letteredIcon.image = _basePath+"images/icons/marker" + letter + ".png";
	
	  /* Set up our GMarkerOptions object */
	  var markerOptions = { icon:letteredIcon };
		var marker = new GMarker(new GLatLng(event.geom.lat, event.geom.lng), markerOptions);
		var html = makeOverlayHtml(event);
		marker.bindInfoWindowHtml(html);
		map.addOverlay(marker);
		
		/* record so the user can click on the sidebar and see a popup in the map */
		recordOverlay( marker, html, "point", event.id)
	}
	
	/* Add to a global polygon object that is used to fit the map to the
	 * search results.	 */
	function updateFitToPolygon(gll) {
		if ((limitWithinMapBounds() == false) ) {
			_fitToPolygon[_fitToPolygonIndex] = gll;
			_fitToPolygonIndex++;
		}
	}
	/* called by createOverlay */
	function createPoly(event) {
		var points = [];
		var line = event.geom.line;
		
		for (i=0; i<line.length; i++) {
			var vertex = new GLatLng(line[i].lat, line[i].lng);
			points.push(vertex);
			/*if the user specified "where", then we should not try to fit the map to all
			  polygons or lines in the search results, otherwise very large polys or lines could force
			  the map to be zoomed way out (e.g. an explorer path that crosses the ocean, but passes 
			  nearby New York City)
			*/ 
			if (isEmpty($('where').value)) {
				updateFitToPolygon(vertex);
			} 
		}
		var poly = newPoly(points, event.geom.gtype);
		if (poly) {
			/* record so the user can click on the sidebar and see a popup in the map */
			var html = makeOverlayHtml(event);
			var overlayItem = recordOverlay(poly, html, event.gtype, event.id)
			addOverlayClickListener(overlayItem);
		  
			map.addOverlay(poly);
		}
	}
	
	/* Listener for polys and lines */
	function addOverlayClickListener(overlayItem) {
			GEvent.addListener(overlayItem.overlay, "click", function(point) {
		    map.openInfoWindowHtml(point, overlayItem.html);
		    highlightOverlay(overlayItem);
		  });
	}
	
	/* record overlay and html so we can pop up a window when the user clicks
	     on info in the sidebar s*/
	function recordOverlay( overlay, html, type, id) {
		var item = new overlayItem(overlay, html, type, id);
		_overlays[_overlayIndex] = item;
		_overlayIndex++;
		return item;
	}


  /* END PRE FUNCTIONS (initialization) ============================= */

  /* BEGIN WHILE FUNCTIONS  ============================= */
	function openMarker(index) {	
		if (_overlays[index].type == "point")	{
			_overlays[index].overlay.openInfoWindowHtml(_overlays[index].html);
		} else {
			var overlay = _overlays[index].overlay;
			unhighlightOverlay();
			var point = findClosestVertex(map.getCenter(), overlay);
			map.openInfoWindow(point, _overlays[index].html);
			highlightOverlay(_overlays[index]);
		}
	}
	
	function highlightOverlay(overlayItem) {
		var newOverlay = redrawOverlay(overlayItem, LINE_WEIGHT_HIGHLIGHT, LINE_COLOR_HIGHLIGHT, POLY_COLOR_HIGHLIGHT);
		overlayItem.isHighlighted = true;
		overlayItem.overlay = newOverlay;
		addOverlayClickListener(overlayItem);
	}
	
	function redrawOverlay(overlayItem, weight /* optional */, lineColor /* optional */, polyColor /* optional */) {
		var overlay = overlayItem.overlay;
		map.removeOverlay(overlay);
		var points = new Array();
		for (var i=0; i<overlay.getVertexCount(); i++) {
			points[i] = overlay.getVertex(i);
		}
		overlay = newPoly(points, overlayItem.type, weight, lineColor, polyColor);
		map.addOverlay(overlay);
		return overlay;
	}
	
	function unhighlightOverlay() {
		for (var ov=0; ov<_overlays.length; ov++) {
			if (_overlays[ov].isHighlighted == true) {
				_overlays[ov].overlay = redrawOverlay(_overlays[ov], LINE_WEIGHT, LINE_COLOR, POLY_COLOR);
				_overlays[ov].isHighlighted = false;
				addOverlayClickListener(_overlays[ov]);
			} 
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
			map.setZoom(map.getZoom() +4); /* TODO infer this from something else, not sure what */
		} else {
			map.zoomIn();
		}
	}
	
  function isEmpty(value) {
		return ((null == value) || ('' == value));
	}
	
		
  /* END WHILEFUNCTIONS  ============================= */

  /* BEGIN POST FUNCTIONS  ============================= */

	/* user has clicked on 'add to map' */
	function editEvent(eventId) {
		document.getElementById("eventSearchForm").editEventId.value = eventId; 
		document.getElementById("eventSearchForm").mapType.value = getMapTypeIndex();
		if (eventId == '') {
			document.getElementById("eventSearchForm").isAddEvent.value = 'true';
		}		
		/* don't geocode, but do everything else.  */
		document.getElementById("eventSearchForm").mapCenter.value = gLatLngToJSON(map.getCenter());
		saveAndSubmit();		
	}

	function limitWithinMapBounds() {
		return document.getElementById("eventSearchForm").limitWithinMapBounds.checked ; 
	}
	
	function setLimitWithinMapBounds(value) {
		document.getElementById("eventSearchForm").limitWithinMapBounds.value = value;
	}

	/* user has clicked on 'search' */
	function search() {
		/* set the map center, in case we aren't geocoding or the geocode 
		     doesn't succeed */
	 	document.getElementById("eventSearchForm").mapCenter.value = gLatLngToJSON(map.getCenter());
  	/* Geocode before submitting so that we can get the map extent first!	 */
		geocode(document.getElementById("eventSearchForm").where.value);
	}			
	
	/* geocode using the search form */
	function geocode(address) {
		/* geocoder uses a callback mechanism don't submit until we get the results */
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
			/* set the center so we can calulate the map bounds to be passed to the geographic search */
			map.setCenter(latLng);
			var where = document.getElementById("eventSearchForm").where.value;
			var isEdit = (document.getElementById("eventSearchForm").editEventId.value != null);
			if ((where != '') && (isEdit != "true")) {
				map.setZoom(_accuracy_to_zoom[accuracy]); /* TODO infer this from the geocode results!! */
			}
			document.getElementById("eventSearchForm").mapCenter.value = gLatLngToJSON(latLng);
		}
		saveAndSubmit();
	}
	
	/* callback user to submit form as soon as the geocode is complete */
	function saveAndSubmit() {
		var boundingBox = map.getBounds();
		document.getElementById("eventSearchForm").boundingBoxSW.value = 
			gLatLngToJSON(boundingBox.getSouthWest());
		document.getElementById("eventSearchForm").boundingBoxNE.value = 
			gLatLngToJSON(boundingBox.getNorthEast());
		/* if the geocode failed, we will get a null.  Pass this back to the controller to indicate
		     that the geocode failed */

		document.getElementById("eventSearchForm").mapType.value = getMapTypeIndex();
		document.getElementById("eventSearchForm").mapZoom.value = map.getZoom();
		document.event.submit();
	  
	}

  /* END POST FUNCTIONS  ============================= */
  
