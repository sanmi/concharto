<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="rx" uri="http://jakarta.apache.org/taglibs/regexp-1.0" %>
<%@ taglib prefix="display" uri="http://displaytag.sf.net" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tsm"%>
<%@ taglib prefix="wiki" uri="wikiRender" %>
<% pageContext.setAttribute("linefeed", "\n"); %>

<%-- The page title is based on the search terms --%>	
<tsm:page title="${title}">
	<jsp:attribute name="head">
		<jsp:include page="../include/mapkey.js.jsp"/>
		<%-- localized messages for javascript --%>
		<jsp:include page="include/messages.jsp"/>
	</jsp:attribute>
	<jsp:attribute name="stylesheet">map.css,wiki.css,header.css,search.css</jsp:attribute>
	<jsp:attribute name="script">prototype.js,map.js,control.modal.js,help.js,searchcommon.js,eventsearch.js</jsp:attribute>
	<jsp:attribute name="bodyattr">onload="init()" onunload="GUnload();" class="mapedit" id="searchpage" onresize="adjustSidebarIE();"</jsp:attribute>
	<jsp:attribute name="nohead">true</jsp:attribute>

	<jsp:body>
		<form:form name="event" id="eventSearchForm" commandName="eventSearch" action="${basePath}search/eventsearch.htm" onsubmit="search(); return false">
			<form:hidden path="boundingBoxSW" htmlEscape="true"/>
			<form:hidden path="boundingBoxNE" htmlEscape="true"/>
			<form:hidden path="mapCenter" htmlEscape="true"/>
			<form:hidden path="mapCenterOverride" />
			<form:hidden path="searchResults" htmlEscape="true"/>
			<form:hidden path="mapZoom"/>
			<form:hidden path="zoomOverride"/>
			<form:hidden path="mapType"/>
			<form:hidden path="isGeocodeSuccess"/>
			<form:hidden path="isAddEvent"/>
			<form:hidden path="editEventId"/>
			<form:hidden path="displayEventId"/>
			<form:hidden path="linkHereEventId"/>
			<form:hidden path="userTag" htmlEscape="true"/>
			<form:hidden path="embed"/>
			<%-- for javascript --%>
			<input type="hidden" id="basePath" value="${basePath}"/>
	  	
	  	<jsp:include page="include/searchbar.jsp">
	  		<jsp:param name="showSearchOptions" value="true"/>
	  		<jsp:param name="showAdminBar" value="true"/>
	  	</jsp:include>	
		  
			<%-- Pull the center from the form object so we can center using javascript (see above) --%>
		 
			<table class="mainarea"><tbody><tr>			
				<td id="sidebar">
				  <div id="sidebarSize"><a href="#" onclick="hideSidebar();">hide sidebar</a></div>
          <div id="results" >
	          <div class="resultcount">
	            <c:choose>
	              <c:when test="${fn:length(events) > 0}">
	                Displaying events <b>${currentRecord + 1}  - ${currentRecord + fn:length(events) }</b>  
	                of <b><c:out value="${totalResults}"/></b>
	              </c:when>
	              <c:otherwise>
	                No Events found
	              </c:otherwise>
	            </c:choose>
	            <c:if test="${eventSearchForm.asText != ''}">
	              for <b><c:out value="${eventSearchForm.asText}"/></b>
	            </c:if>
	          </div>    
		    		<form:errors path="where" cssClass="errorLabel" element="div"/>
						<form:errors path="when" cssClass="errorLabel" element="div"/>
						<form:errors path="when">
							<div class="errorHint">
								<jsp:include page="../include/timerangeexamples.jsp"/>
							</div>
		    		</form:errors>
						
			    	<c:set var="test" value="ABCDEFGHIJKLMNOPQRSTUVWXYZ"/>
						<!--  start -->
							<display:table id="event" 
												name="events" 
												requestURI="${requestURI}"
												pagesize="${pagesize}"
												partialList="true" 
												size="${totalResults}"
												excludedParams="*"						
												>
								<display:setProperty name="basic.msg.empty_list"> </display:setProperty>
								<display:setProperty name="paging.banner.placement">both</display:setProperty>
								<display:setProperty name="paging.banner.item_name"> </display:setProperty>
								<display:setProperty name="paging.banner.items_name"> </display:setProperty>
								<display:setProperty name="paging.banner.onepage"> </display:setProperty>
								<display:setProperty name="paging.banner.no_items_found"> </display:setProperty>
								<display:setProperty name="paging.banner.one_item_found"> </display:setProperty>
								<display:setProperty name="paging.banner.all_items_found"> </display:setProperty>
								<display:setProperty name="paging.banner.some_items_found"> </display:setProperty>

								<display:column autolink="true" class="iconcol">
									<a href="#"><img alt="marker" height='34' width='20' src="${basePath}images/icons/marker<c:out value='${fn:substring(test,event_rowNum-1,event_rowNum)}'/>.gif" onclick="openMarker(<c:out value='${event_rowNum-1}'/>)"/></a>
								</display:column>
								
								<display:column autolink="true">
									<div id="${event.id}" class="result wikitext">
			            	<div>					           
				          	<c:if test="${event.hasUnresolvedFlag}">
					          	<a class="errorLabel" href="${basePath}event/changehistory.htm?id=${event.id}">Flagged! </a>
				          	</c:if>
					          <a class="summary" href="#" onclick="openMarker(<c:out value='${event_rowNum-1}'/>)">
					          	<c:out value="${event.summary}"/></a></div>
					          <div class="when"><c:out value="${event.when.asText}"/></div>
					          <span class="where">
					          	<c:out value="${event.where}"/>
					          	<c:if test="${null != event.positionalAccuracy.name}">
						          	(Accuracy: <spring:message code="event.positionalAccuracy.${event.positionalAccuracy.id}"/>)
					          	</c:if> 
					          </span> 
					          <br/>
					           <%-- We want to keep any line breaks but escape all other html --%>
					          <c:set var="description" value="${fn:substring(event.description,0,300)}"/>
					          <c:choose>
						          <c:when test="${fn:length(event.description) > 300}">
							          <wiki:render wikiText="${description} '''. . .'''"/>
							          <a class="more" href="#" onclick="openMarker(<c:out value='${event_rowNum-1}'/>)">... more</a>
						          </c:when>
						          <c:otherwise>
							          <wiki:render wikiText="${description}"/>
						          </c:otherwise> 
					          </c:choose>
					          <c:if test="${fn:length(event.userTagsAsString) > 0}">
						          <div class="usertags">
						          	<b>Tags:</b>
						          	<c:choose>
							          	<c:when test="${fn:contains(realURI,'list')}">
							          		<c:set var="root" value="list/event.htm"/>
							          	</c:when>
							          	<c:otherwise>
							          		<c:set var="root" value="search/eventsearch.htm"/>
							          	</c:otherwise>
						          	</c:choose>
						          	<c:forEach items="${event.userTags}" var="userTag" varStatus="status">
                          <%-- THE following operation is necessary because javascript in IE6 doesn't interpret
                          single quote &#039; properly.  It needs an extra slash for single quotes = \#039; instead of #039;
                          so we replace 'qu'ran' with 'qu\'ran' before the final substitution --%>
						          	  <c:set var="tag" value="${fn:replace(userTag.tag,'\\'','\\\\\\'')}"/>
                          <%-- note the following needs to all be on one line for proper HTML format --%>
                          <a href="#" onclick="goToTag('<c:out value="${tag}"/>')"><c:out value="${userTag.tag}"/></a><c:if test="${status.index != (fn:length(event.userTags)-1)}">, </c:if>
						          	</c:forEach>
						          </div>
					          </c:if>
					          <c:if test="${fn:length(event.source) > 0}">
						          <div class="source">
						          	<b>Source:</b>
							          <c:set var="source" value="${fn:substring(event.source,0,300)}"/>
							          <wiki:render wikiText="${source}"/>
						          </div>
					          </c:if>
										<div class="linkbar">
						          <a class="links" href="#" onclick="editEvent(<c:out value='${event.id}'/>)" 
						             title="<spring:message code='searchresults.linktitle.edit'/>" >edit</a>
											<c:choose>
												<c:when test="${event.discussion == null}">
								         	<span class="new_links">
								          	<a href="${basePath}edit/discussedit.htm?id=${event.id}" 
								          	   title="<spring:message code='searchresults.linktitle.newdiscuss'/>" >discuss</a>
								          </span>
												</c:when>
												<c:otherwise>
													<a class="links" href="${basePath}event/discuss.htm?id=${event.id}" 
													   title="<spring:message code='searchresults.linktitle.discuss'/>" >discuss</a>													
												</c:otherwise>
											</c:choose>
						         
						          <a class="links" href="${basePath}event/changehistory.htm?id=${event.id}" 
						             title="<spring:message code='searchresults.linktitle.changes'/>" >changes</a>
						          <a class="links" href="${basePath}edit/flagevent.htm?id=${event.id}" 
						             title="<spring:message code='searchresults.linktitle.flag'/>" >flag</a>
						          
					          </div>
									</div>
								</display:column>
							</display:table>
															    
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

		<div id="linkhere" class="linkbox" style="visibility: hidden">
		    <div class='bd' style="margin:0; padding:0;">
		    	<div class="tl"><a href="#" onclick="Control.Modal.close(); return false;"><img alt="close" src="../images/12xclose.gif"></img></a></div>
		    	<div class="label">Paste this text to link back to this map</div>		    	
		      <input id="linkhere_url" type="text" class="textinput" onclick="selectThis(this);" />
		    	<div class="label">Paste this text to embed this map into a web page</div>
		      <input id="embedmap_html" type="text" class="textinput" onclick="selectThis(this);" />
		    </div>
		</div>		

	</jsp:body>
</tsm:page>
