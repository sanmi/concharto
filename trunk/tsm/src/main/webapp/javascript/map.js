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
				
			