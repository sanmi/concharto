<%@ taglib tagdir="/WEB-INF/tags" prefix="tsm"%>


<tsm:page title="Not Authorized">
	<jsp:body>
	  	<div class="loginForm">
				<h2>Sorry, you are not authorized to use this page</h2>
				<a href="${pageContext.request.contextPath}/">Return</a>
			</div>	  
	</jsp:body>
</tsm:page>

