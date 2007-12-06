<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<div id="nav">
    <div class="nav_block">
			<ul>
			<c:if test="${username == null}">
				<li><a href="${basePath}login.htm">Sign in / create account</a></li>
				</c:if><c:if test="${username != null}">
					<li><span class="name">Hello, <b>${username}</b> </span> 
					<a href="${basePath}member/settings.htm">Settings</a>
				</li>
				<li> | <a href="${basePath}logout.htm">Sign out</a></li>
				</c:if></ul>
    </div>
    <div class="nav_left" ><img src="${basePath}images/nav-left.png" /></div>
  </div>

<div class="clearfloat"></div>

