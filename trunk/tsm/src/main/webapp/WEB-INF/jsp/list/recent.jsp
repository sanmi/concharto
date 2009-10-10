<%--
Copyright 2009 Time Space Map, LLC

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="display" uri="http://displaytag.sf.net" %>
<%@ taglib prefix="wiki" uri="wikiRender" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tsm"%>

<tsm:page title="Recent Events">
	<jsp:attribute name="stylesheet">textpanel.css,header.css,recent.css,map.css,wiki.css</jsp:attribute>
	<jsp:attribute name="script">prototype.js,effects.js,dragdrop.js,resizable.js,livepipe.js,window.js</jsp:attribute>
	<jsp:attribute name="bodyattr">onload="init()"</jsp:attribute>
	<jsp:attribute name="head">
		<script type="text/javascript">
		//<![CDATA[
			function init() {
		    $$('.link_map').each(function(link){
		        var modal = new Control.Modal(link, {
		        opacity: 0.2,
		        iframe: true, 
						className: 'helpbox',
		        width: 500,
		        height: 400
		        });
		    });
			}
		//]]>
		</script>
	</jsp:attribute>

	<jsp:body>
		<div class="textpanel">

			<h1>Recently Added</h1>
			
      <div class="recent" >
				<display:table id="event" 
									name="recentEvents" 
									sort="list" 
									requestURI="${requestURI}"
									pagesize="${pagesize}"
									partialList="true" 
									size="${totalResults}"			
									>
					<display:setProperty name="paging.banner.placement">both</display:setProperty>
					<display:setProperty name="paging.banner.item_name">Event</display:setProperty>
					<display:setProperty name="paging.banner.items_name">Events</display:setProperty>
					<display:setProperty name="paging.banner.all_items_found">
						<span class="pagebanner"><b>{0}</b> {1} found.</span>
					</display:setProperty>
					<display:setProperty name="paging.banner.some_items_found">
						<span class="pagebanner"><b>{0}</b> {1} found.</span>
					</display:setProperty>
											
					<display:column class="result">
            Added: <fmt:formatDate value="${event.created}" pattern="MMM dd, yyyy hh:mm a"/>
            <small><a href="${basePath}search/embeddedsearch.htm?_id=${event.id}&nc" class="link_map">(quick map)</a></small> 
            <br/>
          	<c:if test="${event.hasUnresolvedFlag}">
	          	<a class="errorLabel" href="${basePath}event/changehistory.htm?id=${event.id}">Flagged! </a>
          	</c:if>
            <a class="summary"  href='${basePath}list/event.htm?_id=${event.id}'><c:out value="${event.summary}" escapeXml="true"/></a> <br/>
            <div class="when"><c:out value="${event.when.asText}"/></div>
						<c:if test="${fn:length(event.where) > 0}">
	            <span class="where"><c:out value="${event.where}"/></span> <br/>
						</c:if>
						
						<c:if test="${fn:contains(roles, 'admin')}">
							<div class="infoBox">
								<div class="adminBox" style="width: 10em">ADMIN VIEW</div>
			          <wiki:render wikiText="${event.description}"/>
			          <c:if test="${fn:length(event.source) > 0}">
				          <div class="source">
				          	<b>Source:</b>
					          <c:set var="source" value="${fn:substring(event.source,0,300)}"/>
					          <wiki:render wikiText="${source}"/>
				          </div>
			          </c:if>
			          <c:if test="${fn:length(event.userTagsAsString) > 0}">
				          <div class="usertags">
				          	<b>Tags:</b>
					          ${fn:substring(event.userTagsAsString,0,300)}
				          </div>
			          </c:if>
								<div class="linkbar">
				          <a class="links" href="${basePath}edit/event.htm?id=${event.id}" />edit</a>
				          <a class="links" href="${basePath}event/discuss.htm?id=${event.id}" >discuss</a>
				          <a class="links" href="${basePath}event/changehistory.htm?id=${event.id}">changes</a>
				          <a class="links" href="${basePath}edit/flagevent.htm?id=${event.id}">flag</a>
			          </div>
			        </div>
	          </c:if>
	          <div class="clearfloat"></div>
					</display:column>
			 	</display:table>
			</div>			
      
		</div>	
	</jsp:body>
</tsm:page>
