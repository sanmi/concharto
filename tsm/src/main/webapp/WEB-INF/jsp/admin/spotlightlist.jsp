<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="display" uri="http://displaytag.sf.net" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tsm"%>


<tsm:page title="Spotlight List">
	<jsp:attribute name="stylesheet">textpanel.css,contributions.css,spotlight.css,header.css</jsp:attribute>
	<jsp:attribute name="script">prototype.js,control.modal.js</jsp:attribute>
	<jsp:attribute name="bodyattr">onload="init()"</jsp:attribute>
	<jsp:attribute name="head">
		<script type="text/javascript">
		//<![CDATA[

			function confirmAndSubmit(location) {
				if (confirm("Deleting is permanent. Are you sure?")) {
					document.location = location;
				}
			}

			function init() {
		    document.getElementsByClassName('link_map').each(function(link){
		        var modal = new Control.Modal(link, {
		        opacity: 0.2,
		        iframe: true, 
						containerClassName: 'helpbox',
		        width: 500,
		        height: 400
		        });
		    });
			}

		//]]>
		</script>				
	</jsp:attribute>
	
	<jsp:body>
	  	<div class="textpanel">
				<h2>Destination Spotlight Administration</h2>
				<div class="inputcell">
					<input type="button" value="Add" onclick="document.location='${basePath}admin/spotlightedit.htm'"/>
				</div>
				<display:table id="simpleTable" 
								name="spotlights" 
								sort="list" 
								requestURI="${requestURI}"
								pagesize="20"
								>
				<display:setProperty name="basic.msg.empty_list">There are no spotlights</display:setProperty>
				<display:setProperty name="paging.banner.placement">both</display:setProperty>
				<display:setProperty name="paging.banner.item_name">Spotlight</display:setProperty>
				<display:setProperty name="paging.banner.items_name">Spotlights</display:setProperty>
				<display:setProperty name="paging.banner.onepage">&nbsp;</display:setProperty>
				<display:setProperty name="paging.banner.no_items_found">&nbsp;</display:setProperty>
				<display:setProperty name="paging.banner.one_item_found">&nbsp;</display:setProperty>
				<display:setProperty name="paging.banner.all_items_found">
					<span class="pagebanner"><b>{0}</b> {1} found.</span>
				</display:setProperty>
				<display:setProperty name="paging.banner.some_items_found">
					<span class="pagebanner"><b>{0}</b> {1} found.</span>
				</display:setProperty>
				
				<display:column title="Action" class="action">
					<a href="${basePath}admin/spotlightedit.htm?id=${simpleTable.id}">edit</a> | 
					<a href="#" onclick="confirmAndSubmit('${basePath}admin/spotlightlist.htm?del=${simpleTable.id}')">delete</a> |
					<c:choose>
						<c:when test="${simpleTable.visible == true}">
							<a href="#" onclick="document.location='${basePath}admin/spotlightlist.htm?hide=${simpleTable.id}'">hide</a>
						</c:when>
						<c:otherwise>
							<a href="#" onclick="document.location='${basePath}admin/spotlightlist.htm?show=${simpleTable.id}'">show</a>
						</c:otherwise>
					</c:choose>
				</display:column>
				<display:column title="visible">
					<c:if test="${simpleTable.visible == true}">
						Yes
					</c:if>
				</display:column>
				<display:column>
					<table class="none">
						<tr>
							<td class="label">Added:</td>
							<td >
								<a  href="${basePath}event/contributions.htm?user=${simpleTable.addedByUser.username}" >${simpleTable.addedByUser.username}</a>
								, <fmt:formatDate value="${simpleTable.created}" pattern="MMM dd, yyyy hh:mm a"/>
							</td>
						</tr>
						<tr>
							<td class="label">Label:</td>
							<td>${simpleTable.label}</td>
						</tr>
						<tr>
							<td class="label">
								<c:set var="embeddedlink" value="${fn:replace(simpleTable.link, 'eventsearch.htm', 'embeddedsearch.htm')}"/>
								<small><a href="<c:out value="${embeddedlink}" escapeXml="true"/>&nc" class="link_map">(preview)</a></small>
							</td>
							<td></td>
						</tr>
					</table>
					
				</display:column>
		 	</display:table>		
		  </div>
	  
	</jsp:body>
</tsm:page>
