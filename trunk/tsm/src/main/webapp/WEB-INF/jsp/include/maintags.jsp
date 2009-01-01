<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<h2>Recently created event tags:</h2>
<c:forEach items="${tagCloud}" var="entry">
  <a href="${basePath}search/eventsearch.htm?_tag=<c:out value='${entry.tag}'/>&_x">
  <span style="font-size: <c:out value='${entry.fontSize}'/>pt"><c:out value="${entry.tag}"/></span>
  </a> &nbsp;&nbsp;
</c:forEach>

