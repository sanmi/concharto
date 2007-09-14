<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%@taglib tagdir="/WEB-INF/tags" prefix="example"%>
<example:page title="Events">
  	Events
    <table >

    <c:forEach items="${tsEventList}" var="event">
        <tr>
            <td>${event.id}</td>
            <td>${event.summary}</td>
            <td>${event.description}</td>
            <td>
            <c:forEach items="${event.userTags}" var="tag">
            	<c:out value="${tag}"/>
            </c:forEach>
            </td>
            <c:url value="/event.htm?listid=${event.id}" var="editLink"/>
            <td><a href="${editLink}">[edit]</a></td>
            <c:url value="/switchboard/deleteEvent.htm?listid=${event.id}" var="delLink"/>
            <td><a href="${delLink}">[x]</a></td> 
        </tr>
    </c:forEach>
    </table>
    <c:url value="/event.htm" var="addLink"/>
    <a href="${addLink}">Add an Event</a>

</example:page>

