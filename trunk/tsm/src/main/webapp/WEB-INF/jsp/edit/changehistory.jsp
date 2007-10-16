<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
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
	         <%-- Add the length (integer) to the beginning --%>
	         begin += parameterName.length;
	         <%-- Multiple parameters are separated by the "&" sign --%>
	         end = queryString.indexOf ( "&" , begin );
	      if ( end == -1 ) {
	         end = queryString.length
	      }
	      <%-- Return the string --%>
	      return unescape ( queryString.substring ( begin, end ) );
	   }
	   <%-- Return "null" if no parameter has been found --%>
	   return "null";
	   }
	}
	
	function edit() {
		var id = <c:out value="${id}"/>;
		document.location="event.htm?listid=" + id;
	}
	
		<%-- user has clicked on next, prev or a page number --%>
	function nextPage(pageCommand) {
		document.getElementById("changeHistoryForm").pageCommand.value = pageCommand;
		document.change.submit();
	}
	
		//]]>
		</script>
	</jsp:attribute>
	<jsp:attribute name="bodyattr">class="mapedit"</jsp:attribute>

<jsp:body>
	<form:form name="change" id="changeHistoryForm" commandName="changeHistory" >
		<form:hidden path="currentRecord"/>
		<form:hidden path="pageCommand"/>
		<table><tr>			
			<div class="changes">
				<div class="miniTabBar">
					<a href="#" class="miniTabUnselected" onclick="edit(); return false;">Edit</a>
					<span class="miniTabSelected" >Change History</span>
	      </div>
	      <div class="changeEntry">
	      	<span>
	      		Change History, items <b>${changeHistory.currentRecord} - ${changeHistory.currentRecord + fn:length(auditEntries)}</b>
	      		of <b>${totalResults}</b>
	      	</span>
				</div>
				<div class="changeEntry">
			    <c:if test="${changeHistory.currentRecord > 0}"> 
				    	<a class="nextPrev" href="#" onclick="nextPage('previous')">Previous</a>
			    </c:if>
				  <c:if test="${totalResults > changeHistory.currentRecord + fn:length(auditEntries)}"> 
				  	<a class="nextPrev" href="#" onclick="nextPage('next')">Next</a>
				  </c:if>
				</div>
	      <c:forEach items="${auditEntries}" var="auditEntry">
	        <div class="changeEntry" >
	      			Revision <c:out value="${auditEntry.version}"/>, 
	      			<c:out value="${actionLabels[auditEntry.action]}"/>
	      			<c:out value="${auditEntry.user}"/> 
	       		<fmt:formatDate value="${auditEntry.dateCreated}" pattern="MMM dd, yyyy hh:mm a"/>
	       		<ul>
	         		<c:forEach items="${auditEntry.auditEntryFieldChange}" var="changes">
	         			<li>
	         				<b><c:out value="${propertyLabels[changes.propertyName]}"/> was changed from </b> 
	         				<em>"<c:out value="${changes.oldValue}"/>"</em> <b> to</b>
	         				<em>"<c:out value="${changes.newValue}"/>"</em>
	         			</li>
	         		</c:forEach>
	         	</ul>
	      		<a href="#">revert</a>
	        </div>
	      </c:forEach>
	      <div class="changeEntry">
			    <c:if test="${changeHistory.currentRecord > 0}"> 
				    	<a class="nextPrev" href="#" onclick="nextPage('previous')">Previous</a>
			    </c:if>
				  <c:if test="${totalResults > changeHistory.currentRecord + fn:length(auditEntries)}"> 
				  	<a class="nextPrev" href="#" onclick="nextPage('next')">Next</a>
				  </c:if>
				</div>			
	  	</div>   
	  	  
	  	<input type="button" value="Done" onclick="edit(); return false;"/>
	  	
	 	</tr></table>
	</form:form>
</jsp:body>
</tsm:page>

