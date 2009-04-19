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
<%-- wiki convention is that the user page "Joe Smith" be written "Joe_Smith" --%>
<c:set var="userPageName" value="${fn:replace(param.user,' ','_')}"/>
<c:set var="talkpage" value="User_talk:${userPageName}"/>
<c:set var="userpage" value="User:${userPageName}"/>
<c:if test="${null == userPages[talkpage]}">
	<c:set var="talkclass" value="new_links"/>
</c:if>
<c:if test="${null == userPages[userpage]}">
	<c:set var="userclass" value="new_links"/> 
</c:if>

<span class="${userclass}"><a href="${basePath}page.htm?page=${userpage}">${param.user}</a></span> 
(<span class="${talkclass}"><a href="${basePath}page.htm?page=${talkpage}">Talk</a></span>
<%-- TODO this is goofy.  There should be a better way to do it --%>
<c:choose>
	<c:when test="${param.excludecontribs == null}">
	| <a href="${basePath}event/contributions.htm?user=${param.user}" >Contribs</a>)
	</c:when>
	<c:otherwise>
	)
	</c:otherwise>
</c:choose>
