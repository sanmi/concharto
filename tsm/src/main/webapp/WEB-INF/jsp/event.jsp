<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tsm"%>

<tsm:page title="Event">
	<jsp:attribute name="head">
		<script
			src="http://maps.google.com/maps?file=api&amp;v=2.x&amp;key=ABQIAAAA1DZDDhaKApTfIDHGfvo13hSQekddw1ZVY1OywWYSY7GTmNOxgRQ1UKcA9cKipDAZNLJ5R_X-JJcYhw"
			type="text/javascript">
		</script>		
		<script type="text/javascript">
		//<![CDATA[
	function saveEvent() {
		document.getElementById("eventForm").lat.value = marker.getLatLng().lat();
		document.getElementById("eventForm").lng.value = marker.getLatLng().lng();
		document.getElementById("eventForm").zoomLevel.value = map.getZoom();
		document.event.submit();
	}			

	/*
		Called by the main initialize function
	 */
	function initialize() {
			initializeMap();
			var editLocation = false;
			if (document.getElementById("eventForm").lat.value != "") {
				editLocation = true;
			}
			if (editLocation) {
				var zoomStr = document.getElementById("eventForm").zoomLevel.value;
				var zoom;
				if (zoomStr == '') {
					zoom = 11;
				} else {
					zoom = parseInt (zoomStr);
				}
				map.setCenter(new GLatLng(
					document.getElementById("eventForm").lat.value,
					document.getElementById("eventForm").lng.value), zoom); 
			} else {
				//TODO UI WORK: don't have a default starting point
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
		}
	
	// addAddressToMap() is called when the geocoder returns an
	// answer.  
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
			
	function changeHistory() {
		var id = document.getElementById("eventForm").id.value;
		if (id != '') {
			document.location="changehistory.htm?id=" + id;
		}
	}
			
		//]]>
		</script>
	</jsp:attribute>
	<jsp:attribute name="script">map.js</jsp:attribute>
	<jsp:attribute name="bodyattr">onload="initialize()" onunload="GUnload();" class="mapedit"</jsp:attribute>

	<jsp:body>
	   <div id="sidebar">

       <form:form name="event" id="eventForm" commandName="event" method="post" onsubmit="saveEvent(); return false">
			   <form:hidden path="id"/>
			   <form:hidden path="lat"/>
			   <form:hidden path="lng"/>
			   <form:hidden path="zoomLevel"/>
				 <div style="margin-bottom:5px; margin-left:5px">
					 <span id="selectedMiniTab">Edit</span><a id="unselectedMiniTab" href="#" onclick="changeHistory(); return false;">Change History</a>
	       </div>
         <table>
           <tr>
             <td class="labelcell">Summary <br/>
             <form:input path="summary" size="45"/></td>
           </tr>
           <tr>
             <td class="labelcell">Where
                 <small>e.g., "gettysburg, pa" </small><br/>
                 <form:input path="where" size="45"/>
                 <br/>
                 <input  type="button" name="Find" value="Go to Location" onclick="showAddress(document.event.where.value); return false"/>             
                 <small id="tip"><b>Tip:</b> drag and drop the lollypop!</small>
             </td>
           </tr>
           <tr>
             <td class="labelcell">
               When
               <small>
                 e.g. "1962" or "March, 1064" or "1880 - 1886" <a href="#">hints</a>
               </small><br/>
               <form:input path="when" size="45"/>
             </td>
           </tr>
           <tr>
             <td class="labelcell">Description<br/>
             <form:textarea rows="5" cols="35" path="description"/></td>
           </tr>
           <tr>
             <td class="labelcell">Tags<br/>
             <form:input path="tags" size="45"/>
           </tr>
           <tr>
             <td class="labelcell">Source 
             <small><a id="selectedMiniTab" href="#">URL</a><a id="unselectedMiniTab" href="#">Publication</a><a id="unselectedMiniTab" href="#">Other</a></small><br/>
             <form:input path="source" size="45"/>
           </td> 
             
           </tr>
         </table>
         <input type="submit" name="Save" value="Save This Event" />
         <input type="button" name="Cancel" value="Cancel" onclick="javascript:document.location='switchboard/listEvents.htm';"/>
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

