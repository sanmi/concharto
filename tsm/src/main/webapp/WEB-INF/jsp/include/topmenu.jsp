<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<div class="nav">
<ul class="nav_left"><li><a href="${basePath}">Home</a></li
<c:if test="${fn:length(username)>0}">
></ul>
<ul class="nav_left"><li><a href="#" onclick="alert('Not Yet Implemented');">Add From Wikipedia</a></li
</c:if>
></ul>
<ul>
<c:if test="${username == null}">
<li><a href="${basePath}login.htm">Sign in / create account</a></li
></c:if><c:if test="${username != null}">
<li><span class="name">Hello <b>${username}</b></span> <a href="${basePath}member/settings.htm">Settings</a></li
><li><a href="${basePath}logout.htm">Sign out</a></li
></c:if></ul>
</div>

