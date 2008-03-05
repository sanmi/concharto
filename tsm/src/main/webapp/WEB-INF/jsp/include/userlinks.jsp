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
| <a  href="${basePath}event/contributions.htm?user=${param.user}" >Contribs</a>)
