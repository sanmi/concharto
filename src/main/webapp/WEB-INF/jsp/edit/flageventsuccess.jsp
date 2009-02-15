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
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tsm"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>


<tsm:page title="Flag Event">
	<jsp:attribute name="head">
		<jsp:include page="../include/mapkey.js.jsp"/>
		
	</jsp:attribute>
	<jsp:attribute name="stripped">false</jsp:attribute>
	<jsp:attribute name="stylesheet">map.css,simpleform.css,header.css</jsp:attribute>
	<jsp:attribute name="bodyattr">class="mapedit"</jsp:attribute>

	<jsp:body>
		<div class="pagepanel">
			<div class="simpleForm">
				<div>
					<h2>Thank you, this event has been flagged</h2>
					<div>
						To find out what happens next, read the <a href="http://wiki.concharto.com/wiki/Deletion_Policy">deletion policy</a>
					</div>
					<br/>
			  	<input type="submit" value="Return to Search" onclick="document.location='${basePath}search/eventsearch.htm'"/>
			  </div>
			</div>
		</div>
	</jsp:body>
</tsm:page>

