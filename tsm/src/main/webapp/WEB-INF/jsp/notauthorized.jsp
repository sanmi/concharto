<%@ taglib tagdir="/WEB-INF/tags" prefix="tsm"%>

<tsm:page title="Not Authorized">
	<jsp:attribute name="stylesheet">textpanel.css,header.css</jsp:attribute>
	<jsp:body>
	  	<div class="textpanel">
				<h2>Sorry, you are not authorized to use this page</h2>
				<a href="${pageContext.request.contextPath}/">Return</a>
			</div>	  
	</jsp:body>
</tsm:page>

