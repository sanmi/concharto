	//some of the globals
	var map;
	var geocoder = null;
	
	function initializeMap() {
		if (GBrowserIsCompatible()) {
			// map and its equipment
			map = new GMap2(document.getElementById("map"));
			//map.enableContinuousZoom();
			map.addControl(new GMapTypeControl(1));
			map.addControl(new GLargeMapControl());
			map.enableDoubleClickZoom();
			map.enableScrollWheelZoom();
			geocoder = new GClientGeocoder();
	
			//prevent scrolling the window when the mouse is inside of the map
			GEvent.addDomListener(map.getContainer(), "DOMMouseScroll", wheelevent);
			map.getContainer().onmousewheel = wheelevent; 
			
			map.setCenter(new GLatLng(40.879721,-76.998322),10);  //la la land, PA 
			setMapExtent();
			map.checkResize() //tell the map that we have resized it
		}
	}

	function setMapExtent() {
    	var top = document.getElementById("map").offsetTop;
    	var height = getHeight() - top - 20;
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
	
	function createInfoWindowHtml(event) {
	   var html = '<div class="result" style="width:450px;margin-bottom:10px"><b>' + event.summary+'</b><br/>' + event.when + '<br/>' +
				event.description + '<br/>' +
				'<br/><b>Tags: </b>' + event.tags + '<br/>' + 
				'<b>Source: </b>' + event.source;
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
		return '{"lat":' + point.lat() + ',"lng":' + point.lng() + '}';
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
				
			