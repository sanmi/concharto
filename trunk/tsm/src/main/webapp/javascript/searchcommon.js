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
/* 
	common javascript functions that may be shared among various views 
	(e.g. eventsearch, embeddedsearch) 
*/

	
  /* BEGIN PRE FUNCTIONS (initialization) ============================= */
  
  //Setup a special map manager for search
  function SearchEventOverlayManager(parent) {
    this.parent = parent;
    
    //initialize
    this.initialize = function(mapControl) {
	    this.adjustSidebar();
	    _mapManager.initializeMap(mapControl);
	    /* map center and map zoom */
	    
	    var zoomTxt = $('mapZoom').value;
	    var mapZoom;
	    if (!isEmpty(zoomTxt)) {
	      mapZoom = parseInt(zoomTxt);
	    }
	    /* set map type from the event */
	    var mapType = $('mapType').value;
	    if (mapType != '') {
	      map.setMapType(_mapManager.MAP_TYPES[mapType]);
	    }
	    
      this.parent.initialize();
	    
	    var mapCenter = this.getMapCenterFromJSON();
	    if (null != mapCenter) {
	      /* recenter the map */
	      map.setCenter(new GLatLng(mapCenter.lat,mapCenter.lng), mapZoom);     
	
	      var eventsJSON = $('searchResults').value;
	      if (eventsJSON != '') {
	        var events = eventsJSON.evalJSON();
	        _overlayManager.createOverlays(events);
	      }
	    }   
	    
	    this.adjustSidebar();
	    if ($('embed').value == 'true') {
	      /* always fit to results for embedded maps */
	      _overlayManager.fitToResults();
	    } else if (this.limitWithinMapBounds() == false)  {
	      if (!($('mapCenterOverride').value == 'true') || !($('zoomOverride').value == 'true')) {
	        _overlayManager.fitToResults();
	      } else {
	        if (($('mapCenterOverride').value == 'true') && ((0 != this.parent.getFitToPolygon().length))) {
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
	  }
  
	  /** override from base class */
	  this.limitWithinMapBounds = function() {
	    return document.getElementById("eventSearchForm").limitWithinMapBounds.checked ; 
	  }
  
	  /* Get the map center from the html form, return null if none was provided */
	  this.getMapCenterFromJSON = function() {
	    var mapCenterJSON = $('mapCenter').value;
	    if (mapCenterJSON != "") {
	      return mapCenterJSON.evalJSON();
	    } else {
	      return null;
	    }       
	  }

    /* Resize map to fit the current window */
	  this.adjustSidebar = function() {
	    /* adjust the map */
	    _mapManager.setMapExtent();
	    var top = document.getElementById("map").offsetTop;
	    var height = _mapManager.getHeight() - top - _currResultsNudge;
	    document.getElementById("results").style.height=height+"px";
	    /* DEBUG the following is a Kludge! for an IE 6 rendering problem argh!*/
	    document.getElementById("results").style.width = _currResultsWidth + "px"; 
	  }
	  

	  /* Add a subtle highlight to the sidebar item when */
	  this.highlightSidebarItem = function(overlayItem) {
	    //highlight our one if it exists (e.g. we are on the search page, not the embedded page
	    if ($(overlayItem.id.toString()) != null) {
	      //unhighlight others
	      $$('.highlight').each(function(item){
	       $(item).removeClassName('highlight');
	      });
	      $(overlayItem.id.toString()).addClassName('highlight');
	    }   
	  }

	  /* create html for info bubbles */  
	  this.makeOverlayHtml = function(index, event, totalEvents) {
	    var overlaysIndex = _overlayManager.getOverlaysIndex(event.id);
	    var html = _overlayManager.createInfoWindowHtml(index, event);  
	    html += 
	    '<div style="width:' + _overlayManager.INFO_WIDTH + 'px;"><span  style="float:right">';
	    if (overlaysIndex > 0) {
	      html +='<a href="" onclick="_overlayManager.showEvent(' + event.id + ', -1); return false;">&laquo; prev</a>&nbsp;&nbsp; '; 
	    } 
	    if (overlaysIndex < (totalEvents - 1)) {
	      html += '<a href="" onclick="_overlayManager.showEvent(' + event.id + ', 1); return false;">next &raquo;</a>';
	    }
	    html += '</span>';
	    html += '<div class="infolinkbar linkbar"><a class="links" href="#" onclick="editEvent(' + event.id + ')" title="' + _msg_edit + '">edit</a>';
	        
	    if (event.hasDiscuss) {
	      html += '<a class="links" href="'+ _basePath + 'event/discuss.htm?id=' + event.id + '" title="' + _msg_discuss + '">discuss</a>';
	    } else {
	      html += '<span class="new_links"><a href="'+ _basePath + 'edit/discussedit.htm?id=' + event.id + '" title="' + _msg_newdiscuss + '">discuss</a></span>';
	    }
	    html += '<a class="links" href="' + _basePath + 'event/changehistory.htm?id=' + event.id + '" title="' + _msg_changes + '">changes</a>';
	    html += '<a class="links" href="' + _basePath + 'edit/flagevent.htm?id=' + event.id + '" title="' + _msg_flag + '">flag</a>' +
	      '<a class="links" href="#" onclick="zoomTo(' + event.id + ')">zoom in</a>' +
	      '<br/>'+
	      '</div></div>';
	    return html;
	  }
	  
  }
  SearchEventOverlayManager.prototype = new EventOverlayManager();  //inherit with override

  /* Overlay manager for embedded maps - info bubbles are smaller and we don't worry about the sidebar 
   * TODO we could probably do a better job at decomposing / inheritance to avoid do-nothing sidebar 
   * methods */
  function EmbeddedSearchEventOverlayManager(parent) {
    this.parent = parent;
    this.highlightSidebarItem = function(overlayItem) {/* do nothing */};
    this.adjustSidebar = function() {/* do nothing */};
    
    //override, only use the superclass method 
    this.makeOverlayHtml = function(index, event, totalEvents) {
      return this.parent.makeOverlayHtml(index, event, totalEvents);
    }
    
  } 
  EmbeddedSearchEventOverlayManager.prototype = new SearchEventOverlayManager();  //inherit with override


  /* END PRE FUNCTIONS (initialization) ============================= */

  /* BEGIN WHILE FUNCTIONS  ============================= */
	function zoomTo(id) {
		var index;
		for (var i=0; i<_overlayManager.getOverlays().length; i++) {
			if (_overlayManager.getOverlays()[i].id == id) {
				index = i;
			}
		}
		overlay = _overlayManager.getOverlays()[index].overlay;
		var point;
		if (_overlayManager.getOverlays()[index].type == "point")	{
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
		document.getElementById("eventSearchForm").mapType.value = _mapManager.getMapTypeIndex();
		if (eventId == '') {
			document.getElementById("eventSearchForm").isAddEvent.value = 'true';
		}		
		/* don't geocode, but do everything else.  */
		document.getElementById("eventSearchForm").mapCenter.value = _overlayManager.gLatLngToJSON(map.getCenter());
		saveAndSubmit();		
	}

	function setLimitWithinMapBounds(value) {
		document.getElementById("eventSearchForm").limitWithinMapBounds.value = value;
	}

	/* user has clicked on 'search' */
	function search() {
		/* set the map center, in case we aren't geocoding or the geocode 
		     doesn't succeed */
	 	document.getElementById("eventSearchForm").mapCenter.value = _overlayManager.gLatLngToJSON(map.getCenter());
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
	 		document.getElementById("eventSearchForm").mapCenter.value = _overlayManager.gLatLngToJSON(map.getCenter());
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
				map.setZoom(_mapManager.ACCURACY_TO_ZOOM[accuracy]); /* TODO infer this from the geocode results!! */
			}
			document.getElementById("eventSearchForm").mapCenter.value = _overlayManager.gLatLngToJSON(latLng);
		}
		saveAndSubmit();
	}
	
	/* callback user to submit form as soon as the geocode is complete */
	function saveAndSubmit() {
		var boundingBox = map.getBounds();
		document.getElementById("eventSearchForm").boundingBoxSW.value = 
			_overlayManager.gLatLngToJSON(boundingBox.getSouthWest());
		document.getElementById("eventSearchForm").boundingBoxNE.value = 
			_overlayManager.gLatLngToJSON(boundingBox.getNorthEast());
		/* if the geocode failed, we will get a null.  Pass this back to the controller to indicate
		     that the geocode failed */

		document.getElementById("eventSearchForm").mapType.value = _mapManager.getMapTypeIndex();
		document.getElementById("eventSearchForm").mapZoom.value = map.getZoom();
		document.event.submit();
	}

  /* END POST FUNCTIONS  ============================= */
  
