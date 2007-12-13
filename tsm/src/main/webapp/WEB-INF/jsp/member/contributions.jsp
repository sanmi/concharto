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

<tsm:page title="Change History">
	<jsp:attribute name="stylesheet">changes.css,header.css</jsp:attribute>
	<jsp:attribute name="bodyattr"></jsp:attribute>
	
	<jsp:body>
		
		<div class="loginForm">			
			<h2>Member Contributions for <a  href="#" onclick="alert('Not Implemented')">${param.user}</a></h2>
			
			<display:table id="auditEntryTable" 
								name="userChanges" 
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
				<display:column title="Date" class="date">
					<fmt:formatDate value="${auditEntryTable.auditEntry.dateCreated}" pattern="MMM dd, yyyy hh:mm a"/>
				</display:column>
				<display:column title="Change" >
						<spring:message code="audit.action.field.${auditEntryTable.auditEntry.action}"/>						
				</display:column>
				<display:column title="Action" class="action">
					<a  href="${basePath}edit/event.htm?listid=${auditEntryTable.auditable.id}">edit</a> | 
					<a  href="${basePath}edit/eventdetails.htm?id=${auditEntryTable.auditable.id}">changes</a>
				</display:column>
				<display:column title="Summary" class="summary">
					<a  href='${basePath}search/eventsearch.htm?_id=${auditEntryTable.auditable.id}'>${auditEntryTable.auditable.summary}</a>
				</display:column>
				<display:column title="when" property="auditable.when.asText"/>
				<display:column title="where" property="auditable.where"/>	
		 	</display:table>		

		</div>	  	
	</jsp:body>
</tsm:page>
