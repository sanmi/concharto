<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<script type="text/javascript">
//<![CDATA[
	<%-- The javascript popup windows also need these localized strings --%>
	var _msg_newdiscuss = "<spring:message code='searchresults.linktitle.newdiscuss'/>";
	var _msg_discuss = "<spring:message code='searchresults.linktitle.discuss'/>";
	var _msg_edit = "<spring:message code='searchresults.linktitle.edit'/>";
	var _msg_changes = "<spring:message code='searchresults.linktitle.changes'/>";
	var _msg_flag = "<spring:message code='searchresults.linktitle.flag'/>";
	var _msg_accy = new Array();
	<c:forEach items="${positionalAccuracies}" var="accuracy" >
		_msg_accy[${accuracy.id}] = '<spring:message code="event.positionalAccuracy.${accuracy.id}"/>'; 
	</c:forEach>
	   
//]]>
</script>			
