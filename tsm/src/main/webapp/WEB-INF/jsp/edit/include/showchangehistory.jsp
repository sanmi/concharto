<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ page import="com.tech4d.tsm.audit.EventFieldChangeFormatter, com.tech4d.tsm.model.audit.AuditEntry" %>
<%@ taglib prefix="display" uri="http://displaytag.sf.net" %>
<%
int geometryField = EventFieldChangeFormatter.TSGEOMETRY;
request.setAttribute("geometryField", geometryField);
request.setAttribute("ACTION_INSERT", AuditEntry.ACTION_INSERT);
%>
	
 	
	<display:table id="auditEntryTable" 
						name="auditEntries" 
						sort="list" 
						requestURI="${requestURI}"
						pagesize="${pagesize}"
						partialList="true" 
						size="${totalResults}"						
						>
		<display:setProperty name="basic.msg.empty_list">There have been no changes</display:setProperty>
		<display:setProperty name="paging.banner.item_name">Entry</display:setProperty>
		<display:setProperty name="paging.banner.items_name">Entries</display:setProperty>
		<display:setProperty name="paging.banner.onepage">&nbsp;</display:setProperty>
		<display:setProperty name="paging.banner.no_items_found">&nbsp;</display:setProperty>
		<display:setProperty name="paging.banner.one_item_found">&nbsp;</display:setProperty>
		<display:setProperty name="paging.banner.all_items_found">
			<span class="pagebanner">Change History <b>{0}</b> {1} found, displaying all {2}.</span>
		</display:setProperty>
		<display:setProperty name="paging.banner.some_items_found">
			<span class="pagebanner">Change History <b>{0}</b> {1} found, displaying <b>{2}</b> to <b>{3}</b>.</span>
		</display:setProperty>
		<display:column>
		
		<div class="changeEntry">
				Revision <c:out value="${auditEntryTable.version}"/>, 
				<c:out value="${actionLabels[auditEntryTable.action]}"/> <%-- TODO switch to messages here --%>
				<a class="links" href="#" onclick="alert('Not Implemented')">${auditEntryTable.user}</a> 
	 		<fmt:formatDate value="${auditEntryTable.dateCreated}" pattern="MMM dd, yyyy hh:mm a"/>
	 		
	 		<c:choose>
		 		<c:when test="${fn:length(auditEntryTable.auditEntryFieldChange) > 0}">
			 		<div class="simpleTable">
						<display:table name="${auditEntryTable.auditEntryFieldChange}" id="dt" >
							<display:column style="width:12em" title="Field">
								<spring:message code="audit.event.field.${dt.propertyName}"/>
							</display:column>
							<display:column style="width:355px" title="Old Value">
								<c:choose>
				   				<c:when test="${dt.propertyName == geometryField}">
										<iframe src="${basePath}search/auditmapthumbnail.htm?id=${dt.id}&change=oldValue"
						  						height="150" width="350" frameborder="0" scrolling="no">
										   This browser doesn't support embedding a map.
						 				</iframe>
				   				</c:when>
				   				<c:otherwise>
				     				<c:out value="${dt.oldValue}"/>
				   				</c:otherwise>
				   			</c:choose>
							</display:column>
							<display:column title="New Value">
								<c:choose>
				   				<c:when test="${dt.propertyName == geometryField}">
										<iframe src="${basePath}search/auditmapthumbnail.htm?id=${dt.id}&change=newValue"
						  						height="150" width="350" frameborder="0" scrolling="no">
										   This browser doesn't support embedding a map.
						 				</iframe>
				   				</c:when>
				   				<c:otherwise>
				     				<c:out value="${dt.newValue}"/>
				   				</c:otherwise>
				   			</c:choose>
							</display:column>
						</display:table> 
					</div>
					<c:if test="${dt.id > 0}">
						<a class="links" href="${basePath}edit/undoevent.htm?id=${auditEntryTable.entityId}&toRev=${auditEntryTable.version-1}">
							Undo this revision
						</a>
					</c:if>
		 		</c:when>
		 		<c:otherwise>
		 			<c:if test="${auditEntryTable.action != ACTION_INSERT}">
			 			<div><em>There were no changes</em></div>
		 			</c:if>
		 		</c:otherwise>
	 		</c:choose>

	  </div>
		</display:column>
 	</display:table>
	
