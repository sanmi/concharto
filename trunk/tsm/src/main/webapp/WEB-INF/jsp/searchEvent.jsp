<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tsm"%>

<tsm:page title="Event">
	<jsp:attribute name="head">
		<script
			src="http://maps.google.com/maps?file=api&amp;v=2.x&amp;key=ABQIAAAA1DZDDhaKApTfIDHGfvo13hQHaMf-gMmgKgj1cacwLLvRJWUPcRTWzCG3PTSVLKG0PgyzHQthDg5BUw"
			type="text/javascript">
		</script>		
		<script type="text/javascript">
		//<![CDATA[
	function saveEvent() {
		document.getElementById("eventForm").lat.value = marker.getLatLng().lat();
		document.getElementById("eventForm").lng.value = marker.getLatLng().lng();
		document.event.submit();
	}			
	
	//some of the globals
	var map;
	var geocoder = null;
	var marker;
	
	function initialize() {
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
		
			map.setCenter(new GLatLng(40.879721,-76.998322),11);  //la la land, PA 
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
			/*
			var editLocation = false;
			if (document.getElementById("eventForm").lat.value != "") {
				editLocation = true;
			}
			if (editLocation) {
				map.setCenter(new GLatLng(
					document.getElementById("eventForm").lat.value,
					document.getElementById("eventForm").lng.value),11); 
			} else {
				map.setCenter(new GLatLng(40.879721,-76.998322),11);  //la la land, PA 
			}
			//Add a marker in the center of the map		
			var point = map.getCenter();
			marker = new GMarker(point, {draggable: true});
			map.addOverlay(marker);
			marker.enableDragging();
			if (!editLocation) {
				marker.openInfoWindowHtml("<b>Drag me</b> <br/>anywhere on the map");
			}
	
			GEvent.addListener(marker, "dragstart", function() {
				map.closeInfoWindow();
			});
		
			GEvent.addListener(marker, "click", function() {
				marker.openInfoWindowHtml(html);
			});
			*/
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
	    alert("Sorry, we can't find that location, " + response.Status.code);
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
				
			
		//]]>
		</script>
	</jsp:attribute>
	<jsp:attribute name="script">addevent.js</jsp:attribute>
	<jsp:attribute name="bodyattr">onload="initialize()" onunload="GUnload();" class="mapedit"</jsp:attribute>

	<jsp:body>
	   <div id="sidebar">
	   
	       <form:form name="event" id="searchEventForm" commandName="searchEvent" method="post">
	
	         <table>
	           <tr>
	             <td class="labelcell">Where
	                 <small>e.g., "gettysburg, pa" </small><br/>
	                 <form:input path="where" size="50"/>
	             </td>
	           </tr>
	           <tr>
	             <td class="labelcell">
	               When
	               <small>
	                 e.g. "1962" or "March, 1064" or "1880 - 1886" <a href="#">hints</a>
	               </small><br/>
	               <form:input path="when" size="50"/>
	             </td>
	           </tr>
	           <tr>
	             <td class="labelcell">What<br/>
	                 <form:input path="what" size="50"/>
	           </tr>
	         </table>
	         <input type="button" name="Search" value="Search" onclick="saveEvent(); return false"/>
	         <input type="button" name="Save" value="Cancel" onclick="javascript:document.location='switchboard/listEvents.htm';"/>
	       </form:form>
	   </div>

	   <div id="map"  style="position:absolute; height:1000px;width:1000px">
	     Map coming...
	     <noscript>
	       <p>
	         JavaScript must be enabled to get the map.
	       </p>
	     </noscript>
	   </div>
	 
	
	</jsp:body>
</tsm:page>

