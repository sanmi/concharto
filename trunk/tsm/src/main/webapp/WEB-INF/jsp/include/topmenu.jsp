<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<div id="nav">
    <div class="nav_block">
			<ul>
			<c:if test="${username == null}">
				<li><a href="${basePath}login.htm">Sign in / create account</a></li>
				</c:if><c:if test="${username != null}">
				<li>Hello, <b>${username}</b> &nbsp;</li>
				<li> 
					<a href="${basePath}">Home</a>  
				</li>
				<li>|</li>
				<li> 
					<a href="${basePath}member/settings.htm">Settings</a>
				</li>
				<li>|</li>
				<li> 
					<a href="${basePath}member/contributions.htm?user=${username}">Contributions</a>
				</li>
				<li>|</li>
				<li><a href="${basePath}logout.htm">Sign out</a></li>
				</c:if></ul>
    </div>
    <div class="nav_left" ><img src="${basePath}images/nav-left.png" /></div>
  </div>

<div class="clearfloat"></div>

