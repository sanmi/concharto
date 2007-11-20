<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="rx" uri="http://jakarta.apache.org/taglibs/regexp-1.0" %>
<%@ taglib prefix="display" uri="http://displaytag.sf.net" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tsm"%>
<% pageContext.setAttribute("linefeed", "\n"); %>

<tsm:page title="Event">
	<jsp:attribute name="head">
		<%-- we use includes so we can comment the javascript --%>
		<jsp:include page="../include/mapkey.js.jsp"/>
		<jsp:include page="include/searchcommon.js.jsp"/>
		<jsp:include page="include/eventsearch.js.jsp"/>
	</jsp:attribute>
	<jsp:attribute name="stylesheet">textcontent.css,map.css</jsp:attribute>
	<jsp:attribute name="script">prototype-1.7.0.js,map.js,json.js</jsp:attribute>
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
			<form:hidden path="isFitViewToResults"/>
	  	<jsp:include page="include/searchbar.jsp"/>
	  
		  
			<%-- Pull the center from the form object so we can center using javascript (see above) --%>
		
			<table><tbody><tr>			
				<td id="sidebar">
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
						<div class="resultcount">
							<c:choose>
			  				<c:when test="${fn:length(events) > 0}">
					  			Displaying events <b>${eventSearch.currentRecord + 1}  - ${eventSearch.currentRecord + fn:length(events) }</b>  
					  			of <b><c:out value="${totalResults}"/></b>
				 				</c:when>
			  				<c:otherwise>
				  				No Events found
			  				</c:otherwise>
			  			</c:choose>
				  			<c:set var="where" value="${(eventSearchForm.where != '') && (eventSearchForm.where != null)}"/>
				  			<c:set var="when" value="${(eventSearchForm.when != null)}"/>
				  			<c:set var="what" value="${(eventSearchForm.what != '')}"/>
			  			<c:if test="${where || when || what}">for</c:if>
			  			<c:if test="${where}">
				  			<b>${eventSearchForm.where}</b> 
			  			</c:if>
			  			<c:if test="${((where && when) || (where && what)) && when}">,</c:if>
			  			<c:if test="${when}">
				  			<b>${eventSearchForm.when.asText}</b> 
			  			</c:if>
			  			<c:if test="${(when || where) && what }">,</c:if>
			  			<c:if test="${what}">
				  			<b>${eventSearchForm.what}</b> 
			  			</c:if>							
						</div>
			    	<c:set var="test" value="ABCDEFGHIJKLMNOPQRSTUVWXYZ"/>
						<!--  start -->
							<display:table id="event" 
												name="events" 
												requestURI="${basePath}${requestURI}.htm"
												>
								<display:setProperty name="basic.msg.empty_list"> </display:setProperty>
								<display:setProperty name="paging.banner.placement"> </display:setProperty>
								<display:setProperty name="paging.banner.item_name"> </display:setProperty>
								<display:setProperty name="paging.banner.items_name"> </display:setProperty>
								<display:setProperty name="paging.banner.onepage"> </display:setProperty>
								<display:setProperty name="paging.banner.no_items_found"> </display:setProperty>
								<display:setProperty name="paging.banner.one_item_found"> </display:setProperty>
								<display:setProperty name="paging.banner.all_items_found"> </display:setProperty>
								<display:setProperty name="paging.banner.some_items_found"> </display:setProperty>
								<display:column autolink="true">
									<div class="result">
			            	<span class="letter">(<c:out value="${fn:substring(test,event_rowNum-1,event_rowNum)}"/>)</span>
					          <span class="when"><c:out value="${event.when.asText}"/></span>, 
					          <a class="summary" href="#" onclick="openMarker(<c:out value='${event_rowNum-1}'/>)"><c:out value="${event.summary}"/></a><br/>
					          <span class="where"><c:out value="${event.where}"/></span> <br/>
					           <%-- We want to keep any line breaks but escape all other html --%>
					          <c:set var="description" value="${fn:escapeXml(fn:substring(event.description,0,300))}"/>
					          <c:out value="${fn:replace(description, linefeed, '<br/>')}" escapeXml="false"/> 
					          <c:if test="${fn:length(event.description) > 300}">
					          	<a class="more" href="#" onclick="openMarker(<c:out value='${event_rowNum-1}'/>)"> ... more</a>
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
								</display:column>
							</display:table>
											
						<!-- end -->
						<%-- 
				    <c:forEach items="${events}" var="event" varStatus="status">
				    	<div class="result">
	            	<span class="letter">(<c:out value="${fn:substring(test,status.count-1,status.count)}"/>)</span>
			          <span class="when"><c:out value="${event.when.asText}"/></span>, 
			          <a class="summary" href="#" onclick="openMarker(<c:out value='${status.count-1}'/>)"><c:out value="${event.summary}"/></a><br/>
			          <span class="where"><c:out value="${event.where}"/></span> <br/>

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
				    --%>
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

	</jsp:body>
</tsm:page>

