<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

  <ul>
    <c:forEach items="${recentEvents}" var="event" varStatus="status">
      <li> 
        <c:if test="${event.hasUnresolvedFlag}">
          <a class="errorLabel" href="${basePath}event/changehistory.htm?id=${event.id}">Flagged! </a>
        </c:if>
        <a  href='${basePath}search/eventsearch.htm?_id=${event.id}'><c:out value="${event.summary}" escapeXml="true"/></a> <br/>
        ${event.when.asText} <br/>
        <em><c:if test="${event.where != null && event.where != ''}"><c:out value="${event.where}" escapeXml="true"/></c:if></em>
        <hr/>
      </li>
    </c:forEach>
    <li>
      <em><a href="${basePath}list/recent.htm">More recently added ...</a></em>
    </li>             
  </ul>        
