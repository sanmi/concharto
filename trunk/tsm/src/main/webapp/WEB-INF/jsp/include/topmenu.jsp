<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
// TODO this is gross, fix this 
request.setAttribute("username", (String)request.getSession().getAttribute("username"));
%>
<div id="nav">
<ul>
<c:if test="${username == null}">
<li><a href="<%=path %>/login.htm">Sign in / create account</a></li
></c:if><c:if test="${username != null}">
<li><span class="name">Hello <b>${username}</b></span> <a href="<%=path %>/member/settings.htm">Settings</a></li
><li><a href="<%=path %>/logout.htm">Sign out</a></li
></c:if></ul>
</div>