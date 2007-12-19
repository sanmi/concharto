<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div>
	<b><c:out value="${discussForm.event.summary}" escapeXml="true"/></b>
</div>
<div>
	<em>${discussForm.event.when.asText}</em>
</div>
<div >
	<c:out value="${discussForm.event.where}" escapeXml="true"/>
</div>
