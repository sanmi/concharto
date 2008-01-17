<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tsm"%>


<tsm:page title="Admin Links">
	<jsp:attribute name="stylesheet">textpanel.css,header.css</jsp:attribute>

	<jsp:body>
  	<div class="textpanel">
			<h2>Admin Functions</h2>
			<ul>
				<li><a href="${basePath}admin/spotlightlist.htm">Spotlight Events:</a> Edit or create new events to be spotlighted on the home page</li>
				<li><a href="${basePath}admin/signup.htm">Signup form:</a> (Temporary during beta period).  Create new users.</li>
			</ul>
	  	<p>Back to <a href="${basePath}">Time Space Map</a></p>	
	  </div>
	  
	</jsp:body>
</tsm:page>

