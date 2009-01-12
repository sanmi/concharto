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
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tsm"%>


<tsm:page title="Admin Links">
	<jsp:attribute name="stylesheet">textpanel.css,header.css</jsp:attribute>

	<jsp:body>
  	<div class="textpanel">
			<h2>Admin Functions</h2>
			<ul>
				<li><a href="${basePath}admin/spotlightlist.htm">Spotlight Events:</a> Edit or create new events to be spotlighted on the home page</li>
			</ul>
	  	<p>Back to <a href="${basePath}">Concharto</a></p>	
	  </div>
	  
	</jsp:body>
</tsm:page>

