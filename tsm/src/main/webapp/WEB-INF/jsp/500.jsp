<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tsm"%>


<tsm:page title="Server Error">
	<jsp:attribute name="stylesheet">textpanel.css,header.css</jsp:attribute>

	<jsp:body>
		<div class="textpanel">
			<h2>
				Sorry, an error has occurred.  
			</h2>
			
			<br/><br/>
			Return <a class="links" href="${basePath}">home</a>
		</div>
	</jsp:body>
</tsm:page>

