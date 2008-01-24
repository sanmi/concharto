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
	<jsp:attribute name="stylesheet">map.css,simpleform.css,header.css</jsp:attribute>
	<jsp:attribute name="bodyattr">class="mapedit"</jsp:attribute>

	<jsp:body>
		<div class="pagepanel">
			<div class="simpleForm">
				<form:form name="flagEvent" id="flagEventForm" commandName="flagEvent"  method="post">
						<form:errors path="*"> 
					<div class="infoBox">
						<span class="errorLabel">There are problems with the information you have entered</span> 
					</div>
						</form:errors>
					<div class="infoBox">
						<h2>Should this Event be Deleted or Moved?</h2>
						<div>Before flagging a item, please check the <a href="#flagHistory">flag history</a> 
							and the <a href="#changeHistory">change history</a> 
			  			to be sure you can't simple revert a recent change.  Also, please review the 
			  			<a href="http://wiki.timespacemap.com/wiki/Deletion_Policy" >deletion policy</a>.  For instance, if
			  			an event violates the <a href="http://wiki.timespacemap.com/wiki/Guidelines" >Neutral Point of View Policy</a>, it may be better to fix it than delete it.
				  	</div>
				  </div>
					<div class="infoBox">
						<form:errors cssClass="errorLabel" path="reason"/>			
						<h2>Reason for Flagging this Event</h2>
						<div class="formRow">
							<c:forEach items="${reasons}" var="reasonCode">
								<form:radiobutton path="reason" value="${reasonCode}"/> <spring:message code="flag.reason.${reasonCode}"/><br/>	  			
							</c:forEach>
						</div>
							<form:errors cssClass="errorLabel" path="comment"/>			
							<h2>Comments</h2>
						<div class="formRow">
							<form:textarea rows="3" cols="80" path="comment"/>
						</div>
						<div class="formRow">
							<input type="submit" value="Submit to Administrators" />
							<input type="button" value="Cancel" onclick="document.location='${basePath}search/eventsearch.htm'"/>
						</div>
					</div>
			  	<jsp:include page="../event/include/showevent.jsp"/>
			  	<a name="flagHistory"/>
			  	<div class="infoBox">
						<h2>Flags on this Event</h2>
						<jsp:include page="../event/include/showflaghistory.jsp"/>
					</div>
				  	
			  	<a name="changeHistory"/>
					<div class="infoBox">
						<h2>Change History</h2>
						<jsp:include page="../event/include/showchangehistory.jsp"/>
					</div>
				</form:form>
			</div>
		</div>
	</jsp:body>
</tsm:page>

