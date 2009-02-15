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

<%-- NOTE: no newlines to reduce whitespace - TODO perhaps add a whitespace servlet filter--%>
An index of user defined tags ordered by century
<c:forEach items="${tagIndex}" var="entry">
<h1><c:out value="${entry.key.asText}"/></h1>
<c:forEach items="${entry.value}" var="entry">
<a href='${basePath}<c:url value="search/eventsearch.htm"><c:param name="_tag" value="${entry.tag}" /></c:url>'><span style="font-size: <c:out value='${entry.fontSize}'/>pt"><c:out value="${entry.tag}"/></span></a>&nbsp;&nbsp;
</c:forEach>
</c:forEach>
