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
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="display" uri="http://displaytag.sf.net" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<% pageContext.setAttribute("linefeed", "\n"); %>
<script type="text/javascript">
<!--
<%-- for submitting a post method to undo (POST is not idempotent) --%>
  function undo(id, toRev, page) {
    document.getElementById('undoId').value = id;
    document.getElementById('undoToRev').value = toRev;
    document.getElementById('undoPage').value = page;
    document.undoForm.submit();
  }

//-->
</script>

  <form name="undoForm" id="undoForm" method="post" action="${basePath}undopage.htm">
       <input type="hidden" name="id" id="undoId" />
       <input type="hidden" name="toRev" id="undoToRev" />
       <input type="hidden" name="page" id="undoPage" />
  </form>

		<table><tr><td>
      <div class="simpleForm">
     		
				<div class="infoBox">
					<h2>Change History</h2>
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
						<div class="changeEntry">
						
								Revision <c:out value="${simpleTable.version}"/>, 
								<spring:message code="audit.action.field.${simpleTable.action}"/> by
								<jsp:include page="../include/userlinks.jsp">
									<jsp:param name="user" value="${simpleTable.user}"/>
								</jsp:include>
								
					 		<fmt:formatDate value="${simpleTable.dateCreated}" pattern="MMM dd, yyyy hh:mm a"/>
					 		
					 		<c:choose>
						 		<c:when test="${fn:length(simpleTable.auditEntryFieldChange) > 0}">
							 		<div class="cleanTable">
										<display:table
											name="${simpleTable.auditEntryFieldChange}" id="dt" >
											
											<display:column style="width:50%" title="Old Value">
                        <c:set var="lines" value="${fn:split(dt.oldValue, linefeed)}" />
                        <c:forEach items="${lines}" var="line">
                          <c:out value="${line}"/><br/>
                        </c:forEach> 
											</display:column>
											<display:column title="New Value">
                        <c:set var="lines" value="${fn:split(dt.newValue, linefeed)}" />
                        <c:forEach items="${lines}" var="line">
                          <c:out value="${line}"/><br/>
                        </c:forEach> 
											</display:column>
										</display:table> 
									</div>
									<c:if test="${dt.id > 0}">
									  <a href="#" onclick="undo(${simpleTable.entityId}, ${simpleTable.version-1}, '${param.page}');">Undo this revision</a>
									</c:if>
						 		</c:when>
					 		</c:choose>
				
					  </div>
						</display:column>
				 	</display:table>	        	
        </div>   
    	</div>
		</td></tr></table>
