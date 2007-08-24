<%@tag description="page layout" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@attribute name="head" fragment="true"%>
<%@attribute name="title" required="true" type="java.lang.String"%>
<%@attribute name="script" required="false" type="java.lang.String"%>
<%@attribute name="stylesheet" required="false" type="java.lang.String"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>${title} - Page Tag Example</title>
<style type="text/css">
    @import url("<c:url value="/css/style.css"/>");
	<c:forTokens var="item" items="${stylesheet}" delims=",">
    @import url("<c:url value="/css/"/>${item}");
	</c:forTokens>    
</style>

<c:forTokens var="item" items="${script}" delims=",">
<script type="text/javascript" src="<c:url value="/script/"/>${item}">
</script>
</c:forTokens>    
<jsp:invoke fragment="head" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="Pragma" content="no-cache">
</head>
<body>
<h2>Example App</h2>
Demonstrating Hibernate Annotations, Spring, Spring MVC w/ convention over configuration, maven, ant, jsp page.tag layout
<div id="nav">
<ul>
	<li><a href="listEvents.htm">List Events</a></li>
	<li><a href="page0.htm">Page 0</a></li>
	<li><a href="page1.htm">Page 1</a></li>
	<li><a href="page2.htm">Page 2</a></li>
</ul>
</div>
<div id="content">
<jsp:doBody />
</div>
  <hr>
  <div id="footer">
	Example App
  </div>
  </body>
</html>
