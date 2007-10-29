<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="display" uri="http://displaytag.sf.net" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tsm"%>

<tsm:page title="Flags">
	<jsp:attribute name="head">
		<script type="text/javascript">
		//<![CDATA[

	function confirmAndSubmit(id, disposition) {
		var ok = true;
		if (disposition == 'deleted') {
			if (!confirm("Deleting an event is permanent. Are you sure?")) {
				ok=false;			
			}
		}
		if (ok) {
			document.location = '${basePath}admin/flagdisposition.htm?id=' + id + '&disposition=' + disposition;
		}
	}
		//]]>
		</script>
				
	</jsp:attribute>
	<jsp:attribute name="bodyattr">class="mapedit"</jsp:attribute>

<jsp:body>
	<div class="simpleForm">

		<input class="action" type="submit" value="Return to Search" onclick="document.location='${basePath}search/eventsearch.htm'"/>
		
		<jsp:include page="include/showevent.jsp"/>
		
		<c:if test="${fn:length(event.flags) > 0}">
			<div class="infoBox">
				<h2>Flags on this Event</h2>
				<div class="simpleTable">
					<display:table id="dt" 
						name="event.flags" 
						sort="list" 
						requestURI="${basePath}edit/eventdetails.htm"
						pagesize="5"
						defaultsort="1"
						>
						<display:column defaultorder="descending" sortable="true" sortName="created" style="width:12em" title="Date" >
						 	<fmt:formatDate value="${dt.created}" pattern="MMM dd, yyyy hh:mm a"/>
						</display:column>
						<display:column sortable="true" title="User" >
						 	<a class="links" href="#" onclick="alert('Not Implemented')">${dt.user.username}</a>
						</display:column>
						<display:column title="Comment">${dt.comment}&nbsp;</display:column>
						<display:column title="Reason" >
							<spring:message code="flag.reason.${dt.reason}"/>
						</display:column>
						<display:column sortable="true" style="width:14em" title="Disposition">
							<c:choose>
								<c:when test="${dt.disposition != null}">
									${dt.disposition} 
									<%-- Only admins can resolve flags --%>
									<c:if test="${fn:contains(roles, 'admin')}">
										<span style="align:right" class="adminBox">ADMIN
										(<a class="adminLinks" href="${basePath}admin/flagdisposition.htm?id=${dt.id}"
										>Reopen</a>) 
										</span>
									</c:if>
								</c:when>
								<c:otherwise>
									<%-- Only admins can resolve flags --%>
									<c:if test="${fn:contains(roles, 'admin')}">
										<div class="adminBox">ADMIN<br/>
											<c:forEach items="${dispositions}" var="dispositionCode">										
												<a class="errorlinks" href="#" onclick="confirmAndSubmit(${dt.id}, '${dispositionCode}')">
													<spring:message code="flag.disposition.${dispositionCode}"/>
												</a><br/>
											</c:forEach>
										</div>
									</c:if>
								</c:otherwise> 
							</c:choose>
						</display:column>
					</display:table>
				</div>
			</div>
		</c:if>
		
		<div class="infoBox">
			<h2>Change History</h2>
			<jsp:include page="include/showchangehistory.jsp"/>
		</div>

		<input class="action" type="submit" value="Return to Search" onclick="document.location='${basePath}search/eventsearch.htm'"/>

	</div>
</jsp:body>
</tsm:page>
