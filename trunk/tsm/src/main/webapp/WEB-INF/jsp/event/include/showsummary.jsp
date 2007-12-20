<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div>
	
	<b><c:out value="${event.summary}" escapeXml="true"/></b>
</div>
<div>
	<em>${event.when.asText}</em>
</div>
<div >
	<c:out value="${event.where}" escapeXml="true"/>
</div>
