<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tsm"%>


<tsm:page title="Help">
<jsp:attribute name="stripped">true</jsp:attribute>

	<jsp:body>
		<jsp:include page="include/help${param.name}.jsp"/>
	</jsp:body>
</tsm:page>
