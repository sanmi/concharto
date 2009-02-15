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
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="display" uri="http://displaytag.sf.net" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tsm"%>

<tsm:page title="Discussion History">
	<jsp:attribute name="stylesheet">map.css,simpleform.css,header.css</jsp:attribute>
	<jsp:attribute name="bodyattr">class="mapedit"</jsp:attribute>

<jsp:body>
	<div class="pagepanel">
		<div class="miniTabBar">
			<a class="miniTabUnselected" href="${basePath}edit/event.htm?id=${param.eventId}">Event</a>
	   	<a class="miniTabSelected" href="${basePath}event/discuss.htm?id=${param.eventId}">Discussion</a>
			<span class="miniTabSelected miniTabLSpace" >Changes</span>
    </div>
    <div class="changeBar"></div>

		<jsp:include page="../include/wikihistory.jsp"/>
		
	</div>	  	
</jsp:body>
</tsm:page>

