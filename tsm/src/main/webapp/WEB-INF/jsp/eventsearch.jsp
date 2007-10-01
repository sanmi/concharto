<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tsm"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>

<tsm:page title="Event">
	<jsp:attribute name="head">
		<script
			<%-- TODO: put the API key a properties file, probably best to use localization api <c:fmt> --%>
			src="http://maps.google.com/maps?file=api&amp;v=2.x&amp;key=ABQIAAAA1DZDDhaKApTfIDHGfvo13hSQekddw1ZVY1OywWYSY7GTmNOxgRQ1UKcA9cKipDAZNLJ5R_X-JJcYhw"
			type="text/javascript">
		</script>		
		<script type="text/javascript">
		//<![CDATA[
		
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
	}

	function drawPlacemarks() {
			var eventsJSON = document.getElementById("eventSearchForm").searchResults.value;
			var events = eventsJSON.parseJSON();
			for (var i =0; i<events.length; i++) {
			  map.addOverlay( createMarker(events[i]) );
			} 
	}

	function createMarker(event) {
				var marker = new GMarker(new GLatLng(event.latLng.lat, event.latLng.lng));
				GEvent.addListener(marker, "click", function() {
					var html = '<b>' + event.summary+'</b><br/>' + event.when + '<br/>' +
					event.description + '<br/>' +
					'<br/><b>Tags: </b>' + event.tags + '<br/>' + 
					'<b>Source: </b>' + event.source + '<br/>' + 
					'<a href="http://www.map4d.com:8080/tsm/event.htm?listid=' + event.id + '">edit</a>' +  
					' &nbsp; <a href="#">flag</a>'
					;
						
					marker.openInfoWindowHtml(html);
				});
				return marker;
	}
	

		//]]>
		</script>
	</jsp:attribute>
	<jsp:attribute name="script">map.js,json.js</jsp:attribute>
	<jsp:attribute name="bodyattr">onload="initialize()" onunload="GUnload();" class="mapedit"</jsp:attribute>


	<jsp:body>
	 <%-- Pull the center from the form object so we can center using javascript (see above) --%>
	  <div id="sidebar">
	    <form:form name="event" id="eventSearchForm" commandName="eventSearch" method="post" onsubmit="search(); return false">
	    	<form:hidden path="boundingBoxSW" htmlEscape="true"/>
	    	<form:hidden path="boundingBoxNE" htmlEscape="true"/>
	    	<form:hidden path="mapCenter" htmlEscape="true"/>
	    	<form:hidden path="searchResults" htmlEscape="true"/>
	    	<form:hidden path="mapZoom"/>
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
        <input type="submit" name="Search" value="Search" />
        <input type="button" name="Save" value="Cancel" onclick="document.location='switchboard/listEvents.htm';"/>
	    </form:form>
	    <c:if test="${fn:length(events) > 0}">
		    <br/>
		    ${fn:length(events)} Events found
		    <table class="eventlist">
		    	<thead>
		    		<td>Summary</td>
		    		<td>Where</td>
		    		<td>When</td>
		    	</thead>
		    <c:forEach items="${events}" var="event">
		        <tr>
		            <td>${event.summary}&nbsp;</td>
		            <td>${event.where}&nbsp;</td>
		            <td>${event.when.asText}&nbsp;</td>
		        </tr>
		    </c:forEach>
		    </table>
	    </c:if>
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

