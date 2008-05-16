<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tsm"%>

<tsm:page title="Embedded Map">
	<jsp:attribute name="stylesheet">map.css,wiki.css,search.css</jsp:attribute>
	<jsp:attribute name="script">prototype.js,map.js,searchcommon.js</jsp:attribute>
	<jsp:attribute name="bodyattr">onload="initialize_embedded()" onunload="GUnload();" class="mapedit" onresize="adjustSidebarIE();"</jsp:attribute>
	<jsp:attribute name="stripped">true</jsp:attribute>
	<jsp:attribute name="head">
		<jsp:include page="../include/mapkey.js.jsp" />
		<%-- localized messages for javascript --%>
		<jsp:include page="../search/include/messages.jsp" />
		<script type="text/javascript">
		//<![CDATA[
		<%-- the main initialize function --%>
		function initialize_embedded() {
			initialize(new GSmallMapControl());
	   	var top = document.getElementById("map").offsetTop;
	   	var headerHeight = 0;
		  if ($('embeddedHeader') != null) {
		     headerHeight = 23;
		  }
	   	var height = getHeight() - headerHeight; 
	   	document.getElementById("map").style.height=height+"px";		
		}
	
		<%-- override create html for info bubbles --%>	
		function makeOverlayHtml(event, totalEvents) {
			return createInfoWindowHtml(event, 350, 150);
		}
	
		<%-- override this function to do nothing --%>
		function adjustSidebarIE() {
		}
		
		//]]>
		</script>
		<style type="text/css">
			body {
				background-image: none;
			}
		</style>
	</jsp:attribute>

	<jsp:body>
		<form:form name="event" id="eventSearchForm" commandName="eventSearch"
			onsubmit="search(); return false">
			<form:hidden path="boundingBoxSW" htmlEscape="true" />
			<form:hidden path="boundingBoxNE" htmlEscape="true" />
			<form:hidden path="mapCenter" htmlEscape="true" />
			<form:hidden path="mapCenterOverride" />
			<form:hidden path="searchResults" htmlEscape="true" />
			<form:hidden path="mapZoom" />
			<form:hidden path="zoomOverride" />
			<form:hidden path="mapType" />
			<form:hidden path="isGeocodeSuccess" />
			<form:hidden path="isAddEvent" />
			<form:hidden path="editEventId" />
			<form:hidden path="displayEventId" />
			<form:hidden path="linkHereEventId" />
			<form:hidden path="embed" />
			<form:hidden path="limitWithinMapBounds" />
			<form:hidden path="excludeTimeRangeOverlaps" />
			<form:hidden path="where" />
			<form:hidden path="when" />
			<form:hidden path="what" />
			<%-- for javascript --%>
			<input type="hidden" id="basePath" value="${basePath}" />
			<%-- a branded header, but don't show it if we are on our own site (e.g. from home page) --%>
			  <c:if test="${param.h != 1}">
          <div id="embedded">
	        <div id="embeddedHeader">
	          <a class="embeddedTitleLinks" href="${basePath}search/eventsearch.htm?${pageContext.request.queryString}" target="_top">
	            <span class="embeddedTitleLogo"><img src="${basePath}images/concharto-logo-xsm.png" alt="concharto"/></span>
	            <span class="embeddedTitle">An atlas of history and happenings that anyone can edit</span>
	          </a>
	        </div>   
	        <div class="clearfloat"/>
			  </c:if>
				<div id="map">
				  <br />
		    	<br />Map coming...
				  <noscript>
				    <p>
				      JavaScript must be enabled to get the map.
				    </p>
				  </noscript>
				</div>
      <c:if test="${param.h != 1}">
      </div>
      </c:if>
		</form:form>	

	</jsp:body>
</tsm:page>

