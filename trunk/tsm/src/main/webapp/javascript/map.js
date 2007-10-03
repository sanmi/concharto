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
			
			///check resize
			mapLeft = document.getElementById("sidebar").clientWidth + 40;
			document.getElementById("map").style.left=mapLeft + "px";
		
			map.setCenter(new GLatLng(40.879721,-76.998322),10);  //la la land, PA 
			if (window.attachEvent) { 
				setIEMapExtent(); //on initialize
				window.attachEvent("onresize",function(){ //IE
					setIEMapExtent();
				});
			} else {
	  		document.getElementById("map").style.top=document.getElementById("sidebar").offsetTop + "px";
				document.getElementById("map").style.right="20px";
				document.getElementById("map").style.bottom = "20px"
				// We need need to do set these on the div tag, otherwise there is a GMap loading bug that
				//causes only some of the map to be loaded, but then we need to reset them now that we are 
				//doing automatic resizing
				document.getElementById("map").style.width=""; 
				document.getElementById("map").style.height = ""
			}
			map.checkResize() //tell the map that we have resized it
		}
	}
	
	function setIEMapExtent() {
	  var top = document.getElementById("sidebar").offsetTop + 20;
		document.getElementById("map").style.top= top + "px";
		var hght=document.documentElement.clientHeight-top-20;
		document.getElementById("map").style.height=hght+"px";
		var width=document.documentElement.clientWidth-document.getElementById("sidebar").clientWidth-60;
		document.getElementById("map").style.width=width+"px";
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
				
			