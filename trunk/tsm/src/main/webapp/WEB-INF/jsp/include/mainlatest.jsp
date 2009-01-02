<%-- 
 ***** BEGIN LICENSE BLOCK *****
 Version: MPL 1.1
 
 The contents of this file are subject to the Mozilla Public License Version 
 1.1 (the "License"); you may not use this file except in compliance with 
 the License. You may obtain a copy of the License at 
 http://www.mozilla.org/MPL/
 
 Software distributed under the License is distributed on an "AS IS" basis,
 WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 for the specific language governing rights and limitations under the
 License.
 
 The Original Code is Concharto.
 
 The Initial Developer of the Original Code is
 Time Space Map, LLC
 Portions created by the Initial Developer are Copyright (C) 2007 - 2009
 the Initial Developer. All Rights Reserved.
 
 Contributor(s):
 Time Space Map, LLC
 
 ***** END LICENSE BLOCK *****
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
