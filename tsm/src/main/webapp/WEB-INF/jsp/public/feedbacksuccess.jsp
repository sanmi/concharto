<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tsm"%>


<tsm:page title="Feedback - Thanks">
	<jsp:attribute name="stylesheet">textpanel.css,header.css</jsp:attribute>

	<jsp:body>
	  	<div class="textpanel">
				<h2>Thank You</h2>
				<p>Thank you for providing your feedback!</p>	
		  	<p>Back to <a href="${basePath}">Time Space Map</a></p>	
		  </div>
	  
	</jsp:body>
</tsm:page>
