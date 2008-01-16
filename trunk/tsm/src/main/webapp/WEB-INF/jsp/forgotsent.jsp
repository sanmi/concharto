<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tsm"%>


<tsm:page title="Lost Password Sent">
	<jsp:attribute name="stylesheet">textpanel.css,header.css</jsp:attribute>

	<jsp:body>
	  	<div class="textpanel">
				<h2>Now check your email</h2>
				<p>You should have received an email with a link in it.  When you click that link, you will be able to reset your password.</p>
		  	<p>Back to <a href="${basePath}">Home</a></p>	
		  </div>
	  
	</jsp:body>
</tsm:page>
