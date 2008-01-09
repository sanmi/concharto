<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tsm"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>


<tsm:page title="Flag Event">
	<jsp:attribute name="head">
		<jsp:include page="../include/mapkey.js.jsp"/>
		
	</jsp:attribute>
	<jsp:attribute name="stripped">false</jsp:attribute>
	<jsp:attribute name="stylesheet">simpleform.css,header.css</jsp:attribute>

	<jsp:body>
		
		<div class="simpleForm">
			<div >
				<h2>Thank you, this event has been flagged</h2>
				<div>
					To find out what happens next, read the <a href="http://wiki.timespacemap.com/Deletion_Policy">deletion policy</a>
				</div>
				<br/>
		  	<input type="submit" value="Return to Search" onclick="document.location='${basePath}search/eventsearch.htm'"/>
		  </div>
	  	
			
		</div>

	</jsp:body>
</tsm:page>

