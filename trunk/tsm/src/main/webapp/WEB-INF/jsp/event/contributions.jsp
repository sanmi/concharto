<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tsm"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ page import="com.tech4d.tsm.audit.EventFieldChangeFormatter, com.tech4d.tsm.model.audit.AuditEntry" %>
<%@ taglib prefix="display" uri="http://displaytag.sf.net" %>
<%
int geometryField = EventFieldChangeFormatter.TSGEOMETRY;
request.setAttribute("geometryField", geometryField);
request.setAttribute("ACTION_INSERT", AuditEntry.ACTION_INSERT);
%>

<tsm:page title="Member Contributions">
	<jsp:attribute name="stylesheet">textpanel.css,contributions.css,header.css</jsp:attribute>
	<jsp:attribute name="bodyattr"></jsp:attribute>
	
	<jsp:body>
		
		<div class="textpanel">			
			<h2>Member Contributions for ${param.user}</h2>
			
			<display:table id="simpleTable" 
								name="userChanges" 
								sort="list" 
								requestURI="${requestURI}"
								pagesize="${pagesize}"
								partialList="true" 
								size="${totalResults}"						
								>
				<display:setProperty name="basic.msg.empty_list">There have been no edits</display:setProperty>
				<display:setProperty name="paging.banner.placement">both</display:setProperty>
				<display:setProperty name="paging.banner.item_name">Edit</display:setProperty>
				<display:setProperty name="paging.banner.items_name">Edits</display:setProperty>
				<display:setProperty name="paging.banner.onepage">&nbsp;</display:setProperty>
				<display:setProperty name="paging.banner.no_items_found">&nbsp;</display:setProperty>
				<display:setProperty name="paging.banner.one_item_found">&nbsp;</display:setProperty>
				<display:setProperty name="paging.banner.all_items_found">
					<span class="pagebanner"><b>{0}</b> {1} found.</span>
				</display:setProperty>
				<display:setProperty name="paging.banner.some_items_found">
					<span class="pagebanner"><b>{0}</b> {1} found.</span>
				</display:setProperty>
				<display:column >
					<fmt:formatDate value="${simpleTable.auditEntry.dateCreated}" pattern="MMM dd, yyyy hh:mm a z"/>,
					<c:if test="${simpleTable.auditable.summary != null}">  <%-- todo this is a kludge - fix it --%>
						(<a href="${basePath}edit/event.htm?id=${simpleTable.auditEntry.entityId}">edit</a> | 
						<a href="${basePath}event/changehistory.htm?id=${simpleTable.auditEntry.entityId}">changes</a>),
					</c:if>
					(<spring:message code="audit.action.field.${simpleTable.auditEntry.action}"/>
					r${simpleTable.auditEntry.version}), 						
					<c:choose>
						<c:when test="${simpleTable.auditable.summary == null}">
							<em>event has been deleted</em> 
						</c:when>
						<c:otherwise>
							<a href='${basePath}search/eventsearch.htm?_id=${simpleTable.auditEntry.entityId}'>${simpleTable.auditable.summary}</a>
							${simpleTable.auditable.when.asText} &nbsp; <%-- IE6 hack --%>
							${simpleTable.auditable.where} &nbsp; <%-- IE6 hack --%>
						</c:otherwise>
					</c:choose> 
				</display:column>
		 	</display:table>		

		</div>	  	
	</jsp:body>
</tsm:page>


