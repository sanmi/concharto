<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page import="com.tech4d.tsm.web.wiki.WikiConstants" %>
<%
	Boolean myPageExists = (Boolean)request.getSession().getAttribute(WikiConstants.SESSION_MYPAGE_EXISTS); 
	if ((null != myPageExists) && (myPageExists == false)) {
		request.setAttribute("myPageLinkClass", "new_links");
	}
	Boolean myTalkExists = (Boolean)request.getSession().getAttribute(WikiConstants.SESSION_MYTALK_EXISTS); 
	if ((null != myTalkExists) && (myTalkExists == false)) {
		request.setAttribute("myTalkLinkClass", "new_links");
	}
 %>

<div id="nav">
    <div class="nav_block">
			<ul>
			<c:if test="${username == null}">
				<c:if test="${param.nohomemenu != true}">
					<li><a href="${basePath}">Home </a></li>
					<li>|</li>
				</c:if>
				<li><a href="${basePath}login.htm">Sign in / create account</a></li>
				</c:if><c:if test="${username != null}">
				<li> 
					Hello, <b><c:out value="${username}"/></b>
				</li>
				<c:if test="${param.nohomemenu != true}">
					<li><a href="${basePath}">Home </a></li>
					<li>|</li>
				</c:if>
				<c:set var="userPageName" value="${fn:replace(username,' ','_')}"/>
				<li><span class="${myPageLinkClass}"><a href="${basePath}page.htm?page=User:${userPageName}">My Page</a></span></li>
				<li>|</li>
				<li><span class="${myTalkLinkClass}"><a href="${basePath}page.htm?page=User_talk:${userPageName}">My Talk</a></span></li>
				<li>|</li>
				<li> 
					<a href="${basePath}member/settings.htm">Settings</a>
				</li>
				<li>|</li>
				<li> 
					<a href="${basePath}event/contributions.htm?user=${username}">Contributions</a>
				</li>
				<c:if test="${fn:contains(roles, 'admin')}">
				<li>|</li>
				<li> 
					<a href="${basePath}admin/adminlinks.htm">Admin</a>
				</li>
				</c:if>
				<li>|</li>
				<li><a href="${basePath}logout.htm">Sign out</a></li>
				</c:if></ul>
    </div>
    <div class="nav_left" ><img style="height:20px;width:6px"  alt="" src="${basePath}images/nav-left.png" /></div>
  </div>

<div class="clearfloat"></div>

