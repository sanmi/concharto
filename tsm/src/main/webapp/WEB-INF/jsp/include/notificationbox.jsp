<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:if test="${null != hasMessages}">
 	<div id="notification" class="notificationBox">
 		You have <a href="/page.htm?page=User_talk:${username}">New Messages</a> on your Talk Page.
 	</div>
</c:if>
