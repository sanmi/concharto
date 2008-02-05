<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tsm"%>


<tsm:page title="Event">
	<jsp:attribute name="head">
		<jsp:include page="../include/mapkey.js.jsp"/>
	</jsp:attribute>
	<jsp:attribute name="stylesheet">map.css,header.css</jsp:attribute>
	<jsp:attribute name="script">prototype.js,map.js,control.modal.js,help.js,edit.js</jsp:attribute>
	<jsp:attribute name="bodyattr">onload="initialize()" onunload="GUnload();" class="mapedit" onresize="setMapExtent();"</jsp:attribute>

	<jsp:body>
		<table><tr>			
			<td id="sidebar">
        <form:form name="event" id="eventForm" commandName="event"  method="post" onsubmit="saveEvent(); return false">
					<form:hidden path="eventId"/>
					<form:hidden path="geometry" htmlEscape="true"/>
					<form:hidden path="zoomLevel"/>
					<form:hidden path="mapType"/>
					<form:hidden path="mapCenter" htmlEscape="true"/>
					<form:hidden path="searchResults" htmlEscape="true"/>
					<form:hidden path="showPreview"/>
					<form:hidden path="previewEvent" htmlEscape="true"/>
					<form:hidden path="addEvent"/>
					<%-- for javascript --%>
					<input type="hidden" id="basePath" value="${basePath}"/>
					
	    		<c:if test="${param.id != null}">
	   		    <div class="miniTabBar">
	   		    	<span class="miniTabSelected">Event</span>
	   		    	<a class="miniTabUnselected" href="${basePath}event/discuss.htm?id=${param.id}">Discussion</a>
	   		    	<a class="miniTabUnselected miniTabLSpace" href="${basePath}event/changehistory.htm?id=${param.id}">Changes</a>
		 		    </div>
	    		</c:if>
   		    <div class="inputcell ">
	 		    	<span id="larger" class="hidden"><a href="#" onClick="larger()">larger &raquo;</a></span>
 	 		    	<span id="smaller" class="hidden"><a href="#" onClick="smaller()">smaller &laquo;</a></span>
		        <span class="radio">
		        	<span class="inputlabel">Point:</span>
		        	<form:radiobutton path="geometryType" value="point" onclick="setupNewPoint()"/> 
		        </span>
		        <span class="radio">
		        	<span class="inputlabel">Line:</span>
	        		<form:radiobutton path="geometryType" value="line" onclick="setupNewPoly()"/> 
		        </span>
		        <span class="radio">
	        		<span class="inputlabel">Shape:</span>
	        		<form:radiobutton path="geometryType" value="polygon" onclick="setupNewPoly()"/> 
        		</span>
   		    </div>
   		    <div class="inputcell">
		        <span class="errorlabel"><form:errors path="geometry" element="div"/></span>
		        <span class="errorlabel"><form:errors path="summary" element="div"/></span>
		        <span class="inputlabel">Summary</span> 
		        <br/>
		        <form:input cssClass="textInput expando" path="summary" maxlength="${event.SZ_SUMMARY}" htmlEscape="true"/>
   		    </div>
   		    <div class="inputcell">
		        <span class="errorlabel"><form:errors path="when" element="div"/></span>
		        <span class="inputlabel">When</span> 
	          <small>
	            e.g. "1962" or "March, 1064" or "1880 - 1886" <a href="${basePath}help.htm?name=when" class="link_help">hint</a>
	          </small><br/>
		        <form:input cssClass="textInput expando" path="when" />
   		    </div>
   		    <div class="inputcell">
		        <span class="errorlabel"><form:errors path="where" element="div"/></span>
   		    	<span class="inputlabel">Where</span>
	          <small>e.g., "gettysburg, pa" <a href="${basePath}help.htm?name=where" class="link_help">hint</a></small><br/>
	          <form:input cssClass="textInput expando" path="where" maxlength="${event.SZ_WHERE}" htmlEscape="true"/>
	          <input  type="button" name="Find" value="Go to Location" onclick="showAddress(document.event.where.value); return false"/>             
	          <small id="tip"><b>Tip:</b> drag and drop the lollypop!</small>
   		    </div>
   		    <div class="inputcell">
   		    	<span class="inputlabel">Accuracy of the position </span>
						         
	          <spring:bind path="event.positionalAccuracy">
		          <select class="textInput" name="<c:out value="${status.expression}"/>" >
		          	<c:forEach items="${positionalAccuracies}" var="accuracy" >
			          	<option <c:if test="${event.positionalAccuracy == accuracy.id}">selected="true"</c:if> value="${accuracy.id}"><spring:message code="event.positionalAccuracy.${accuracy.id}"/></option>
		          	</c:forEach>
		          </select>
	          </spring:bind>
   		    	<small><a href="${basePath}help.htm?name=accuracy" class="link_help">hint</a></small>
   		    </div>
   		    <div class="inputcell">
		        <span class="errorlabel"><form:errors path="description" element="div"/></span>
	   		    <span class="inputlabel">Description</span> <small>(wiki markup) <a href="${basePath}help.htm?name=wikimarkup" class="link_help">hint</a></small><br/>
						<form:textarea cssClass="textInput expando" rows="5" path="description"/>
   		    </div>
   		    <div class="inputcell">
		        <span class="errorlabel"><form:errors path="tags" element="div"/></span>
   		    	<span class="inputlabel">Tags</span> <small>(comma separated) <a href="${basePath}help.htm?name=tags" class="link_help">hint</a></small> <br/>
		        <form:input cssClass="textInput expando" path="tags" maxlength="${event.SZ_TAGS}" htmlEscape="true"/>
  		    </div>
   		    <div class="inputcell">
		        <span class="errorlabel"><form:errors path="source" element="div"/></span>
   		    	<span class="inputlabel">Source</span> <small>e.g. URL, Book or Publication <a href="${basePath}help.htm?name=source" class="link_help">hint</a></small><br/>
			      <form:textarea cssClass="textInput expando" path="source" rows="3" htmlEscape="true"/>
   		    </div>
					<div class="inputcell">				
					 <input type="submit" name="Save" value="Save This Event" accesskey="s" title="Save your changes [alt+shift+s]"/>
					 <input type="button" name="Preview" value="Preview" accesskey="p" title="Save your changes [alt+shift+p]" onclick="preview();"/>
					 <input type="button" name="Cancel" value="Cancel" onclick="javascript:document.location='${basePath}search/eventsearch.htm';"/>
					 	<p class="textInput expando">
							By submitting this, you are releasing your work under the 
							<a href="http://wiki.timespacemap.com/wiki/Legal#User_Submissions">Creative Commons License</a>. 
							You are also promising us that you wrote this yourself, or copied it from a public domain or 
							similar free resource. <b>DO NOT SUBMIT COPYRIGHTED WORK WITHOUT PERMISSION!</b>
						</p>		  		
					 
					</div>
				
	      </form:form>
			</td>		   
			<td>
				<div id="map">
					Map coming...
					<noscript>
					  <p>
					    JavaScript must be enabled to get the map.
					  </p>
					</noscript>
				</div>
			</td>
	 	</tr></table>

	</jsp:body>
</tsm:page>

