	
	//some of the globals
	var map;
	var geocoder = null;
	
	function initializeMap(control) {
	
		if (GBrowserIsCompatible()) {
			// map and its equipment
			map = new GMap2(document.getElementById("map"));
			//map.enableContinuousZoom();
			map.addControl(new GMapTypeControl(1));
			if ((control == null) || (control == 'null')) {
				control = new GLargeMapControl();
			}
			map.addControl(control);
			
			map.enableDoubleClickZoom();
			map.enableScrollWheelZoom();
			geocoder = new GClientGeocoder();
	
			//prevent scrolling the window when the mouse is inside of the map
			GEvent.addDomListener(map.getContainer(), "DOMMouseScroll", wheelevent);
			map.getContainer().onmousewheel = wheelevent; 
			
			//map.setCenter(new GLatLng(40.879721,-76.998322),10);  //la la land, PA
			setMapExtent();
			map.checkResize(); //tell the map that we have resized it
			showDefault(); 
		}
	}
	function showDefault() {		
		diagonal = new GPolyline([
  		new GLatLng(86,-180),
  		new GLatLng(-86,180)
		]);
		var bounds = diagonal.getBounds();
		map.setCenter(bounds.getCenter());  
		map.setZoom(map.getBoundsZoomLevel(bounds));
	}
	
	function setMapExtent() {
    	var top = document.getElementById("map").offsetTop;
    	var height = getHeight() - top - 32;
    	document.getElementById("map").style.height=height+"px";
	}
	
	function getHeight() {
      var myWidth = 0, myHeight = 0;
      if( typeof( window.innerWidth ) == 'number' ) {
        //Non-IE
        myWidth = window.innerWidth;
        myHeight = window.innerHeight;
      } else if( document.documentElement && ( document.documentElement.clientWidth || document.documentElement.clientHeight ) ) {
        //IE 6+ in 'standards compliant mode'
        myWidth = document.documentElement.clientWidth;
        myHeight = document.documentElement.clientHeight;
      } else if( document.body && ( document.body.clientWidth || document.body.clientHeight ) ) {
        //IE 4 compatible
        myWidth = document.body.clientWidth;
        myHeight = document.body.clientHeight;
      }
      return myHeight;
    }
    
	function getWidth() {
      var myWidth = 0;
      if( typeof( window.innerWidth ) == 'number' ) {
        //Non-IE
        myWidth = window.innerWidth;
      } else if( document.documentElement && ( document.documentElement.clientWidth || document.documentElement.clientHeight ) ) {
        //IE 6+ in 'standards compliant mode'
        myWidth = document.documentElement.clientWidth;
      } else if( document.body && ( document.body.clientWidth || document.body.clientHeight ) ) {
        //IE 4 compatible
        myWidth = document.body.clientWidth;
      }
      return myWidth;
    }
	
	function createInfoWindowHtml(event, width /* optional */, height /* optional */) {
		if (width==null || width=='null' || width=='') { width = 450};
		if (height==null || height=='null' || height=='') {
			height = 'max-height:375px;';
		} else {
			height = 'height:' + height + 'px;'
		}
	  var html = '<div class="result inforesult wikitext" style="width:' + width +'px;'+ height +'margin-bottom:10px">' +
	  		'<b>' + event.summary.escapeHTML() +'</b><br/>' + 
	   		'<b>' + event.when + '</b><br/>' + 
				'<em>' + event.where.escapeHTML();
				if (event.accuracy != "") {
					 html += ' (Accuracy: ' + event.accuracy + ')'; 
				}
				html += '</em><br/>' + event.description  +
				'<b>Tags: </b>' + event.tags.escapeHTML() + '<br/>' + 
				'<b>Source: </b>';
				html += event.source + '</div>';
				
	   return html;
	}	
	
	function getMapTypeIndex() {
		var mapTypeIndex = 0;
		for (i=0; i<G_DEFAULT_MAP_TYPES.length; i++) {
			if (G_DEFAULT_MAP_TYPES[i].getName() == map.getCurrentMapType().getName()) {
				mapTypeIndex = i;
			}
		}
		return mapTypeIndex;
	}
		
  function gLatLngToJSON( point ) {
		return '{"gtype":"point","lat":' + point.lat() + ',"lng":' + point.lng() + '}';
	}
	
	function markersToJSON(markers, geometryType) {
		var str = '{"gtype":"' + geometryType + '","line":[';
		for (var i=0; i<markers.length; i++) {
			str += gLatLngToJSON( markers[i].getPoint());
			if (i != markers.length-1) {
				str += ',';
			}
		}
		str +=']}';
		return str;
	}
	
	///prevent page scroll
	function wheelevent(e)
	{
			if (!e){
				e = window.event
			}
			if (e.preventDefault){
				e.preventDefault()
			}
			e.returnValue = false;
	}
				
	function createOverlays(events, excludeEventId) {
		for (var i =0; i<events.length; i++) {
			var event = events[i];
			if ((excludeEventId === null) || (event.id != excludeEventId)) {
				if (event.gtype == 'point') {
				  createMarker(event);
				} else if ((event.gtype == 'line') || (event.gtype == 'polygon')) {
					createPoly(event);					
				}
			}
		} 
	}
	
	function fitToPoly(poly, force /* optional */) {
	  if (force == null) force = false;
		var bounds = poly.getBounds();
		var zoom = map.getBoundsZoomLevel(bounds);
		if ((zoom > map.getZoom()) || (force == true)) {
			map.setZoom(zoom);
		}
	}
	
	function newPoly(points, geometryType) {
		if (points.length > 0) {
			if ((geometryType == 'line')){
				return new GPolyline(points,'#FF0000', 4, .5, {geodesic:true});
			} else if (geometryType == 'polygon') {
				return new GPolygon(points,"#f33f00", 4, .8, '#FF0000', .25);
			}
		}
		return null;
	}
	
	function return2br(dataStr) {
		return dataStr.gsub('(\r\n|[\r\n])', '<br/>');
  }
  
  function autolink(dataStr) {
  	var linked = dataStr.gsub('((ftp|https?)://[^ ,;\t\n<]*)','<a href="#{1}" target="_top">#{1}</a>');
		linked = linked.gsub('.\</a\>','\</a\>.');  	
  	linked = linked.gsub('[.]" target','" target');  	
  	return linked;
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
		
  
  function isEmpty(value) {
		return ((null == value) || ('' == value));
	}
	
  