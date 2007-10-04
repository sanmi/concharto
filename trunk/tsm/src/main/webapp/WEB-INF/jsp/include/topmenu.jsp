<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<div id="nav">
<ul>
<li><a href="<%=path %>">Home</a></li
><li><a href="#">Logout</a></li
></ul>
</div>