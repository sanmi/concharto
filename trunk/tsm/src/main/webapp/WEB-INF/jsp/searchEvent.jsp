<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
		geocode(document.getElementById("searchEventForm").where.value);
	}			
	
	function geocode(address) {
			<%-- geocoder uses a callback mechanism don't submit until we get the results --%>
	    geocoder.getLatLng(address, saveAndSubmit);
	}

	function saveAndSubmit (latLng) {
		map.setCenter(latLng);
		var boundingBox = map.getBounds();
		document.getElementById("searchEventForm").boundingBoxSW.value = 
			gLatLngToJSON(boundingBox.getSouthWest());
		document.getElementById("searchEventForm").boundingBoxNE.value = 
			gLatLngToJSON(boundingBox.getNorthEast());
		document.getElementById("searchEventForm").mapCenter.value = 
			gLatLngToJSON(map.getCenter());
		document.getElementById("searchEventForm").mapZoom.value = map.getZoom();
		document.event.submit();
	}
	
	function gLatLngToJSON( point ) {
		return '{"lat":' + point.lat() + ',"lng":' + point.lng() + '}';
	}

	function initialize() {
		initializeMap();
		var mapCenterJSON = document.getElementById("searchEventForm").mapCenter.value;
		var mapZoom = parseInt(document.getElementById("searchEventForm").mapZoom.value);
		if (mapCenterJSON != "") {
			var mapCenter = mapCenterJSON.parseJSON();
			//recenter the map
			map.setCenter(new GLatLng(mapCenter.lat,mapCenter.lng), mapZoom);			
		}
	}
	

		//]]>
		</script>
	</jsp:attribute>
	<jsp:attribute name="script">map.js,json.js</jsp:attribute>
	<jsp:attribute name="bodyattr">onload="initialize()" onunload="GUnload();" class="mapedit"</jsp:attribute>


	<jsp:body>
	 <%-- Pull the center from the form object so we can center using javascript (see above) --%>
	  <div id="sidebar">
	    <form:form name="event" id="searchEventForm" commandName="searchEvent" method="post" onsubmit="search(); return false">
	    	<form:hidden path="boundingBoxSW" htmlEscape="true"/>
	    	<form:hidden path="boundingBoxNE" htmlEscape="true"/>
	    	<form:hidden path="mapCenter" htmlEscape="true"/>
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

