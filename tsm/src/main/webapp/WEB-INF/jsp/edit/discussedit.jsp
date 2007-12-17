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
				<jsp:include page="../event/include/tabs.jsp"/>
		  	
		  	<div id="discuss">
		  	
		  		<div class="infoBox">
		  		 This is a discussion page.  Please respect the 
		  		 <a href="http://wiki.timespacemap.com/Discussion_Guidelines">discussion guidelines</a>, 
		  		 and remember to sign your posts by typing four tildes (~~~~).
		  		</div>
		  		<c:if test="${discussion.showPreview == true}">
			  		<h1>Preview</h1>
			  		<span class="warning">This is only a preview; changes have not been saved! </span>
				  	<p>
				  	<hr/>
				  	<div class="preview">
						<wiki:render wikiText="${discussion.wikiText.text}"/> 
						</div> 	
		  		</c:if>

					<form:form name="discussion" id="discussForm" commandName="discussion" >
						<form:hidden path="showPreview" />
						<div>
							<form:textarea path="wikiText.text"/>
						</div>
			  		<input type="button" value="Submit" onclick="submitDiscussion()"  accesskey="p" title="Save your changes [alt+shift+p]"/>
			  		<input type="button" value="Preview" onclick="preview()" accesskey="p" title="Preview your changes, please use this before saving! [alt+shift+p]"/>
			  		<input type="button" value="Cancel" onclick="document.location='${basePath}event/discuss.htm?id=${param.id}'"/>
		  		</form:form>
		  		<div class="infoBox">
		  			<p>
							Please note that all contributions to Time Space Map are considered to be released under the 
							Attribution-Noncommercial-Share Alike 3.0 Unported (see  
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
