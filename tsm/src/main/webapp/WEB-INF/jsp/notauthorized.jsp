<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tsm"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<tsm:page title="Not Authorized">
	<jsp:body>
	  	<div class="loginForm">
				<h2>Sorry, you are not authorized to use this page</h2>
				<a href="${pageContext.request.contextPath}">Return</a>
			</div>	  
	</jsp:body>
</tsm:page>

