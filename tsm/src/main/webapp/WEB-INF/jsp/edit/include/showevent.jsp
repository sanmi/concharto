<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div class="infoBox">
	  		<table><tr>
	  			<td>
						<h2>Event Information</h2>
						<div class="formRow">
							<span class="formLabel">Summary:</span> ${event.summary}
						</div>
						<div class="formRow">
							<span class="formLabel">When:</span> ${event.when.asText}
						</div>
						<div class="formRow">
							<span class="formLabel">Where:</span> ${event.where}
						</div>
						<div class="formRow">
							<span class="formLabel">Description:</span> ${event.description}
						</div>
						<div class="formRow">
								<span class="formLabel">Tags:</span> ${event.userTags}
						</div>
						<div class="formRow">
							<span class="formLabel">Source:</span> ${event.source}
						</div>
						<c:if test="${event.hasUnresolvedFlags}">
							<div class="formRow">
								<span class="errorLabel">This event has unresolved flags</span>
							</div>
						</c:if>
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