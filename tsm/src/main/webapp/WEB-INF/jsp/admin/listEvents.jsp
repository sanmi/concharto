<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%@taglib tagdir="/WEB-INF/tags" prefix="tsm"%>
<tsm:page title="Events">
    <c:url value="/event.htm" var="addLink"/>
    <a href="${addLink}">Add an Event</a>
    <c:url value="/eventsearch.htm" var="searchLink"/>
    <a href="${searchLink}">Search</a>
    <table class="eventlist">
    	<thead>
    		<td>ID</td>
    		<td>Summary</td>
    		<td>Where</td>
    		<td>When</td>
    		<td>Description</td>
    		<td>Tags</td>
    		<td>Source</td>
    		<td colspan="2">&nbsp;</td>
    	</thead>

    <c:forEach items="${eventList}" var="event">
        <tr>
            <td>${event.id}&nbsp;</td>
            <td>${event.summary}&nbsp;</td>
            <td>${event.where}&nbsp;</td>
            <td>${event.when.begin} - ${event.when.end}&nbsp;</td>
            <td>${event.description}&nbsp;</td>
            <td>
            <c:forEach items="${event.userTags}" var="tag">
            	<c:out value="${tag}"/>
            </c:forEach>&nbsp;
            </td>
            <td>${event.sourceUrl}&nbsp;</td>
            <c:url value="/event.htm?listid=${event.id}" var="editLink"/>
            <td><a href="${editLink}">[edit]</a></td>
            <c:url value="/switchboard/deleteEvent.htm?listid=${event.id}" var="delLink"/>
            <td><a href="${delLink}">[x]</a></td> 
        </tr>
    </c:forEach>
    </table>

</tsm:page>

