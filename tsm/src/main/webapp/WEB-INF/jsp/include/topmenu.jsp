<%--
Copyright 2009 Time Space Map, LLC

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page import="org.tsm.concharto.web.wiki.WikiConstants" %>
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
			<c:choose>
				<c:when test="${fn:contains(roles,'anonymous')}">
					<c:if test="${param.nohomemenu != true}">
						<li><a href="${basePath}">Home </a></li>
						<li>|</li>
					</c:if>
					<li><a href="${basePath}login.htm">Sign in / create account</a></li>
				</c:when>
				<c:otherwise>
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
				</c:otherwise>
			</c:choose>
			</ul>
    </div>
    <div class="nav_left" ><img style="height:20px;width:6px"  alt="" src="${basePath}images/nav-left.png" /></div>
  </div>

<div class="clearfloat"></div>

