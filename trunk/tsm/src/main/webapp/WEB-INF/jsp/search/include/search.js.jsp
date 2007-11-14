
<script type="text/javascript">
//<![CDATA[

	<%-- global array and index for dealing with overlays --%>
	var _overlays = [];
	var _overlayIndex = 0;
	var _fitToPolygon = [];
	var _fitToPolygonIndex = 0;
	
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
  function overlayItem(overlay, html, type, id ) {
  	this.overlay = overlay;
  	this.html = html;
  	this.type = type;
  	this.id = id
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
		
		if (isFitViewToResults() == 'true') {
			<%-- fit map to the results --%>
			setIsFitViewToResults('false');
			var boundsPoly = new GPolyline(_fitToPolygon);
			var zoom = map.getBoundsZoomLevel(boundsPoly.getBounds());
			if (zoom > 13) {
				zoom = 13;
			}
			map.setZoom(zoom);
			map.setCenter(boundsPoly.getBounds().getCenter());
		}

		if (document.getElementById("eventSearchForm").isFirstView != null) {
			createModalWelcome();
			showWelcome();
		}		
	}

	function createModalWelcome() {
		var screenWidth = parseInt(getWidth() * .8);		
		var screenHeight = parseInt(getHeight() * .8);
    modal2 = new Control.Modal('modal_link',{
        opacity: 0.08,
        width: screenWidth,
        height: screenHeight
    });
	}
	
	<%-- Welcome message on the first view--%>
	function showWelcome() {
		modal2.open();
	}
	
	<%-- called by createOverlay --%>
	function createMarker(event) { 
		if (isFitViewToResults() == 'true') {
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
	  letteredIcon.image = "http://www.google.com/mapfiles/marker" + letter + ".png";
	
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

	<%-- create html for info bubbles --%>	
	function makeOverlayHtml(event) {
		var html = createInfoWindowHtml(event) +  
			'<br/><a class="links" href="#" onclick="editEvent(' + event.id + ')">edit</a> &nbsp;' +  
			'<a class="links" href="/edit/flagevent.htm?id=' + event.id + '">flag</a> &nbsp;' +
			'<a class="links" href="#" onclick="zoomTo(' + event.id + ')">zoom in</a> &nbsp;';
		if (event.hasUnresolvedFlags == 'true') {
			html += '<span class="errorLabel"><em>This event has been <a class="errorlinks" href="${basePath}edit/eventdetails.htm?id=' + event.id + '">flagged!</a></em></span>';
		} else {
			html += '<a class="links" href="${basePath}edit/eventdetails.htm?id=' + event.id + '">details</a>';
		}
		html += '<br/></div>';
		return html;
	}
	
	function simpleModal() {
			//Control.Modal.open('contents of modal');
			modal2.open();
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
	
	function adjustSidebarIE() {
		<%-- adjust the map --%>
		setMapExtent();
    	var top = document.getElementById("map").offsetTop;
    	var height = getHeight() - top - 44;
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
	
	function isFitViewToResults() {
		return document.getElementById("eventSearchForm").isFitViewToResults.value; 
	}
	
	function setIsFitViewToResults(value) {
		document.getElementById("eventSearchForm").isFitViewToResults.value = value;
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
			var where = document.getElementById("eventSearchForm").where.value;
			if (where != '') {
				map.setZoom(10); <%-- TODO infer this from the geocode results!! --%>
			}
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
  <%-- END POST FUNCTIONS  ============================= --%>
  
//]]>
</script>
