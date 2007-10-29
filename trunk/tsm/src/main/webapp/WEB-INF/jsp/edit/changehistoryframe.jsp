<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tsm"%>

<tsm:page title="Change History">
	<jsp:attribute name="head">
		<script type="text/javascript">
		//<![CDATA[

		<%-- user has clicked on next, prev or a page number --%>
	function nextPage(pageCommand) {
		document.getElementById("changeHistoryForm").pageCommand.value = pageCommand;
		document.change.submit();
	}
	
		//]]>
		</script>
	</jsp:attribute>
	<jsp:attribute name="bodyattr">class="mapedit"</jsp:attribute>
	<jsp:attribute name="stripped">true</jsp:attribute>

<jsp:body>
	>
		<table><tr><td>			
				<jsp:include page="include/showchangehistory.jsp"/>				
	 	</td></tr></table>
</jsp:body>
</tsm:page>

