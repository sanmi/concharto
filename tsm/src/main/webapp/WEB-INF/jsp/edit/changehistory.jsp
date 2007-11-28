<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tsm"%>

<tsm:page title="Change History">
	<jsp:attribute name="head">
		<script type="text/javascript">
		//<![CDATA[

	function edit() {
		var id = <c:out value="${id}"/>;
		document.location="event.htm?listid=" + id;
	}
	
		//]]>
		</script>
	</jsp:attribute>
	<jsp:attribute name="stylesheet">map.css</jsp:attribute>
	<jsp:attribute name="bodyattr">class="mapedit"</jsp:attribute>
	

<jsp:body>
		<table><tr><td>			
			<div class="changes">
				<div class="miniTabBar">
					<a href="#" class="miniTabUnselected" onclick="edit(); return false;">Edit</a>
					<span class="miniTabSelected" >Change History</span>
	      </div>
	      <jsp:include page="include/showchangehistory.jsp"/>   
	  	  
	  	<input type="button" value="Back to Edit" onclick="edit(); return false;"/>
	  	
	 	</td></tr></table>
</jsp:body>
</tsm:page>

