<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tsm"%>

<tsm:page title="Page Not Found">
	<jsp:attribute name="stylesheet">textpanel.css,header.css</jsp:attribute>

	<jsp:body>
<div class="textpanel">
	<h2>
		Sorry, we can't find the page you are looking for.  
	</h2>
	Return <a class="links" href="${basePath}">home</a>
</div>	
	</jsp:body>
</tsm:page>
