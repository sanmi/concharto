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
