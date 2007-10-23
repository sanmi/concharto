<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tsm"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
request.setAttribute("basePath", basePath);
%>

<tsm:page title="Flag Event">
	<jsp:attribute name="head">
		<script
			src="http://maps.google.com/maps?file=api&amp;v=2.x&amp;key=<spring:message code='map.key'/>"
			type="text/javascript">
		</script>		
		<script type="text/javascript">
			function closeIt() {
				parent.window.Control.Modal.close();
			}		
		</script>
	</jsp:attribute>
	<jsp:attribute name="stripped">false</jsp:attribute>

	<jsp:body>
		
		<div class="simpleForm">
			<div >
				<h2>Thank you, this event has been flagged</h2>
				<div>
					To find out what happens next, read the <a href="#" onclick="alert('Not Implemented')">deletion policy</a>
				</div>
				<br/>
		  	<input type="submit" value="Return to Search" onclick="document.location='${basePath}search/eventsearch.htm'"/>
		  </div>
	  	
			
		</div>

	</jsp:body>
</tsm:page>

