<%@tag description="page layout" pageEncoding="UTF-8"%>
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
<%@tag import="com.tech4d.tsm.web.filter.NotificationFilter" %>
<%@tag import="com.tech4d.tsm.web.filter.LoginFilter" %>
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
request.setAttribute("hasMessages", (String)request.getSession().getAttribute(NotificationFilter.SESSION_MESSAGES_PENDING));
request.setAttribute("hostprefix", (String)request.getSession().getAttribute(LoginFilter.SESSION_HOSTPREFIX));
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" >

<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
	<meta http-equiv="Pragma" content="no-cache"/>
	<meta name="description" content="an encyclopedic atlas of history and happenings that anyone can edit. It is a geographic wiki" />
	<meta name="keywords" lang="en" content="history, historical maps, historical events, geographic wiki, happenings, atlas, places, maps, locations, sharing, biography, neogeography, geospatial, collective mapping, collaborative mapping, metadata, tagging, groupmaps, mashups, web2.0, google, tools, geotagging, googlemaps, community, cartography"/>
	<title>Concharto - ${title}  </title>
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
		<jsp:include flush="true" page="/WEB-INF/jsp/include/topmenu.jsp">
			<jsp:param name="nohomemenu" value="${nohomemenu == true}"/>
		</jsp:include>		
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
			<span class="links linkleft">&copy; 2008, Time Space Map, LLC</span>
			<a class="linkleft links" href="http://wiki.concharto.com/wiki/Legal" >Legal</a>
			<a class="linkleft links" href="http://wiki.concharto.com/wiki/About" >About</a>
			<a class="links linkleft" href="${basePath}feedback.htm">Feedback</a>
			<a class="links linkleft" href="http://blog.concharto.com">Blog</a>
			<a class="links linkleft" href="http://wiki.concharto.com">Community Wiki</a>
	  	
			<a class="links linkleftright" href="http://creativecommons.org/licenses/by-sa/3.0/us" >
				<img alt="creative commons" src="http://i.creativecommons.org/l/by-sa/3.0/us/80x15.png"/>
			</a>
	  </div>
  </c:if>

	<!--  <spring:message code="app.version"/> -->
	<%-- the parameter 'nc', short for nocount is a private signal to not count the page hits to embedded iframes --%>
	<c:if test="${param.nc == null}">
		<script src="http://www.google-analytics.com/urchin.js" type="text/javascript">
		</script>
		<script type="text/javascript">
			_uacct = "UA-1809442-6";
			urchinTracker();
		</script>
	</c:if>

  </body>
</html>
