<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tsm"%>
<%@ page import="com.tech4d.tsm.auth.AuthConstants" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
request.setAttribute("basePath", basePath);
String roles = (String)request.getSession().getAttribute(AuthConstants.AUTH_ROLES);
request.setAttribute("rolse", roles);
%>

<tsm:page title="Flags">
	<jsp:attribute name="head">
		
	</jsp:attribute>
	<jsp:attribute name="bodyattr">class="mapedit"</jsp:attribute>

<jsp:body>
	<div class="simpleForm">
		<jsp:include page="include/showevent.jsp"/>
			<c:forEach items="${event.flags}" var="flag">
				<div class="infoBox">
					<ul>
						<li>
							<span class="formLabel">User:</span> <a href="#" onclick="alert('Not Implemented')">${flag.user.username}</a>
						</li> 
						<li>
							<span class="formLabel">Comment:</span> ${flag.comment}
						</li> 
						<li>
							<span class="formLabel">Reason:</span> ${flag.reason} 
						</li>
						<c:if test="${flag.disposition != null}">
							<li>
								<span class="formLabel">Disposition:</span> ${flag.disposition} 
							</li>
						</c:if>
					</ul> 
					<c:if test="${fn:contains(roles, 'admin')}">
						
						<div class="formRow">
							<c:forEach items="${dispositions}" var="dispositionCode">
								<span class="action">
									<a class="links" href="${basePath}admin/flagdisposition.htm?id=${flag.id}&disposition=${dispositionCode}">
										<spring:message code="flag.disposition.${dispositionCode}"/>
									</a>
								</span>
							</c:forEach>
							<a class="links" href="${basePath}admin/flagdisposition.htm?id=${flag.id}&disposition=">
								Reopen
							</a>
						</div>
					</c:if>
				</div>
			</c:forEach>
			<input type="submit" value="Return to Search" onclick="document.location='${basePath}search/eventsearch.htm'"/>
			
	</div>
</jsp:body>
</tsm:page>

