<%-- 
 ***** BEGIN LICENSE BLOCK *****
 Version: MPL 1.1
 
 The contents of this file are subject to the Mozilla Public License Version 
 1.1 (the "License"); you may not use this file except in compliance with 
 the License. You may obtain a copy of the License at 
 http://www.mozilla.org/MPL/
 
 Software distributed under the License is distributed on an "AS IS" basis,
 WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 for the specific language governing rights and limitations under the
 License.
 
 The Original Code is Concharto.
 
 The Initial Developer of the Original Code is
 Time Space Map, LLC
 Portions created by the Initial Developer are Copyright (C) 2007 - 2009
 the Initial Developer. All Rights Reserved.
 
 Contributor(s):
 Time Space Map, LLC
 
 ***** END LICENSE BLOCK *****
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ page import="com.tech4d.tsm.audit.EventFieldChangeFormatter, com.tech4d.tsm.model.audit.AuditEntry" %>
<%@ taglib prefix="display" uri="http://displaytag.sf.net" %>
<%
pageContext.setAttribute("linefeed", "\n"); //for displaying description and summary fields
request.setAttribute("geometryField", EventFieldChangeFormatter.TSGEOMETRY);
request.setAttribute("posAccuracyField", EventFieldChangeFormatter.POSITIONAL_ACCURACY);
request.setAttribute("discussField", EventFieldChangeFormatter.DISCUSSION);
request.setAttribute("descriptionField", EventFieldChangeFormatter.DESCRIPTION);
request.setAttribute("sourceField", EventFieldChangeFormatter.SOURCE);
request.setAttribute("flagsField", EventFieldChangeFormatter.FLAGS);
request.setAttribute("ACTION_INSERT", AuditEntry.ACTION_INSERT);
%>
<script type="text/javascript">
<!--
<%-- for submitting a post method to undo (POST is not idempotent) --%>
  function undo(id, toRev) {
    document.getElementById('undoId').value = id;
    document.getElementById('undoToRev').value = toRev;
    document.undoForm.submit();
  }

//-->
</script>

  <form name="undoForm" id="undoForm" method="post" action="${basePath}edit/undoevent.htm">
       <input type="hidden" name="id" id="undoId" />
       <input type="hidden" name="toRev" id="undoToRev" />
  </form>

	<display:table id="simpleTable" 
						name="auditEntries" 
						sort="list" 
						requestURI="${requestURI}"
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
		<%-- TODO fix cut and paste code --%>
		<div class="changeEntry">
		
			Revision <c:out value="${simpleTable.version}"/>, 
			<spring:message code="audit.action.field.${simpleTable.action}"/> by
			<jsp:include page="../../include/userlinks.jsp">
				<jsp:param name="user" value="${simpleTable.user}"/>
			</jsp:include>
	 		<fmt:formatDate value="${simpleTable.dateCreated}" pattern="MMM dd, yyyy hh:mm a"/>
	 		
	 		<c:choose>
		 		<c:when test="${fn:length(simpleTable.auditEntryFieldChange) > 0}">
			 		<div class="cleanTable">
						<display:table name="${simpleTable.auditEntryFieldChange}" id="dt" >
							<display:column style="width:12em" title="Field">
								<spring:message code="audit.event.field.${dt.propertyName}"/>
							</display:column>
							<display:column style="width:355px" title="Old Value">
								<c:choose>
				   				<c:when test="${dt.propertyName == geometryField}">
				          	<%-- &nc means don't count this as a page hit in google analytics --%>
										<iframe src="${basePath}search/auditmapthumbnail.htm?id=${dt.id}&change=oldValue&nc"
						  						height="150" width="350" frameborder="0" scrolling="no">
										   This browser doesn't support embedding a map.
						 				</iframe>
				   				</c:when>
									<%--  Description and Source are wikitext and need to show carriage returns--%>			   				
				   				<c:when test="${(dt.propertyName == descriptionField) || (dt.propertyName == sourceField)}">
										<c:set var="lines" value="${fn:split(dt.oldValue, linefeed)}" />
										<c:forEach items="${lines}" var="line">
											<c:out value="${line}" /><br/>												
										</c:forEach>
				   				</c:when>
				   				<c:when test="${dt.propertyName == posAccuracyField}">
				   					<spring:message code="event.positionalAccuracy.${dt.oldValue}"/>
				   				</c:when>
				   				<c:otherwise>
				     				<c:out value="${dt.oldValue}"/>
				   				</c:otherwise>
				   			</c:choose>
							</display:column>
							<display:column title="New Value">
								<c:choose>
				   				<c:when test="${dt.propertyName == geometryField}">
				          	<%-- &nc means don't count this as a page hit in google analytics --%>
										<iframe src="${basePath}search/auditmapthumbnail.htm?id=${dt.id}&change=newValue&nc"
						  						height="150" width="350" frameborder="0" scrolling="no">
										   This browser doesn't support embedding a map.
						 				</iframe>
				   				</c:when>
									<%--  Description and Source are wikitext and need to show carriage returns--%>			   				
				   				<c:when test="${(dt.propertyName == descriptionField) || (dt.propertyName == sourceField)}">
										<c:set var="lines" value="${fn:split(dt.newValue, linefeed)}" />
										<c:forEach items="${lines}" var="line">
											<c:out value="${line}" /><br/>												
										</c:forEach>
				   				</c:when>
				   				<c:when test="${dt.propertyName == posAccuracyField}">
				   					<spring:message code="event.positionalAccuracy.${dt.newValue}"/>
				   				</c:when>
				   				<c:when test="${dt.propertyName == discussField}">
				   					<spring:message code="audit.event.field.value.${dt.newValue}"/>
				   				</c:when>
				   				<c:otherwise>
				     				<c:out value="${dt.newValue}"/>
				   				</c:otherwise>
				   			</c:choose>
							</display:column>
						</display:table> 
					</div>
					<%-- Only a user can undo an event.  And we can't undo the first revision --%> 
          <%-- And we can't undo flags --%>
					<c:if test="${(dt.id > 0) && (fn:contains(roles, 'edit')) 
					              && (simpleTable.auditEntryFieldChange[0].propertyName != flagsField)}">
            <a href="#" onclick="undo(${simpleTable.entityId}, ${simpleTable.version-1});">Undo this revision</a>
            
					</c:if>
		 		</c:when>
	 		</c:choose>

	  </div>
		</display:column>
 	</display:table>
	
