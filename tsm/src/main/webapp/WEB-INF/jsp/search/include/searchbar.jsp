<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<div>
	<span style="float:left;">
		<img src="${basePath}images/tsm-globe.png" alt="" /><img src="${basePath}images/tsm-text.png" alt="" />
	</span>
	<div style=" float:right; min-height:63px; min-width:506px; background-image: url(/images/banner-timeline.png); background-repeat: no-repeat;">
	</div>
	<table class="searchbar" >
		<tr>
			<td>
				<span class="labelcell">
	  			<form:errors path="where"><span class="errorLabel"></form:errors>
	        Where 
	  			<form:errors path="where"></span></form:errors>
          <small>e.g., "Gettysburg, PA" </small><br/>
          <form:input path="where" size="22"/>
       </span>
			</td>
			<td>
       <span class="labelcell">
         <form:errors path="when"><span class="errorLabel"></form:errors>
         	When
         <form:errors path="when"></span></form:errors>
         <small>
           e.g. "1962", "Oct 14, 1066", "1880-1886" 
         </small><br/>
         <form:input path="when" size="35"/>
       </span>
      </td>
      <td>
       <span class="labelcell">
       	What
         <small>
           e.g. "Battle" 
         </small><br/>
           <form:input path="what" size="22" htmlEscape="true"/>
       </span>
      </td>
		</tr>
	</table>
	<table class="searchbar" style="width:48em; padding:0; margin:0"><tr><td>
    <span class="action"><input type="submit" name="Search" value="Search" /></span>
    <span class="action"><input type="button" name="add" value="Add to the Map!" onclick="editEvent('')"/></span>
    <c:choose>
    	<c:when test="${param.showSearchOptions == 'true'}">
		    <span class="action">
		  		<form:checkbox path="isOnlyWithinMapBounds" value="true"/> Only near current map 
		  		<%-- <form:checkbox path=""/> Exclude overlapping time ranges --%>
		    </span>
    	</c:when>
    	<c:otherwise>
	  		<form:hidden path="isOnlyWithinMapBounds"/> 
    	</c:otherwise>
    </c:choose>
  
 	</td></tr></table>
 	<c:if test="${fn:contains(roles, 'admin')}">
   	<table class="searchbar" style="width:30em;"><tr><td>
     <span style="padding-top:.5em" class="adminBox">
     	ADMIN:
    		<span class="adminField">
    			<form:radiobutton value="normal" path="show"/>
    			Normal
    		</span>
    		<span class="adminAction">
    			<form:radiobutton value="hidden" path="show"/>
    			Removed
    		</span>
    		<span class="adminAction">
    			<form:radiobutton value="flagged" path="show"/>
    			Flagged
    		</span>
    	</span>
  	</td></tr></table>
	</c:if>
</div>
<div class="clearfloat"></div>