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
