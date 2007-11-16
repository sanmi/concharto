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
			var what = document.getElementById("eventSearchForm").what.value;
			var when = document.getElementById("eventSearchForm").when.value;
			var where = document.getElementById("eventSearchForm").where.value;
			document.location='${basePath}search/eventsearch.htm?_where='+where+'&_when='+when+'&_what='+what+'&_fit=true';
	}
	
	function editEvent() {
		document.location='${basePath}edit/event.htm';
	}
	//]]>
	</script>
	</jsp:attribute>	
	<jsp:attribute name="stylesheet">home.css,simpleform.css</jsp:attribute>

	<jsp:body>
	<form:form name="event" id="eventSearchForm" commandName="eventSearch" onsubmit="search(); return false">
			<form:hidden path="boundingBoxSW" htmlEscape="true"/>
			<form:hidden path="boundingBoxNE" htmlEscape="true"/>
			<form:hidden path="mapCenter" htmlEscape="true"/>
			<form:hidden path="searchResults" htmlEscape="true"/>
			<form:hidden path="mapZoom"/>
			<form:hidden path="mapType"/>
			<form:hidden path="isGeocodeSuccess"/>
			<form:hidden path="isEditEvent"/>
			<form:hidden path="eventId"/>
			<form:hidden path="currentRecord"/>
			<form:hidden path="pageCommand"/>
			<form:hidden path="isFitViewToResults"/>
	  	<c:if test="${isFirstView != null}">
				<input type="hidden" id="isFirstView" value="true"/>					
	  	</c:if>
	  	<jsp:include page="search/include/searchbar.jsp"/>
	  </form:form>
	  	
	  	<div id="home_page" >
	  		<table>
 				<col id="leftbar"/>
 				<col id="rightbar"/>
	  		<tr>
	  			<td id="leftbar" >
	  				<div class="infoBox">
						<span class="resultcount"></span>
							<p>
								There are <b>${totalEvents}</b> events total. 
							</p>
							<h1>What Happened Everywhere</h1>
							<p>
								<span class="subtag">								
								 An encyclopedic, online atlas of history and happenings that anyone can edit. 
								 A geographic wiki - you can 
								 <a class="links" href="info/tour.htm#search">search</a>, 
								 <a class="links" href="info/tour.htm#add">add or edit</a>,
								 <a class="links" href="info/tour.htm#changes">view or undo changes</a>,
								 <a class="links" href="info/tour.htm#discuss">discuss</a>,
								 <a class="links" href="info/tour.htm#flag">flag for removal</a>
								</span>
							</p>
							<ul>
								<li><a class="links" href="info/tour.htm">Take a tour</a></li>
								<li><a class="links" href="#" onclick="alert('Not Implemented')">Policies and guidelines</a></li>
								<li><a class="links" href="#" onclick="alert('Not Implemented')">Why are we doing this?</a></li>
							</ul>
						</div>
						<div class="infoBox">
		  				<h2>Recently Added</h2>
		  				<c:forEach items="${recentEvents}" var="event" varStatus="status">
		  					<div class="event">
		  						<a class='links' href='${basePath}search/eventsearch.htm?_what="${event.summary}"&_when=${event.when}&_where=${event.where}&_fit=true'>
		  							${event.summary}, ${event.when.asText}
		  							<c:if test="${event.where != null && event.where != ''}">, ${event.where}</c:if> 
		  						</a>
		  					</div>
		  				</c:forEach>
	  				</div>
						
	  			</td>
	  			
	  			<td>
	  				<div class="infoBox" >
							<h2>Destination Spotlight</h2>
							Events about <a class="links" href='${basePath}search/eventsearch.htm?_what="new york draft riots"&_fit=true'>Civil War, 1863 New York Draft Riots</a>
						</div>
						<div class="borderBox">
							<iframe src='${basePath}search/embeddedsearch.htm?_what="new york draft riots"&_fit=true'
										height="350" width="500" frameborder="0" scrolling="no">
						   This browser doesn't support embedding a map.
							</iframe>
						</div>
	  			</td>
	  		</tr>
	  		<tr>
	  			<td colspan="2">
	  				<div class="infoBox">
							<h2>Beta Testers</h2>
							<p>
								Thank you for participating in the very early stages of this project.  Our goal is to create an
								encyclopedic database of <em>everything that ever happened anywhere</em> by using the power of mass collaboration.  
								At present, the system is fairly basic - it allows you to create and edit events using a  
								unique combination of wiki and mapping capabilities.  Please <a class="links" href="mailto:betatest@timespacemap.com">let us know</a> what you think.  
							</p>
							<p>
								There are now <b>${totalEvents}</b> events in the database. You can help to create a better 
								<span class="timefont">time</span><span class="spacefont">space</span><span class="mapfont">map</span> by:
							</p>
							<ul>
								<li>Adding historical or current events</li>
								<li>Reporting problems</li>
								<li>Suggesting improvements</li>
								<li>Requesting features</li>
								<li>Tell your friends - ask them to be beta testers</li>
							</ul>
							<p>	
								During the beta program we will be rolling out lots of new features and what we build will 
								depend on the feedback we get from you.  Some of the possibilities include: 
							</p>	
							<ul>
								<li>Thumbnail pictures for events</li>
								<li>Arrows on lines</li>
								<li>Improved look and feel</li>
								<li>Advanced search</li>
								<li>Advanced polygon editing</li>
								<li>Change notifications </li>
								<li>Embed a <span class="timefont">time</span><span class="spacefont">space</span><span class="mapfont">map</span> in your page (e.g. a blog or facebook account)</li>
							</ul>
							<p>
								Type something into the search bar and see what happens!
							</p>
						</div>
					</td>
	  		</tr>	
	  		</table>
			</div>
	</jsp:body>
</tsm:page>

