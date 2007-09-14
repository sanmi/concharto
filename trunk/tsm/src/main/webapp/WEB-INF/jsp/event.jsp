<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<%@taglib tagdir="/WEB-INF/tags" prefix="example"%>
<example:page title="Events">
	<form:form commandName="event" method="post">
	    <form:hidden path="id"/>
	    <table>
	        <tr>
	            <td>Summary:</td>
	            <td><form:input path="summary"/></td>
	            <td></td>
	        </tr>
	        <tr>
	            <td>Description:</td>
	            <td><form:input path="description"/></td>
	            <td></td>
	        </tr>
	        <tr>
	            <td>Tags:</td>
	            <td><form:input path="tags"/></td>
	            <td></td>
	        </tr>
	        <tr>
	            <td colspan="3">
	                <input type="submit" value="Save Changes"/>
	            </td>
	        </tr>
	    </table>
	</form:form>
</example:page>

