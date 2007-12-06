<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tsm"%>


<tsm:page title="Event">
	<jsp:attribute name="head">
		<jsp:include page="../include/mapkey.js.jsp"/>
		<jsp:include page="include/edit.js.jsp"/>
	</jsp:attribute>
	<jsp:attribute name="stylesheet">map.css,header.css</jsp:attribute>
	<jsp:attribute name="script">prototype-1.7.0.js,map.js,json.js</jsp:attribute>
	<jsp:attribute name="bodyattr">onload="initialize()" onunload="GUnload();" class="mapedit" onresize="setMapExtent();"</jsp:attribute>

	<jsp:body>
		<table><tr>			
			<td id="sidebar">
        <form:form name="event" id="eventForm" commandName="event"  method="post" onsubmit="saveEvent(); return false">
					<form:hidden path="id"/>
					<form:hidden path="geometry" htmlEscape="true"/>
					<form:hidden path="zoomLevel"/>
					<form:hidden path="mapType"/>
					<form:hidden path="mapCenter" htmlEscape="true"/>
					<form:hidden path="searchResults" htmlEscape="true"/>
					
   		    <div class="miniTabBar">
   		    	<span class="miniTabSelected">Edit</span>
   		    	<a class="miniTabUnselected" href="#" onclick="changeHistory(); return false;">Change History</a>
	 		    </div>
   		    <div class="inputcell">
		        <span class="radio">
		        	<span class="inputlabel">Point:</span>
		        	<form:radiobutton path="geometryType" value="point" onclick="setupNewPoint()"/> 
		        </span>
		        <span class="radio">
		        	<span class="inputlabel">Line:</span>
	        		<form:radiobutton path="geometryType" value="line" onclick="setupNewPoly()"/> 
		        </span>
		        <span class="radio">
	        		<span class="inputlabel">Shape:</span>
	        		<form:radiobutton path="geometryType" value="polygon" onclick="setupNewPoly()"/> 
        		</span>
   		    </div>
   		    <div class="inputcell">
		        <span class="errorlabel"><form:errors path="summary" element="div"/></span>
		        <span class="inputlabel">Summary</span> 
		        <br/>
		        <form:input path="summary" size="45" maxlength="${event.SZ_SUMMARY}" htmlEscape="true"/>
   		    </div>
   		    <div class="inputcell">
		        <span class="errorlabel"><form:errors path="where" element="div"/></span>
   		    	<span class="inputlabel">Where</span>
	          <small>e.g., "gettysburg, pa" </small><br/>
	          <form:input path="where" size="45" maxlength="${event.SZ_WHERE}" htmlEscape="true"/>
	          <br/>
	          <input  type="button" name="Find" value="Go to Location" onclick="showAddress(document.event.where.value); return false"/>             
	          <small id="tip"><b>Tip:</b> drag and drop the lollypop!</small>
   		    </div>
   		    <div class="inputcell">
		        <span class="errorlabel"><form:errors path="when" element="div"/></span>
		        <span class="inputlabel">When</span> 
	          <small>
	            e.g. "1962" or "March, 1064" or "1880 - 1886" <a href="#">hints</a>
	          </small><br/>
		        <form:input path="when" size="45"/>
   		    </div>
   		    <div class="inputcell">
		        <span class="errorlabel"><form:errors path="description" element="div"/></span>
	   		    <span class="inputlabel">Description</span><br/>
						<form:textarea rows="5" cols="35" path="description"/>
   		    </div>
   		    <div class="inputcell">
		        <span class="errorlabel"><form:errors path="tags" element="div"/></span>
   		    	<span class="inputlabel">Tags</span><br/>
		        <form:input path="tags" size="45" maxlength="${event.SZ_TAGS}" htmlEscape="true"/>
  		    </div>
   		    <div class="inputcell">
		        <span class="errorlabel"><form:errors path="source" element="div"/></span>
   		    	<span class="inputlabel">Source</span>
			      <form:input path="source" size="45" maxlength="${event.SZ_SOURCE}" htmlEscape="true"/>
   		    </div>
					<div class="inputcell">
					 <input type="submit" name="Save" value="Save This Event" />
					 <input type="button" name="Cancel" value="Cancel" onclick="javascript:document.location='${basePath}search/eventsearch.htm';"/>
					</div>
	      </form:form>
			</td>		   
			<td>
				<div id="map">
					Map coming...
					<noscript>
					  <p>
					    JavaScript must be enabled to get the map.
					  </p>
					</noscript>
				</div>
			</td>
	 	</tr></table>
	
	</jsp:body>
</tsm:page>

