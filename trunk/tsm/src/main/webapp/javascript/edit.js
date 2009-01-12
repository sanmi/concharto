/*
 * Copyright 2009 Time Space Map, LLC
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
	/* Create a base icon for all of our markers that specifies the
	     shadow, icon dimensions, etc. */
	var _markerIcon = new GIcon();
	var _editableMarkerIcon = new GIcon();
	var _editableMarker;
	var _editableMarkerHtml;
	var _editablePoly;
	var _currMarker = 0;
	var _clickListener;
	var _editablePolyClickListener;

	function initializeVars() {
		var basePath = $('basePath').value;
		_markerIcon.image = basePath+"images/icons/mm_20_red.png";
		_markerIcon.shadow = basePath+"images/icons/mm_20_shadow.png";
		_markerIcon.iconSize = new GSize(12, 20);
		_markerIcon.shadowSize = new GSize(22, 20);
		_markerIcon.iconAnchor = new GPoint(6, 20);
		_markerIcon.infoWindowAnchor = new GPoint(6, 2);
		_markerIcon.infoShadowAnchor = new GPoint(18, 18);
	
	  //NOTE: this is a work around for a draggable problem with the current google map api.
	  //when you try to substitute any draggable icon other than G_DEFAULT_ICON, AND there is 
	  //an info window, then the area over the icon for grabbing and dragging is truncated and 
	  //much too low.  Users would have a hard time knowing where to click  
    G_DEFAULT_ICON.image = basePath+"images/icons/marker-clk.png";
    G_DEFAULT_ICON.shadow = basePath+"images/icons/00shadow.png";
	}	
	
	function EditEventOverlayManager(parent) {
    this.parent = parent;

    this.initialize = function() {
      this.parent.initialize();
    }

	  /* construct the marker icon */
	  this.getMarkerIcon = function() {
	    return _markerIcon;
	  }
  } 
  EditEventOverlayManager.prototype = new EventOverlayManager();  //inherit with override
	
  /* BEGIN PRE FUNCTIONS (initialization) ============================= */
	/* the main initialize function */
	function initialize() {
		initializeVars();
		
		_mapManager.initializeMap();
      _mapManager.showDefault();
    
		_overlayManager = new EditEventOverlayManager(new EventOverlayManager);
		_overlayManager.initialize();

		var mapType = document.getElementById("eventForm").mapType.value;
		/* set map type from the event */			
		if (mapType != '') {
			map.setMapType(_mapManager.MAP_TYPES[mapType]);				
		}
		
		//The current search results
		var eventsJSON = document.getElementById("eventForm").searchResults.value;
		if (eventsJSON != '') {
			var excludeEventId = document.getElementById("eventForm").eventId.value;
			var events = eventsJSON.evalJSON();
			_overlayManager.createOverlays(events, excludeEventId);
		}
		
		//The editable overlay
		createEditableOverlay();

		/* this is to fix render time rearranging on IE and firefox*/		
		$('smaller').hide();
		$('smaller').removeClassName('hidden');
		$('larger').removeClassName('hidden');
		
		setupHelpPanels();
		
		/* if it is a polyline or a point, automatically fit it to the window 
				 no matter what, otherwise it can be confusing to the user.  For example, 
				 the centroid of the polyline may be nowhere near the border in which 
				 case you won't see the line at all.*/
		if (!isEmpty(_editablePoly) && _editablePoly.getVertexCount() >0) {
			_overlayManager.fitToPoly(_editablePoly, true);
		} 
		
	}
		
	/* create and draw the editable overlay */
	function createEditableOverlay() {
		var geometryType = getGeometryType();
		var geom = getEventFormGeom();
		var marker;
		if (isEmpty($('geometry').value)) {
			/* this means we are adding to the map.  Default is "point" */
			var center = getEventFormCenter();
			marker = createEditableMarker(new GLatLng(center.lat,center.lng));
			
		} else {
			if (geometryType == "point") {
				/* if we are switcing from one geometry type to the other, the geometry may not
				match.  TODO make this simpler */
				var point;
				if (geom.gtype == 'point') {
					point = new GLatLng(geom.lat, geom.lng);
				} else {
					/* we switched from a line or poly to a point - just use the map center */
					point = map.getCenter();
				}
				marker = createEditableMarker(point);
			} else if ((geometryType == "line") || (geometryType == "polygon")) {
				marker = createEditablePoly(geom);
			}
		}
	}
	
  /* used by listeners */
  function addVertex(point) {
     addMarker(point);
     drawPoly();
  }

	/* create an editable marker from a json point object */
	function createEditableMarker(point) {
		var marker = new GMarker(point, {draggable:true});  
		marker.enableDragging();
		marker.bindInfoWindowHtml(html);
		map.addOverlay(marker);
		GEvent.addListener(marker, "dragstart", function() {
			map.closeInfoWindow();
		});	
		map.setCenter(point, getZoom());
		var html; 
	  if (document.getElementById("eventForm").showPreview.value == 'true') {
			html = makePreviewHtml();
		} else {
			html = "<b>Drag me</b> <br/>anywhere on the map";
		}
		marker.openInfoWindow(html);
		_editableMarker = marker;
		_editableMarkerHtml = html;
	}
	
	function makePreviewHtml() {
	  	var event = getPreviewEvent();
			return _overlayManager.createInfoWindowHtml(null, event);
	}
	//--------------------------------
	/* create an editable poly from a json poly object */
	 function createEditablePoly(jsonLine) {
    var points = [];
    if (jsonLine != null) {
      var line = jsonLine.line;
      /* if we are adding, then line will be null */
      if (line) {
        for (var i=0; i<line.length; i++) {
           var vertex = new GLatLng(line[i].lat, line[i].lng);
           points.push(vertex);
        }
      }
    }
    createEditablePolyFromPoints(points);
  }

  /* create an editable poly from an array of points*/
  function createEditablePolyFromPoints(points) {
    if (points != null) {
      /* we create the editable poly */
      _editablePoly = _overlayManager.newPoly(points, getGeometryType());
      goToPoly(_editablePoly);
	    map.addOverlay(_editablePoly);
      _editablePoly.enableEditing();
	    //for deleting
	    _editablePolyClickListener = GEvent.addListener(_editablePoly, "click", function(point, index) {
	      if (typeof index == "number") {
	        _editablePoly.deleteVertex(index);
	      }
	    });
    }
    showPolyMessage(_editablePoly);
  }
  
  //--------------------------------
	
	function goToPoly(poly) {
		if (poly) {
			map.setCenter(poly.getBounds().getCenter(), getZoom());
			/* if the map is too zoomed in, we should zoom out to fit the line */
			_overlayManager.fitToPoly(poly);
		}
	}
	
	function showPolyMessage(poly) {
		var html;
		if (document.getElementById("eventForm").showPreview.value == 'true')  {
			html = makePreviewHtml();
		} else {
			html = " <b>Click anywhere</b> on the map to add a point<br/><b>Drag a point </b> to edit the line.<br/><b>Click a point</b> to delete it.";
		}
		if (null == poly) {
	    map.openInfoWindowHtml(map.getCenter(), html);
		} else {
			var point = _overlayManager.findClosestVertex(map.getCenter(), poly)
	    map.openInfoWindowHtml(point, html);
		}
	}
	
	/* END PRE FUNCTIONS (initialization) ============================= */

	/* BEGIN WHILE FUNCTIONS (initialization) ============================= */
	/* user is creating a point */
	function setupNewPoint() {
		removeEditableOverlay();
		createEditableOverlay();
	}
	
	/* user is creating a poly */
	function setupNewPoly() {
	  var points = [];
    removeEditableOverlay();
	  if (_editablePoly != null) {
		  for (var i=0; i<_editablePoly.getVertexCount(); i++) {
	      points.push(_editablePoly.getVertex(i));
	    }
      createEditablePolyFromPoints(points);
		} else {
		  //there was no polyline before so we are creating one
		  _editablePoly = _overlayManager.newPoly(points, getGeometryType());
		  map.addOverlay(_editablePoly);
		  _editablePoly.enableDrawing();
      var listener = GEvent.addListener(_editablePoly, "endline", function() {
        //NOTE: this is a kludge to get around a google maps bug in
        //version 2.111 where we can't just enable editing because otherwise
        //something later in the event change simply disables it again.
        setTimeout('_editablePoly.enableEditing()', 50)
      });
		}
	}
	
	/* remove point AND poly overlay */
	function removeEditableOverlay() {
		document.getElementById("eventForm").showPreview.value = 'false';
		if (_editableMarker) {
			map.removeOverlay(_editableMarker);
		} 		
		if (_editablePoly != null) {
      _editablePoly.disableEditing();
			map.removeOverlay(_editablePoly);
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
	/* END WHILE FUNCTIONS (initialization) ============================= */
	
  /* BEGIN MISC FUNCTIONS ============================= */
	function getEventFormGeom() {
			var geomJSON = document.getElementById("eventForm").geometry.value;
			if (geomJSON != '') {
				return geomJSON.evalJSON();			
			} else {
				return null;
			}
	}

	function getEventFormCenter() {
			var centerJSON = document.getElementById("eventForm").mapCenter.value;
			if (centerJSON != '') {
				return centerJSON.evalJSON();		
			} else {
				return null;
			}
	}
	
	function getPreviewEvent() {
			var eventJSON = document.getElementById("eventForm").previewEvent.value;
			if (eventJSON != '') {
				return eventJSON.evalJSON();			
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
		/* set zoom level from the event */
		var zoomStr = document.getElementById("eventForm").zoomLevel.value;
		var zoom;
		if (zoomStr == '') {
			zoom = 11;
		} else {
			zoom = parseInt (zoomStr);
		}
		return zoom;
	}
  /* END MISC FUNCTIONS ============================= */
  
  /* BEGIN POST FUNCTIONS ============================= */
	/* called on form submit */	
	function saveEvent() {
		document.getElementById("eventForm").showPreview.value = 'false';
		submitEvent();
	}
	
	function preview() {	  
		document.getElementById("eventForm").showPreview.value = 'true';
		submitEvent();
	}
	
	function submitEvent() {
		saveGeometry();
		document.getElementById("eventForm").zoomLevel.value = map.getZoom();
		document.getElementById("eventForm").mapType.value = _mapManager.getMapTypeIndex();
		document.getElementById("eventForm").mapCenter.value = _overlayManager.gLatLngToJSON(map.getCenter());
		document.event.submit();
	}
	
	
	/* saves either point or line or poly depending on edit mode */
	function saveGeometry() {
		var geometryType = getGeometryType();
		if (geometryType == "point") {
			document.getElementById("eventForm").geometry.value = _overlayManager.gLatLngToJSON(_editableMarker.getLatLng());
		} else if ((geometryType == "line") || (geometryType == "polygon")) {
			document.getElementById("eventForm").geometry.value = _overlayManager.polyToJSON(_editablePoly, geometryType);
		}
	}

	/* addAddressToMap() is called when the geocoder returns an answer.  */
	function addAddressToMap(response) {
	  if (!response || response.Status.code != 200) {
	    alert("Sorry, we can't find that location, " + response.Status.code);
	  } else {
	  	
	    place = response.Placemark[0];
	    point = new GLatLng(place.Point.coordinates[1],
	                        place.Point.coordinates[0]);
			var accuracy = place.AddressDetails.Accuracy;
			if (null != accuracy) {
				map.setZoom(_mapManager.ACCURACY_TO_ZOOM[place.AddressDetails.Accuracy]);
			} else {
				map.setZoom(_mapManager.ZOOM_BOX_THRESHOLD); 
			}
			map.setCenter(point);
			if (null != _editableMarker) {
				_editableMarker.setLatLng(point);
				_editableMarker.openInfoWindow(_editableMarkerHtml);
			}
	  }
	}
	
	/* showAddress() is called when you click on the Search button
	     in the form.  It geocodes the address entered into the form
	     and adds a marker to the map at that location. */
	function showAddress(address) {
	    geocoder.getLocations(address, addAddressToMap);
	}
	
	function changeHistory() {
		var id = document.getElementById("eventForm").eventId.value;
		if (id != '') {
			document.location="changehistory.htm?id=" + id;
		}
	}
	function discuss() {
		var id = document.getElementById("eventForm").eventId.value;
		if (id != '') {
			document.location="$/event/discuss.htm?id=" + id;
		}
	}
  /* END POST FUNCTIONS ============================= */
		
