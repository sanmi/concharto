<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tsm"%>

<tsm:page title="Home">
		<jsp:attribute name="head">
			<script type="text/javascript">
	//<![CDATA[
	function search() {
			document.getElementById("eventSearchForm").limitWithinMapBounds.value = 'false';
			document.event.submit();
	}
	
	function editEvent() {
		document.location='${basePath}edit/event.htm?addEvent=true';
	}
	//]]>
	</script>
	</jsp:attribute>	
	<jsp:attribute name="stylesheet">main.css,header.css</jsp:attribute>
	<jsp:attribute name="nohead">true</jsp:attribute>
	<jsp:attribute name="nohomemenu">true</jsp:attribute>
	<jsp:attribute name="bodyattr">id="home"</jsp:attribute>

	<jsp:body>

	<div id="heading" >
		<img  src="images/title-home.png" />	
	</div>
	
	<form:form name="event" id="eventSearchForm" action="search/eventsearch.htm" commandName="eventSearch" onsubmit="search(); return false">
			<form:hidden path="boundingBoxSW" htmlEscape="true"/>
			<form:hidden path="boundingBoxNE" htmlEscape="true"/>
			<form:hidden path="mapCenter" htmlEscape="true"/>
			<form:hidden path="searchResults" htmlEscape="true"/>
			<form:hidden path="mapZoom"/>
			<form:hidden path="mapType"/>
			<form:hidden path="isGeocodeSuccess"/>
			<form:hidden path="isAddEvent"/>
			<form:hidden path="editEventId"/>
			<form:hidden path="displayEventId"/>
	  	<c:if test="${isFirstView != null}">
				<input type="hidden" id="isFirstView" value="true"/>					
	  	</c:if>
	  	<div id="searchboxcontent">			  	  		
	  		<jsp:include page="search/include/searchbar.jsp">
	  			<jsp:param name="showSearchOptions" value="false"/>
	  			<jsp:param name="showAdminBar" value="false"/>
	  		</jsp:include>
				<div id="tagline">
			    An encyclopedic atlas of history and happenings that anyone can edit.<%-- TODO finish this <a href="#">anyone can edit.</a>--%></div>
			  <div id="totals"> <a href='${basePath}search/eventsearch.htm?_what=&_where=&_when='><b>${totalEvents}</b> events and counting...</a> </div>
	  	</div>
	  </form:form>
	  
		<div id="main">
			<table>
		    <col id="leftbar"/>
		    <col id="rightbar"/>
		    <tr>
		    <td id="left" >
		      <h1>An Atlas of History and Happenings</h1>
		      <p>Time Space Map is an encyclopedic, online atlas of history and happenings that anyone can edit; a geographic wiki. You
		      can search, add or edit, view or undo changes, discuss, watch, tag, or flag for removal.</p>
		      <ul>
						<li><a href="http://wiki.timespacemap.com/Tour">Take a tour</a></li>
						<li><a href="http://wiki.timespacemap.com/Guidelines">Policies and guidelines</a></li>
						<li><a href="http://wiki.timespacemap.com/About">About Time Space Map</a></li>
		      </ul>
		      <h1>Recently Added</h1>
		      <div class="recent" >
		      	<ul>
							<c:forEach items="${recentEvents}" var="event" varStatus="status">
		  					<li> 
			            <a  href='${basePath}search/eventsearch.htm?_id=${event.id}'>${event.summary}</a> <br/>
			            ${event.when.asText} <br/>
			            <em><c:if test="${event.where != null && event.where != ''}">${event.where}</c:if></em>
			            <hr/>
			          </li>
		  				</c:forEach>	  					
						</ul>        
		      </div>
		      <div class="rightwidth"></div>
		    </td>
		    <td id="right" >
			    <div id="spotlightbox">
		        <img src="images/spotlight.png" />
		        <p><a  href='${basePath}search/eventsearch.htm?_where=london, england&_what="kensington palace"&_zoom=17&_maptype=1'>
		        	Kensington Palace</a>
		        </p>    
		        <div id="borderbox">
		          <iframe id="embeddedmap" 
		          	src='${basePath}search/embeddedsearch.htm?_where=london, england&_what="kensington palace"&_zoom=17&_maptype=1'
		            height="330" width="450" frameborder="0" scrolling="no">
		           	This browser doesn't support embedding a map.
		          </iframe>
		        </div> 
		      </div>
		    </td>
		    </tr>
		  </table>
		</div>
	</jsp:body>
</tsm:page>

