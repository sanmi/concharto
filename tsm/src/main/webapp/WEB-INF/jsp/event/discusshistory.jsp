<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="display" uri="http://displaytag.sf.net" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tsm"%>

<tsm:page title="Discussion History">
	<jsp:attribute name="stylesheet">map.css,simpleform.css,header.css</jsp:attribute>
	<jsp:attribute name="bodyattr">class="mapedit"</jsp:attribute>

<jsp:body>
		<%-- not sure why I need this -3px jog, can't get it to work from css file either --%>
		<table style="margin:-3px;padding:0"><tr><td>
			<div class="pagepanel">
				<div class="miniTabBar">
					<a class="miniTabUnselected" href="${basePath}edit/event.htm?id=${param.eventId}">Event</a>
 		    	<a class="miniTabSelected" href="${basePath}event/discuss.htm?id=${param.eventId}">Discussion</a>
					<span class="miniTabSelected miniTabLSpace" >Changes</span>
	      </div>
	      <div class="changeBar"></div>
	      <div class="simpleForm">
      		<div class="infoBox">
			  		<jsp:include page="include/showsummary.jsp"/>
			  	</div>
      		
					<div class="infoBox">
						<h2>Discussion History</h2>
						<display:table id="simpleTable" 
											name="auditEntries" 
											sort="list" 
											requestURI="${basePath}${requestURI}.htm"
											pagesize="${pagesize}"
											partialList="true" 
											size="${totalResults}"						
											>
							<display:setProperty name="basic.msg.empty_list">There have been no changes</display:setProperty>
							<display:setProperty name="paging.banner.placement">both</display:setProperty>
							<display:setProperty name="paging.banner.item_name">Change</display:setProperty>
							<display:setProperty name="paging.banner.items_name">Changes</display:setProperty>
							<display:setProperty name="paging.banner.onepage">&nbsp;</display:setProperty>
							<display:setProperty name="paging.banner.no_items_found">&nbsp;</display:setProperty>
							<display:setProperty name="paging.banner.one_item_found">&nbsp;</display:setProperty>
							<display:setProperty name="paging.banner.all_items_found">
								<span class="pagebanner"><b>{0}</b> {1} found.</span>
							</display:setProperty>
							<display:setProperty name="paging.banner.some_items_found">
								<span class="pagebanner"><b>{0}</b> {1} found.</span>
							</display:setProperty>
							<display:column>
							
							<%-- TODO make it so we don't show empty changes or we record something for empty changes (e.g. added a flag) --%>
							<div class="changeEntry">
							
									Revision <c:out value="${simpleTable.version}"/>, 
									<spring:message code="audit.action.field.${simpleTable.action}"/> by
									<a  href="${basePath}contributions.htm?user=${simpleTable.user}" >${simpleTable.user}</a> 
						 		<fmt:formatDate value="${simpleTable.dateCreated}" pattern="MMM dd, yyyy hh:mm a"/>
						 		
						 		<c:choose>
							 		<c:when test="${fn:length(simpleTable.auditEntryFieldChange) > 0}">
								 		<div class="cleanTable">
											<display:table name="${simpleTable.auditEntryFieldChange}" id="dt" >
												<display:column style="width:12em" title="Field">
													<spring:message code="audit.event.field.${dt.propertyName}"/>
												</display:column>
												<display:column style="width:355px" title="Old Value">
							     				<c:out value="${dt.oldValue}"/>
												</display:column>
												<display:column title="New Value">
							     				<c:out value="${dt.newValue}"/>
												</display:column>
											</display:table> 
										</div>
										<c:if test="${dt.id > 0}">
											<a  href="${basePath}edit/undodiscuss.htm?id=${simpleTable.entityId}&toRev=${simpleTable.version-1}&eventId=${param.eventId}">
												Undo this revision
											</a>
										</c:if>
							 		</c:when>
						 		</c:choose>
					
						  </div>
							</display:column>
					 	</display:table>	        	
	        </div>   
			  	<input type="button" value="Back to Search" onclick="javascript:document.location='${basePath}search/eventsearch.htm';" />
	    	</div>
	  	</div>  
		</td></tr></table>	  	
</jsp:body>
</tsm:page>

