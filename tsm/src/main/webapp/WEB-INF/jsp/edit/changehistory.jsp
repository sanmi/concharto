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
	<jsp:attribute name="stylesheet">map.css,header.css</jsp:attribute>
	<jsp:attribute name="bodyattr">class="mapedit"</jsp:attribute>
	

<jsp:body>
			<div class="changes">
				<div class="miniTabBar">
					<a class="miniTabUnselected" href="${basePath}edit/event.htm?listid=${param.id}">Event</a>
 		    	<a class="miniTabUnselected" href="${basePath}event/discuss.htm?id=${param.id}">Discussion</a>
					<span class="miniTabSelected  miniTabLSpace" >Changes</span>
	      </div>
	      <div class="changeBar">
		         <jsp:include page="include/showchangehistory.jsp"/>   
	      </div>
	  	  
	  	<input type="button" value="Back to Edit" onclick="edit(); return false;"/>
	  	
</jsp:body>
</tsm:page>

