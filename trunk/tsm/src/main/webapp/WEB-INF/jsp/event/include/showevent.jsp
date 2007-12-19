<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="wiki" uri="wikiRender" %>
			<div class="infoBox wikitext">
	  		<table><tr>
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
								<span class="formLabel">Tags:</span><c:out value="${event.userTags}" escapeXml="true"/>
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
					<td>
						<iframe src="${basePath}search/mapthumbnail.htm?id=${event.id}"
	   						height="250" width="400" frameborder="0" scrolling="no">
						   This browser doesn't support embedding a map.
	  				</iframe>
					</td>
				</tr>
				
			</table>				
			</div>