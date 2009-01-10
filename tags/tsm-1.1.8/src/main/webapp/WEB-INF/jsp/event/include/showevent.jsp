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
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="wiki" uri="wikiRender" %>
			<div >
	  		<table class="infoBox wikitext"><tr>
					<td>
          	<%-- &nc means don't count this as a page hit in google analytics --%>
						<iframe src="${basePath}search/mapthumbnail.htm?id=${event.id}&nc"
	   						height="250" width="400" frameborder="0" scrolling="no">
						   This browser doesn't support embedding a map.
	  				</iframe>
					</td>
	  			<td>
						<h2>Event Information</h2>
						<div class="formRow">
							<span class="formLabel">Summary:</span> <c:out value="${event.summary}" escapeXml="true"/>
						</div>
						<div class="formRow">
							<span class="formLabel">When:</span> ${event.when.asText}
						</div>
						<div class="formRow">
							<span class="formLabel">Where:</span> <c:out value="${event.where}" escapeXml="true"/>
						</div>
						<div class="formRow">
							<span class="formLabel">Description:</span>
							<wiki:render wikiText="${event.description}"/> 
						</div>
						<div class="formRow">
								<span class="formLabel">Tags: </span><c:out value="${event.userTagsAsString}" escapeXml="true"/>
						</div>
						<div class="formRow">
							<span class="formLabel">Source:</span> 
							<wiki:render wikiText="${event.source}"/>
						</div>
						<c:if test="${event.hasUnresolvedFlags}">
							<div class="formRow">
								<span class="errorLabel">This event has unresolved flags</span>
							</div>
						</c:if>
						<div class="formRow">
							<a href="${basePath}edit/event.htm?id=${event.id}">Edit this event</a>
						</div>
					</td>
				</tr>
				
			</table>				
			</div>
