<%@tag description="page layout" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@attribute name="head" fragment="true"%>
<%@attribute name="title" required="true" type="java.lang.String"%>
<%@attribute name="script" required="false" type="java.lang.String"%>
<%@attribute name="stylesheet" required="false" type="java.lang.String"%>
<%@attribute name="bodyattr" required="false" type="java.lang.String"%>
<%@attribute name="stripped" required="false" type="java.lang.String"%>
<%@attribute name="nohead" required="false" type="java.lang.String"%>
<%@attribute name="nohomemenu" required="false" type="java.lang.String"%>
<%@tag import="com.tech4d.tsm.auth.AuthConstants" %>

<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+path;
int port = request.getServerPort();
if (port != 80) {
	basePath += ":" + port;
}
basePath += "/";
request.setAttribute("basePath", basePath);
request.setAttribute("roles", (String)request.getSession().getAttribute(AuthConstants.SESSION_AUTH_ROLES));
request.setAttribute("username", (String)request.getSession().getAttribute(AuthConstants.SESSION_AUTH_USERNAME));
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd" >
<html xmlns="http://www.w3.org/1999/xhtml" xmlns:v="urn:schemas-microsoft-com:vml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<meta http-equiv="Pragma" content="no-cache">
	<title>Time Space Map - ${title}  </title>
	<style type="text/css">

  @import url("${basePath}css/style.css");

	<c:forTokens var="item" items="${stylesheet}" delims=",">
    @import url("${basePath}css/${item}");
	</c:forTokens>    
	</style>

	<jsp:invoke fragment="head" />
	
	<c:forTokens var="item" items="${script}" delims=",">
		<script type="text/javascript" src="${basePath}javascript/${item}"></script>
	</c:forTokens>    
</head>

<body ${bodyattr}>

	<c:if test="${stripped != 'true'}">
		<jsp:include flush="true" page="/WEB-INF/jsp/include/topmenu.jsp"/>		
		<c:if test="${nohead != 'true'}">
			<jsp:include flush="true" page="/WEB-INF/jsp/include/head.jsp"/>
	  </c:if>
		<c:if test="${nohomemenu != 'true'}">
			<div id="homemenu"> 
		  	<a href="${basePath}" id="home"><i>Home</i></a>
		  </div>
	  </c:if>		
  </c:if>
	<div id="content">
		<jsp:doBody />
	</div>
	<c:if test="${stripped != 'true'}">
	  <div id="footer">
			&copy 2007, Time Space Map
			<a class="linkleft links" href="http://wiki.timespacemap.com/wiki/Legal" >Legal</a>
			<a class="linkleft links" class="linkleft" href="http://wiki.timespacemap.com/wiki/About" >About</a>
			<a class="links linkleft" href="${basePath}feedback.htm">Feedback</a>
	  	<span class="links linkleft">Version <spring:message code="app.version"/></span>
			<a class="links linkleftright" href="http://creativecommons.org/licenses/by-sa/3.0/us" >
				<img src="http://i.creativecommons.org/l/by-sa/3.0/us/80x15.png"/>
			</a>
	  </div>
  </c:if>

	<script src="http://www.google-analytics.com/urchin.js" type="text/javascript">
	</script>
	<script type="text/javascript">
		_uacct = "UA-1809442-6";
		urchinTracker();
	</script>

  </body>
</html>
