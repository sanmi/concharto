<%@tag description="page layout" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@attribute name="head" fragment="true"%>
<%@attribute name="title" required="true" type="java.lang.String"%>
<%@attribute name="script" required="false" type="java.lang.String"%>
<%@attribute name="stylesheet" required="false" type="java.lang.String"%>
<%@attribute name="bodyattr" required="false" type="java.lang.String"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>Time Space Map - ${title} </title>
<style type="text/css">
    @import url("<c:url value="/css/style.css"/>");
	<c:forTokens var="item" items="${stylesheet}" delims=",">
    @import url("<c:url value="/css/"/>${item}");
	</c:forTokens>    
</style>

<c:forTokens var="item" items="${script}" delims=",">
<script type="text/javascript" src="<c:url value="/javascript/"/>${item}">
</script>
</c:forTokens>    

<jsp:invoke fragment="head" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="Pragma" content="no-cache">
</head>

<body ${bodyattr} }>

    <div id="head">
      <img src="<%=request.getContextPath()%>/images/logo.png" alt="" />
      <img src="<%=request.getContextPath()%>/images/banner-timeline.png" alt="" width="506" height="63" />
    </div>
<div id="content">
<jsp:doBody />
</div>
  <div id="footer">
	  <hr/>
	  <div id="clearFloat"/>  
		Time Space Map
  </div>
  </body>
</html>
