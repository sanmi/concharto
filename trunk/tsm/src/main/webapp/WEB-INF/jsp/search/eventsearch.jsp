<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tsm"%>

<tsm:page title="Event">
	<jsp:attribute name="head">
		<%-- we use includes so we can comment the javascript --%>
		<jsp:include page="../include/mapkey.js.jsp"/>
		<jsp:include page="include/search.js.jsp"/>
	</jsp:attribute>
	<jsp:attribute name="script">prototype-1.7.0.js,control.modal.2.2.3.js,map.js,json.js</jsp:attribute>
	<jsp:attribute name="bodyattr">onload="initialize()" onunload="GUnload();" class="mapedit" onresize="adjustSidebarIE();"</jsp:attribute>

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
	  	<c:if test="${isFirstView != null}">
				<input type="hidden" id="isFirstView" value="true"/>					
	  	</c:if>
	  	<div>
	     	<span style="position:absolute; left:250px; top:30px;" >
	        <table class="searchbar">
	          <tr>
	            <td class="labelcell">
				    		<form:errors path="where"><span class="errorLabel"></form:errors>
		  	          Where 
				    		<form:errors path="where"></span></form:errors>
                <small>e.g., "Gettysburg, PA" </small><br/>
                <form:input path="where" size="22"/>
	            </td>
	            <td class="labelcell">
	              <form:errors path="when"><span class="errorLabel"></form:errors>
	              	When
	              <form:errors path="when"></span></form:errors>
	              <small>
	                e.g. "1962", "Oct 14, 1066", "1880-1886" 
	              </small><br/>
	              <form:input path="when" size="35"/>
	            </td>
	            <td class="labelcell">
	            	What
	              <small>
	                e.g. "Battle" 
	              </small><br/>
	                <form:input path="what" size="22"/>
	            </td>
	          </tr>
	        </table>
	        
	        <span class="action"><input type="submit" name="Search" value="Search" /></span>
	        <span class="action"><input type="button" name="add" value="Add to the Map!" onclick="editEvent('')"/></span>
	        <c:if test="${fn:contains(roles, 'admin')}">
		        <span style="padding-top:.5em" class="adminBox">
		        	ADMIN:
	        		<span class="adminField">
	        			<form:radiobutton value="normal" path="show"/>
	        			Normal
	        		</span>
	        		<span class="adminAction">
	        			<form:radiobutton value="hidden" path="show"/>
	        			Removed
	        		</span>
	        		<span class="adminAction">
	        			<form:radiobutton value="flagged" path="show"/>
	        			Flagged
	        		</span>
        		</span>
	        </c:if>
	      </span>
		  </div>
	  
		  
			<%-- Pull the center from the form object so we can center using javascript (see above) --%>
		
			<table><tbody><tr>			
				<td id="sidebar">
		  		<span class="resultcount">		  			
				  	<c:if test="${isFirstView != null}">
			  			<%-- this is a hidden link --%>
		  				<a href="#modal_contents" id="modal_link"></a>
		  			</c:if>
		  			<c:choose>
		  				<c:when test="${fn:length(events) > 0}">
				  			Displaying <b>${eventSearch.currentRecord} - ${eventSearch.currentRecord + fn:length(events)}</b> Events 
				  			<c:if test="${totalResults > fn:length(events)}"> 
				  				of <b><c:out value="${totalResults}"/></b>
				  			</c:if>
			 				</c:when>
		  				<c:otherwise>
			  				No Events found
		  				</c:otherwise>
		  			</c:choose>
		  		</span>
		    	<div id="results" >
		    		
		    		<form:errors path="where" cssClass="errorLabel" element="div"/>
						<form:errors path="when" cssClass="errorLabel" element="div"/>
						<form:errors path="when">
							<div class="errorHint">
								Examples of valid time ranges are:
								<ul>
								<li>2000-2006</li>
								<li>Feb 1884 - Jan 1886</li>
								<li>Jine 22, 1992 12am - June 30, 1992</li>
								<li>1992</li>
								<li>October 1992</li>
								<li>Oct 12, 1992, 12:23:05 am</li>
								</ul>
							</div>
						</form:errors>
						
			    	<c:set var="test" value="ABCDEFGHIJKLMNOPQRSTUVWXYZ"/>
				    <c:forEach items="${events}" var="event" varStatus="status">
				    	<div class="result">
	            	<span class="letter">(<c:out value="${fn:substring(test,status.count-1,status.count)}"/>)</span>
			          <span class="when"><c:out value="${event.when.asText}"/></span>, 
			          <a class="summary" href="#" onclick="openMarker(<c:out value='${status.count-1}'/>)"><c:out value="${event.summary}"/></a><br/>
			          <span class="where"><c:out value="${event.where}"/></span>, <br/>
			          
			          <c:out value="${fn:substring(event.description,0,300)}"/> 
			          <c:if test="${fn:length(event.description) > 300}">
			          	<a class="more" href="#" onclick="openMarker(<c:out value='${status.count-1}'/>)"> ... more</a>
			          </c:if> 
								<br/>	
			          <a  class="links" href="#" onclick="editEvent(<c:out value='${event.id}'/>)">edit</a>
			          &nbsp; <a class="links" href="${basePath}edit/flagevent.htm?id=${event.id}" >flag</a>
			          &nbsp; 
			          <c:choose>
			          	<c:when test="${event.hasUnresolvedFlag}">
				          	<span class="errorLabel"><em>This event has been <a class="errorlinks" href="${basePath}edit/eventdetails.htm?id=${event.id}">flagged!</a></em></span>
			          	</c:when>
			          	<c:otherwise>
				          	<a class="links" href="${basePath}edit/eventdetails.htm?id=${event.id}">details</a>
			          	</c:otherwise>
			          </c:choose>
			          <br/>
			          
							</div>
				    </c:forEach>
				    <div class="nextPrev">
					    <c:if test="${eventSearch.currentRecord > 0}"> 
						    	<a class="nextPrev" href="#" onclick="nextPage('previous')">Previous</a>
					    </c:if>
					    <c:if test="${totalResults > eventSearch.currentRecord + fn:length(events)}"> 
						    	<a class="nextPrev" href="#" onclick="nextPage('next')">Next</a>
					    </c:if>
				    </div>
			  	</div>
			  </td>
			  <td>
					<div id="map">
					  <br/><br/>Map coming...
					  <noscript>
					    <p>
					      JavaScript must be enabled to get the map.
					    </p>
					  </noscript>
					</div>
				</td>
			</tr></tbody></table>
		</form:form>	

		<c:if test="${isFirstView != null}">
			<div id="modal_contents">
				<div class="nav">
					<ul><li><a href="#" onclick="Control.Modal.close(); return false;"><b>X</b> CLOSE</a></li></ul>
				</div> 
				<jsp:include page="../include/welcome.jsp"/>
				<p>
					<input type="button" value="Go!" onclick="Control.Modal.close(); return false;"/>
				</p>
			</div>
		</c:if>	  
	</jsp:body>
</tsm:page>

