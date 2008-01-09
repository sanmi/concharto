<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="wiki" uri="wikiRender" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tsm"%>
<% pageContext.setAttribute("linefeed", "\n"); %>

<tsm:page title="Change History">
	<jsp:attribute name="stylesheet">map.css,header.css,discuss.css</jsp:attribute>
	<jsp:attribute name="bodyattr">class="mapedit"</jsp:attribute>	
	<jsp:attribute name="head">
		<script type="text/javascript">
		//<![CDATA[

	function preview() {
		document.getElementById("discussForm").showPreview.value = 'true';
		document.discussion.submit();
	}
	
	function submitDiscussion() {
		document.getElementById("discussForm").showPreview.value = 'false';
		document.discussion.submit();
	}
	
		//]]>
		</script>
	</jsp:attribute>
	

<jsp:body>
			<div class="changes">
				<div class="miniTabBar">
					<a class="miniTabUnselected" href="${basePath}edit/event.htm?id=${param.id}">Event</a>
					<span class="miniTabSelected" >Discussion</span>
				  <a class="miniTabUnselected miniTabLSpace" href="${basePath}event/discusshistory.htm?id=${discussForm.event.discussion.id}&eventId=${param.id}">Changes</a>
				</div>
				<div class="changeBar"></div>

		  	
		  	<div id="discuss">
		  	
		  		<div class="infoBox">
			  		This is a discussion page.  Please respect the 
			  		<a href="http://wiki.timespacemap.com/Discussion_Guidelines">discussion guidelines</a>, 
			  		and remember to sign your posts by typing four tildes (~~~~).
		  		</div>
		  		
		  		<div class="infoBox">
			  		<c:set var="event" scope="request" value="${discussForm.event}"/>
			  		<jsp:include page="../event/include/showsummary.jsp"/>
			  	</div>
		  		
		  		<c:if test="${discussForm.showPreview == true}">
			  		<h1>Preview</h1>
			  		<span class="warning">This is only a preview; changes have not been saved! </span>
				  	<p>
				  	<hr/>
				  	<div class="preview wikitext">
						<wiki:render wikiText="${discussForm.event.discussion.text}"/> 
						</div> 	
		  		</c:if>

					<form:form name="discussion" id="discussForm" commandName="discussForm" >
						<form:hidden path="showPreview" />
						<div>
							<form:textarea path="event.discussion.text"/>
						</div>
			  		<input type="button" value="Submit" onclick="submitDiscussion()"  accesskey="s" title="Save your changes [alt+shift+s]"/>
			  		<input type="button" value="Preview" onclick="preview()" accesskey="p" title="Preview your changes, please use this before saving! [alt+shift+p]"/>
			  		<input type="button" value="Cancel" onclick="document.location='${basePath}event/discuss.htm?id=${param.id}'"/>
		  		</form:form>
		  		<div class="infoBox">
		  			<p>
							Please note that all contributions to Time Space Map are considered to be released under the 
							Creative Commons Attribution-Share Alike 3.0 United States License (see  
							<a href="http://wiki.timespacemap.com/Legal#User_Submissions.">User Submissions Policy</a> for details). 
							If you don't want your writing to be edited mercilessly and redistributed at will, then don't 
							submit it here.
						</p>
						<p>
							You are also promising us that you wrote this yourself, or copied it from a public domain or 
							similar free resource. <b>DO NOT SUBMIT COPYRIGHTED WORK WITHOUT PERMISSION!</b>
						</p>		  		
		  		</div>
				</div>
			</div>
</jsp:body>
</tsm:page>
