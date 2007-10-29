<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:choose>
	<c:when test="${fn:substring(event.source,0,7) == 'http://'}">
		<a href='<c:out value="${event.source}" escapeXml="true"/>'><c:out value="${event.source}" escapeXml="true"/></a>	
	</c:when>
	<c:otherwise>
		<c:out value="${event.source}" escapeXml="true"/>
	</c:otherwise>
</c:choose>
	