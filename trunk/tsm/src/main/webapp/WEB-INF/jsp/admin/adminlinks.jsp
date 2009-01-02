<%-- 
 ***** BEGIN LICENSE BLOCK *****
 Version: MPL 1.1
 
 The contents of this file are subject to the Mozilla Public License Version 
 1.1 (the "License"); you may not use this file except in compliance with 
 the License. You may obtain a copy of the License at 
 http://www.mozilla.org/MPL/
 
 Software distributed under the License is distributed on an "AS IS" basis,
 WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 for the specific language governing rights and limitations under the
 License.
 
 The Original Code is Concharto.
 
 The Initial Developer of the Original Code is
 Time Space Map, LLC
 Portions created by the Initial Developer are Copyright (C) 2007 - 2009
 the Initial Developer. All Rights Reserved.
 
 Contributor(s):
 Time Space Map, LLC
 
 ***** END LICENSE BLOCK *****
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

