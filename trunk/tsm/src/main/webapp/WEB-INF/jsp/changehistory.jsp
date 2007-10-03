<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tsm"%>

<tsm:page title="Change History">
	<jsp:attribute name="head">
		<script type="text/javascript">
		//<![CDATA[

	function getParameter ( queryString, parameterName ) {
	   // Add "=" to the parameter name (i.e. parameterName=value)
	   var parameterName = parameterName + "=";
	   if ( queryString.length > 0 ) {
	      // Find the beginning of the string
	      begin = queryString.indexOf ( parameterName );
	      // If the parameter name is not found, skip it, otherwise return the value
	      if ( begin != -1 ) {
	         // Add the length (integer) to the beginning
	         begin += parameterName.length;
	         // Multiple parameters are separated by the "&" sign
	         end = queryString.indexOf ( "&" , begin );
	      if ( end == -1 ) {
	         end = queryString.length
	      }
	      // Return the string
	      return unescape ( queryString.substring ( begin, end ) );
	   }
	   // Return "null" if no parameter has been found
	   return "null";
	   }
	}
	
	function edit() {
		var id = <c:out value="${id}"/>;
		document.location="event.htm?listid=" + id;
	}
		//]]>
		</script>
	</jsp:attribute>
	<jsp:attribute name="bodyattr">class="mapedit"</jsp:attribute>

	<jsp:body>
	   <div >
				<div style="margin-bottom:5px; margin-left:5px">
					<a href="#" id="unselectedMiniTab" onclick="edit(); return false;">Edit</a><span id="selectedMiniTab" >Change History</span>
        </div>
        <table class="eventlist">
          <c:forEach items="${auditEntries}" var="auditEntry">
          	<tr>
          		<td>
          			Revision <c:out value="${auditEntry.version}"/>, 
          			<c:out value="${actionLabels[auditEntry.action]}"/>
          			<c:out value="${auditEntry.user}"/> 
	          		<fmt:formatDate value="${auditEntry.dateCreated}" pattern="MMM dd, yyyy hh:mm a"/>
	          		<br/>
          			<ul>
	          		<c:forEach items="${auditEntry.auditEntryFieldChange}" var="changes">
	          			<li>
	          				<b><c:out value="${propertyLabels[changes.propertyName]}"/></b> was changed from 
	          				"<c:out value="${changes.oldValue}"/>" to
	          				"<c:out value="${changes.newValue}"/>"
	          			</li>
	          		</c:forEach>
          			</ul>
          			
          			<a href="#">revert</a>
          		</td>
          	</tr>
          </c:forEach>
          <tr>
          </tr>
        </table>
	   </div>
	
	</jsp:body>
</tsm:page>

