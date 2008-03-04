<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="talkpage" value="User_talk:${param.user}"/>
<c:set var="userpage" value="User:${param.user}"/>
<c:if test="${null == userPages[talkpage]}">
	<c:set var="talkclass" value="new_links"/>
</c:if>
<c:if test="${null == userPages[userpage]}">
	<c:set var="userclass" value="new_links"/> 
</c:if>
<span class="${userclass}"><a href="${basePath}page.htm?page=User:${param.user}">${param.user}</a></span> 
(<span class="${talkclass}"><a href="${basePath}page.htm?page=User_talk:${param.user}">Talk</a></span> 
| <a  href="${basePath}event/contributions.htm?user=${param.user}" >Contribs</a>)
