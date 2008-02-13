<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ page import="java.util.Random" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tsm"%>

<%
	//for defeating browser caches of iframes
	Random rand = new Random();
	request.setAttribute("rand", rand.nextInt());
 %>
<tsm:page title="Home">
		<jsp:attribute name="head">
			<script type="text/javascript">
	//<![CDATA[

	function init() {
		setupHelpPanels();
	}
	
	function search() {
			document.getElementById("eventSearchForm").limitWithinMapBounds.value = 'false';
			document.event.submit();
	}
	
	function editEvent() {
		document.location='${basePath}edit/event.htm?add';
	}
	//]]>
	</script>
	</jsp:attribute>	
	<jsp:attribute name="stylesheet">main.css,header.css</jsp:attribute>
	<jsp:attribute name="bodyattr">onload="init()" id="home"</jsp:attribute>
	<jsp:attribute name="nohead">true</jsp:attribute>
	<jsp:attribute name="nohomemenu">true</jsp:attribute>
	<jsp:attribute name="script">prototype.js,control.modal.js,help.js</jsp:attribute>
	
	<jsp:body>

		<div id="heading" >
			<img  src="images/title-home.png" />	
		</div>
		
		<form:form name="event" id="eventSearchForm" action="search/eventsearch.htm" commandName="eventSearch" onsubmit="search(); return false">
			<form:hidden path="boundingBoxSW" htmlEscape="true"/>
			<form:hidden path="boundingBoxNE" htmlEscape="true"/>
			<form:hidden path="mapCenter" htmlEscape="true"/>
			<form:hidden path="mapCenterOverride"/>
			<form:hidden path="searchResults" htmlEscape="true"/>
			<form:hidden path="mapZoom"/>
			<form:hidden path="zoomOverride"/>
			<form:hidden path="mapType"/>
			<form:hidden path="isGeocodeSuccess"/>
			<form:hidden path="isAddEvent"/>
			<form:hidden path="editEventId"/>
			<form:hidden path="displayEventId"/>
			<form:hidden path="linkHereEventId"/>
			<form:hidden path="embed"/>
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
			  <div id="totals"> <a href='${basePath}search/eventsearch.htm?_what='><b>${totalEvents}</b> events and counting...</a> </div>
	  	</div>
	  </form:form>
	  
		<div id="main">
			<table>
		    <col id="leftbar"/>
		    <col id="rightbar"/>
		    <tr>
		    <td id="left" >
		      <h1>An Atlas of History and Happenings</h1>
		      <p>Time Space Map is an encyclopedic atlas of history and happenings that anyone can edit.  It is a geographic wiki. </p>
		      <table><tr>
		      	<td>
				      <ul>
								<li><a href="http://wiki.timespacemap.com/wiki/Tour">Take a tour</a></li>
								<li><a href="http://wiki.timespacemap.com/wiki/Guidelines">Policies and guidelines</a></li>
								<li><a href="http://wiki.timespacemap.com/wiki/About">About Time Space Map</a></li>
				      </ul>
		      	</td>
		      	<td>
				      <ul>
				      	<li><a href="${basePath}list/recent.htm">Recently added</a></li>
								<li><a href="${basePath}event/latestchanges.htm">Latest changes</a></li>
				      </ul>
		      	</td>
		      </tr></table>
		      <h1>Recently Added</h1>
		      <div class="recent" >
		      	<ul>
							<c:forEach items="${recentEvents}" var="event" varStatus="status">
		  					<li> 
			          	<c:if test="${event.hasUnresolvedFlag}">
				          	<a class="errorLabel" href="${basePath}event/changehistory.htm?id=${event.id}">Flagged! </a>
			          	</c:if>
			            <a  href='${basePath}search/eventsearch.htm?_id=${event.id}'><c:out value="${event.summary}" escapeXml="true"/></a> <br/>
			            ${event.when.asText} <br/>
			            <em><c:if test="${event.where != null && event.where != ''}"><c:out value="${event.where}" escapeXml="true"/></c:if></em>
			            <hr/>
			          </li>
		  				</c:forEach>
	  					<li>
	  						<em><a href="${basePath}list/recent.htm">More recently added ...</a></em>
	  					</li>	  					
						</ul>        
		      </div>
		      <div class="rightwidth"></div>
		    </td>
		    <td id="right" >
		    	<%-- this form is a hack to defeat the browser cache on home.htm.  We need the cache for 
		    	     properly handling back buttons to the home page (otherwise the spotlight iframe won't match the page
		    	     content), but we want to be able to click next and see a new spotlight.  This problem
		    	     is most obvious on MacOS Safari, and intermittently so on IE 6, Windows --%>
		    	<form name="nextForm" method="post" action="${basePath}home.htm"></form>
		    	
			    <div id="spotlightbox">
		        <div class="next"><a href="#" onclick="document.nextForm.submit();">next</a></div>
		        <p>${spotlightLabel}</p>
		        <div class="clearfloat"></div>
		        <small style="float:right;padding-right:18px">Click on red icons, lines or areas</a></small>
		        <small>View a <a href='${spotlightLink}'>larger map</a></small>
		        <div class="clearfloat"></div>
		        <div id="borderbox">
		          <iframe id="embeddedmap" 
		          	<%-- &nc means don't count this as a page hit in google analytics 
		          			 &r is to defeat certain types of browser iframe page caching --%>
		          	src='${spotlightEmbedLink}&r=${rand}&nc'
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

