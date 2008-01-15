<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tsm"%>


<tsm:page title="Lost Password Sent">
	<jsp:attribute name="stylesheet">header.css</jsp:attribute>

	<jsp:body>
	  	<div class="memberForm">
				<h2>Your password has been reset</h2>
				<p>Welcome back!</p>	
		  	<p>Back to <a href="${basePath}">Time Space Map</a></p>	
		  </div>
	  
	</jsp:body>
</tsm:page>
