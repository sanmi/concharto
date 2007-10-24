<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="display" uri="http://displaytag.sf.net" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tsm"%>
<%@ page import="com.tech4d.tsm.auth.AuthConstants" %>

<tsm:page title="Flags">
	<jsp:attribute name="script">resizeframe.js</jsp:attribute>
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
var iframeids=["changes"]
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
						<display:column property="comment" title="Comment" />
						<display:column title="Reason" >
							<spring:message code="flag.reason.${dt.reason}"/>
						</display:column>
						<display:column sortable="true" style="width:14em" title="Disposition">
							<c:choose>
								<c:when test="${dt.disposition != null}">
									${dt.disposition} 
									<%-- Only admins can resolve flags --%>
									<c:if test="${fn:contains(roles, 'admin')}">
										(<a class="links" href="${basePath}admin/flagdisposition.htm?id=${dt.id}"
										>Reopen</a>) 
									</c:if>
								</c:when>
								<c:otherwise>
									<%-- Only admins can resolve flags --%>
									<c:if test="${fn:contains(roles, 'admin')}">
										<c:forEach items="${dispositions}" var="dispositionCode">										
											<a class="errorlinks" href="#" onclick="confirmAndSubmit(${dt.id}, '${dispositionCode}')">
												<spring:message code="flag.disposition.${dispositionCode}"/>
											</a><br/>
										</c:forEach>
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
			<iframe id="changes" src="${basePath}edit/changehistoryframe.htm?id=${event.id}"
					 width="100%" height="700px" frameborder="0" >
		   This browser doesn't support showing change history
			</iframe>
		</div>

		<input class="action" type="submit" value="Return to Search" onclick="document.location='${basePath}search/eventsearch.htm'"/>

	</div>
</jsp:body>
</tsm:page>

