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
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tsm"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ page import="org.tsm.concharto.audit.EventFieldChangeFormatter,org.tsm.concharto.model.audit.AuditEntry" %>
<%@ taglib prefix="display" uri="http://displaytag.sf.net" %>
<%
int geometryField = EventFieldChangeFormatter.TSGEOMETRY;
request.setAttribute("geometryField", geometryField);
request.setAttribute("ACTION_INSERT", AuditEntry.ACTION_INSERT);
%>

<tsm:page title="Latest Changes">
	<jsp:attribute name="stylesheet">textpanel.css,contributions.css,header.css</jsp:attribute>
	<jsp:attribute name="bodyattr"></jsp:attribute>
	
	<jsp:body>

    <!--  EVENTS -->		
		<div class="textpanel">			
			<h2>Latest Changes</h2>
			
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
				<display:setProperty name="paging.banner.all_items_found">&nbsp;</display:setProperty>
				<display:setProperty name="paging.banner.some_items_found">&nbsp;</display:setProperty>
				<display:column >
					<fmt:formatDate value="${simpleTable.auditEntry.dateCreated}" pattern="MMM dd, yyyy hh:mm a z"/>,
					<jsp:include page="../include/userlinks.jsp">
						<jsp:param name="user" value="${simpleTable.auditEntry.user}"/>
					</jsp:include>
					,(<spring:message code="audit.action.field.${simpleTable.auditEntry.action}"/>
					r${simpleTable.auditEntry.version}) 						
					, 
					<c:if test="${simpleTable.auditable.summary != null}">  <%-- todo this is a kludge - fix it --%>
						(<a href="${basePath}edit/event.htm?id=${simpleTable.auditEntry.entityId}">edit</a> | 
						<a href="${basePath}event/changehistory.htm?id=${simpleTable.auditEntry.entityId}">changes</a>),
					</c:if>
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

    <!--  WIKITEXT -->
    <div class="textpanel">     
      <h2>Discussion Changes</h2>
      
      <display:table id="simpleTable" 
                name="wikiChanges" 
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
        <display:setProperty name="paging.banner.all_items_found">&nbsp;</display:setProperty>
        <display:setProperty name="paging.banner.some_items_found">&nbsp;</display:setProperty>
        <display:column >
          <fmt:formatDate value="${simpleTable.auditEntry.dateCreated}" pattern="MMM dd, yyyy hh:mm a z"/>,
          <jsp:include page="../include/userlinks.jsp">
            <jsp:param name="user" value="${simpleTable.auditEntry.user}"/>
          </jsp:include>
          ,(<spring:message code="audit.action.field.${simpleTable.auditEntry.action}"/>
          r${simpleTable.auditEntry.version}),             
          <%-- If the title has text it is a page, otherwise it is an event discussion --%>
          <c:choose>
            <c:when test="${not empty simpleTable.auditable.title}">
		          (<a href="${basePath}page.htm?page=${simpleTable.auditable.title}">${simpleTable.auditable.title}</a> |
		          <a href="${basePath}pagehistory.htm?id=${simpleTable.auditEntry.entityId}&page=${simpleTable.auditable.title}">changes</a>) : 
            </c:when>
            <c:otherwise>
              (<a href="${basePath}event/discuss.htm?discussionId=${simpleTable.auditEntry.entityId}">view</a>) :
            </c:otherwise>
          </c:choose>
            ${fn:substring(simpleTable.auditable.text,0,100)}
        </display:column>
      </display:table>    

    </div>      

	</jsp:body>
</tsm:page>
