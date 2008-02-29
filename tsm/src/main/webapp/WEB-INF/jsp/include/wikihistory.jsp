<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="display" uri="http://displaytag.sf.net" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<% pageContext.setAttribute("linefeed", "\n"); %>

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
								<a  href="${basePath}event/contributions.htm?user=${simpleTable.user}" >${simpleTable.user}</a> 
					 		<fmt:formatDate value="${simpleTable.dateCreated}" pattern="MMM dd, yyyy hh:mm a"/>
					 		
					 		<c:choose>
						 		<c:when test="${fn:length(simpleTable.auditEntryFieldChange) > 0}">
							 		<div class="cleanTable">
										<display:table
											name="${simpleTable.auditEntryFieldChange}" id="dt" >
											
											<display:column style="width:50%" title="Old Value">
												<c:set var="withCR" value="${fn:replace(dt.oldValue, linefeed, '<br/>')}" />
						     				<c:out value="${withCR}" escapeXml="no"/>
											</display:column>
											<display:column title="New Value">
												<c:set var="withCR" value="${fn:replace(dt.newValue, linefeed, '<br/>')}" />
						     				<c:out value="${withCR}" escapeXml="no"/>
											</display:column>
										</display:table> 
									</div>
									<c:if test="${dt.id > 0}">
										<a  href="${basePath}undopage.htm?id=${simpleTable.entityId}&toRev=${simpleTable.version-1}&page=${param.page}">
											Undo this revision
										</a>
									</c:if>
						 		</c:when>
					 		</c:choose>
				
					  </div>
						</display:column>
				 	</display:table>	        	
        </div>   
    	</div>
		</td></tr></table>