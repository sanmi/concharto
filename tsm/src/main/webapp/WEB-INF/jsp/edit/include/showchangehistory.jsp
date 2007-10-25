<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ page import="com.tech4d.tsm.audit.EventFieldChangeFormatter" %>
<%@ taglib prefix="display" uri="http://displaytag.sf.net" %>
<%
int geometryField = EventFieldChangeFormatter.TSGEOMETRY;
request.setAttribute("geometryField", geometryField);
%>
	<div class="changeEntry">
    	<span>
    		Change History, items <b>${changeHistory.currentRecord} - ${changeHistory.currentRecord + fn:length(auditEntries)}</b>
    		of <b>${totalResults}</b>
    	</span>
	</div>
	<c:if test="${(changeHistory.currentRecord > 0) || (totalResults > changeHistory.currentRecord + fn:length(auditEntries))}"> 
		<div class="changeEntry">
	    <c:if test="${changeHistory.currentRecord > 0}"> 
		    	<a class="nextPrev" href="#" onclick="nextPage('previous')">Previous</a>
	    </c:if>
		  <c:if test="${totalResults > changeHistory.currentRecord + fn:length(auditEntries)}"> 
		  	<a class="nextPrev" href="#" onclick="nextPage('next')">Next</a>
		  </c:if>
		</div>
	</c:if>
	
		      
	<c:forEach items="${auditEntries}" var="auditEntry">
	  <div class="changeEntry" >
				Revision <c:out value="${auditEntry.version}"/>, 
				<c:out value="${actionLabels[auditEntry.action]}"/> <%-- TODO switch to messages here --%>
				<a class="links" href="#" onclick="alert('Not Implemented')">${auditEntry.user}</a> 
	 		<fmt:formatDate value="${auditEntry.dateCreated}" pattern="MMM dd, yyyy hh:mm a"/>
	 		
	 		<c:choose>
		 		<c:when test="${fn:length(auditEntry.auditEntryFieldChange) > 0}">
			 		<div class="simpleTable">
						<display:table name="${auditEntry.auditEntryFieldChange}" id="dt">
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
					<a href="#">revert</a>
		 		</c:when>
		 		<c:otherwise>
		 			<div><em>There were no changes</em></div>
		 		</c:otherwise>
	 		</c:choose>

	  </div>
	</c:forEach>
    
	<c:if test="${(changeHistory.currentRecord > 0) || (totalResults > changeHistory.currentRecord + fn:length(auditEntries))}"> 
     <div class="changeEntry">
	    <c:if test="${changeHistory.currentRecord > 0}"> 
		    	<a class="nextPrev" href="#" onclick="nextPage('previous')">Previous</a>
	    </c:if>
		  <c:if test="${totalResults > changeHistory.currentRecord + fn:length(auditEntries)}"> 
		  	<a class="nextPrev" href="#" onclick="nextPage('next')">Next</a>
		  </c:if>
		</div>			
	</c:if>
