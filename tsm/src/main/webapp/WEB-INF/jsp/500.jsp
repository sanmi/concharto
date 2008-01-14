<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tsm"%>


<tsm:page title="Server Error">
	<jsp:attribute name="stylesheet">header.css</jsp:attribute>

	<jsp:body>
		<div class="memberForm">
			<h2>
				Sorry, an error has occurred.  
			</h2>
				If you are a beta tester and this is the first time this has
				happened to you, please send a brief report to 
				<a href="mailto:bugs@timespacemap.com">bugs@timespacemap.com</a>.
				<br/><br/>	   
				If possible, try to describe:
				<ul style="padding-left:20px">
					<li>What you were doing when it happened</li>
					<li>Steps to reproduce the problem</li>
					<li>What operating system and version (e.g Windows XP, Mac OS 10.3.2</li>
					<li>Browser type (e.g. Firefox, Internet Explorer, Safari) and version</li>
				</ul>
			<br/>	   
			Thank you for your help!
			<br/><br/>
			Return <a class="links" href="${basePath}">home</a>
		</div>
	</jsp:body>
</tsm:page>

