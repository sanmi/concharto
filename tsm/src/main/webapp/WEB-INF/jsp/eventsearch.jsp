<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tsm"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
request.setAttribute("basePath", basePath);
%>

<tsm:page title="Event">
	<jsp:attribute name="head">
		<script
			<%-- TODO: put the API key a properties file, probably best to use localization api <c:fmt> --%>
			src="http://maps.google.com/maps?file=api&amp;v=2.x&amp;key=ABQIAAAA1DZDDhaKApTfIDHGfvo13hSQekddw1ZVY1OywWYSY7GTmNOxgRQ1UKcA9cKipDAZNLJ5R_X-JJcYhw"
			type="text/javascript">
		</script>		
		<script type="text/javascript">
		//<![CDATA[
		
  var isAddToMap = false;
  
  function doSubmit() {  	
  	if (isAddToMap == true) {
			document.getElementById("eventSearchForm").isAddToMap.value = "true"; 
  		addToMap();
  	} else {
  		document.getElementById("eventSearchForm").isAddToMap.value = "false"; 
  		search();
  	}
  }

	function addToMap() {
		<%-- don't geocode, but do everything else.  --%>
		saveAndSubmit(map.getCenter());		
	}
	  
	function search() {
  	<%-- //Geocode before submitting so that we can get the map extent first!	 --%>
		geocode(document.getElementById("eventSearchForm").where.value);
	}			
	
	function geocode(address) {
			<%-- geocoder uses a callback mechanism don't submit until we get the results --%>
	    geocoder.getLatLng(address, saveAndSubmit);
	}

	function saveAndSubmit (latLng) {
		map.setCenter(latLng);
		var boundingBox = map.getBounds();
		document.getElementById("eventSearchForm").boundingBoxSW.value = 
			gLatLngToJSON(boundingBox.getSouthWest());
		document.getElementById("eventSearchForm").boundingBoxNE.value = 
			gLatLngToJSON(boundingBox.getNorthEast());
		document.getElementById("eventSearchForm").mapCenter.value = 
			gLatLngToJSON(map.getCenter());
		document.getElementById("eventSearchForm").mapZoom.value = map.getZoom();
		document.event.submit();
	}
	
	function gLatLngToJSON( point ) {
		return '{"lat":' + point.lat() + ',"lng":' + point.lng() + '}';
	}

	function initialize() {
		initializeMap();
		var mapCenterJSON = document.getElementById("eventSearchForm").mapCenter.value;
		var mapZoom = parseInt(document.getElementById("eventSearchForm").mapZoom.value);
		if (mapCenterJSON != "") {
			var mapCenter = mapCenterJSON.parseJSON();
			//recenter the map
			map.setCenter(new GLatLng(mapCenter.lat,mapCenter.lng), mapZoom);			
			drawPlacemarks();
		}
		adjustSidebarIE();
	}

	//global array and index for dealing with markers 
	var markers;
	var markerIndex = 0;
	var markerHtml;
	
	// Create a base icon for all of our markers that specifies the
	// shadow, icon dimensions, etc.
	var baseIcon = new GIcon();
	baseIcon.shadow = "http://www.google.com/mapfiles/shadow50.png";
	baseIcon.iconSize = new GSize(20, 34);
	baseIcon.shadowSize = new GSize(37, 34);
	baseIcon.iconAnchor = new GPoint(9, 34);
	baseIcon.infoWindowAnchor = new GPoint(9, 2);
	baseIcon.infoShadowAnchor = new GPoint(18, 25);
		
	function drawPlacemarks() {
			var eventsJSON = document.getElementById("eventSearchForm").searchResults.value;
			var events = eventsJSON.parseJSON();

			//make a new global array for storing the events
			markers = new Array(events.length);
			markerHtml = new Array(events.length);
			
			//create the markers
			for (var i =0; i<events.length; i++) {
			  map.addOverlay( createMarker(events[i]) );
			} 
	}

	function createMarker(event) {
			  // Create a lettered icon for this point using our icon class
			  var letter = String.fromCharCode("A".charCodeAt(0) + markerIndex);
			  var letteredIcon = new GIcon(baseIcon);
			  letteredIcon.image = "http://www.google.com/mapfiles/marker" + letter + ".png";
			
			  // Set up our GMarkerOptions object
			  markerOptions = { icon:letteredIcon };
				var marker = new GMarker(new GLatLng(event.latLng.lat, event.latLng.lng), markerOptions);

				var html = '<b>' + event.summary+'</b><br/>' + event.when + '<br/>' +
				event.description + '<br/>' +
				'<br/><b>Tags: </b>' + event.tags + '<br/>' + 
				'<b>Source: </b>' + event.source + '<br/>' + 
				'<a href="<c:out value="${basePath}"/>/event.htm?listid=' + event.id + '">edit</a>' +  
				' &nbsp; <a href="#">flag</a><br/>'
				;
				//save this marker.
				markers[markerIndex] = marker;
				markerHtml[markerIndex] = html;
				markerIndex++;

				marker.bindInfoWindowHtml(html);

				return marker;
	}
	
	function adjustSidebarIE() {
		var top = document.getElementById("sidebar").offsetTop;
		var hght=document.documentElement.clientHeight-top-40;
		
		document.getElementById("results").style.height=hght+"px";
	}

	function openMarker(index) {	
		markers[index].openInfoWindowHtml(markerHtml[index]);
	}

		//]]>
		</script>
	</jsp:attribute>
	<jsp:attribute name="script">map.js,json.js</jsp:attribute>
	<jsp:attribute name="bodyattr">onload="initialize()" onunload="GUnload();" class="mapedit" onresize="adjustSidebarIE();"</jsp:attribute>


	<jsp:body>
	  <div >
        	<span style="position:absolute; left:250px; top:34px;" >
				    <form:form name="event" id="eventSearchForm" commandName="eventSearch" onsubmit="doSubmit(); return false">
				    	<form:hidden path="boundingBoxSW" htmlEscape="true"/>
				    	<form:hidden path="boundingBoxNE" htmlEscape="true"/>
				    	<form:hidden path="mapCenter" htmlEscape="true"/>
				    	<form:hidden path="searchResults" htmlEscape="true"/>
				    	<form:hidden path="mapZoom"/>
				    	<form:hidden path="isAddToMap"/>
			        <table>
			          <tr>
			            <td class="labelcell">Where 
			                <small>e.g., "gettysburg, pa" </small><br/>
			                <form:input path="where" size="22"/>
			            </td>
			            <td class="labelcell">
			              When
			              <small>
			                e.g. "1962" or "March, 1064" or "1880 - 1886" 
			              </small><br/>
			              <form:input path="when" size="35"/>
			            </td>
			            <td class="labelcell">What<br/>
			                <form:input path="what" size="22"/>
				          </tr>
				        </table>
				        <input type="submit" name="Search" value="Search" />
				        &nbsp;&nbsp;&nbsp;<input type="submit" name="add" value="Add to the Map!" onclick="isAddToMap=true;"/>
					    </form:form>
	          </span>
	  </div>
	  
	 <%-- Pull the center from the form object so we can center using javascript (see above) --%>
	  <div id="sidebar" >
	  		<span style="padding-left: 5px"><b>${fn:length(events)} Events found</b></span>
		    <div id="results" style="margin-right:10px;width=320px;height:100px;overflow-x:hidden;overflow-y:scroll;overflow:-moz-scrollbars-vertical!important;">
		    	<c:set var="test" value="ABCDEFGHIJKLMNOPQRSTUVWXYZ"/>
		    	
			    <table class="eventlist" style="width: 100%">
				    <c:forEach items="${events}" var="event" varStatus="status">
			        <tr>
		            <td>
		            	<b>(<c:out value="${fn:substring(test,status.count-1,status.count)}"/>)</b>
				          <span style="color:#3670A7;font-weight:bold">${event.when.asText}</span>, 
				          <a href="#" onclick="openMarker(<c:out value='${status.count-1}'/>)">${event.summary}</a><br/>
				          <em>${event.where}</em>, <br/>
				          ${event.description} <br/>
				          <a href="<c:out value='${basePath}'/>/event.htm?listid=<c:out value='${event.id}'/>">edit</a>
				          &nbsp; <a href="#">flag</a><br/>
		            </td>
			        </tr>
				    </c:forEach>
			    </table>
			  </div>
	  </div>
	  

	   <div id="map"  style="padding:0;position:absolute; height:1000px;width:1000px">
	     <br/><br/>Map coming...
	     <noscript>
	       <p>
	         JavaScript must be enabled to get the map.
	       </p>
	     </noscript>
	   </div>
	 
	</jsp:body>
</tsm:page>
