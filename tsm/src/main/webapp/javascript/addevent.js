//some of the globals
var map;
var geocoder = null;
var marker;

function initialize() {
	if (GBrowserIsCompatible()) {
		// map and its equipment
		map = new GMap2(document.getElementById("map"));
//		map.enableContinuousZoom();
		map.setCenter(new GLatLng(40.879721,-76.998322),11);  //la la land, PA 
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

		//Add a marker in the center of the map		
		var point = map.getCenter();
		marker = new GMarker(point, {draggable: true});
		map.addOverlay(marker);
		marker.enableDragging();
		marker.openInfoWindowHtml("<b>Drag me</b> <br/>anywhere on the map");

		GEvent.addListener(marker, "dragstart", function() {
			map.closeInfoWindow();
		});
	
		GEvent.addListener(marker, "click", function() {
			marker.openInfoWindowHtml(html);
		});
		
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

// addAddressToMap() is called when the geocoder returns an
// answer.  It adds a marker to the map with an open info window
// showing the nicely formatted version of the address and the country code.
function addAddressToMap(response) {
  if (!response || response.Status.code != 200) {
    alert("Sorry, we were unable to geocode that address " + response.Status.code);
  } else {
    place = response.Placemark[0];
    point = new GLatLng(place.Point.coordinates[1],
                        place.Point.coordinates[0]);
			map.setCenter(point, 13);
			marker.setLatLng(point);
    marker.openInfoWindowHtml(place.address + '<br>' + '<br/><b>Drag me</b> anywhere on the map');

  }

}

// showAddress() is called when you click on the Search button
// in the form.  It geocodes the address entered into the form
// and adds a marker to the map at that location.
function showAddress(address) {
    geocoder.getLocations(address, addAddressToMap);
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



