<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<c:forEach items="${tagIndex}" var="entry">
  <h1><c:out value="${entry.key.asText}"/></h1>
  <c:forEach items="${entry.value}" var="cloud">
    <a href="${basePath}search/eventsearch.htm?_tag=<c:out value='${cloud.tag}'/>&x">
    <span style="font-size: <c:out value='${cloud.fontSize}'/>pt"><c:out value="${cloud.tag}"/></span>
    </a> &nbsp;&nbsp;
  </c:forEach>
  
</c:forEach>