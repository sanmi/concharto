<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<table id="headerbar">
 	<tr>
   	<td>
	    <div id="addbox">
        <span class="biglink"><a href="#" onclick="editEvent(''); return false;">Add to the Map...</a></span>
      </div>
      <c:if test="${param.showSearchOptions == 'true'}">
	      <div id="search_tagline">
	      	An encyclopedic atlas of history and happenings <br/>that anyone can edit.
	      </div>      
      </c:if>
      <div id="searchbox">
        <table>
          <tr>
	          <td>
			  			<form:errors path="where"><span class="errorLabel"></form:errors>
			        Where <span class="eg">e.g. Gettysburg, PA <a href="${basePath}help.htm?name=where" class="link_help">hint</a></span>
			  			<form:errors path="where"></span></form:errors>
		  			</td>
            <td>
            	<form:errors path="when"><span class="errorLabel"></form:errors>
            	When <span class="eg">e.g. 1962; Oct 14,1066; 1880-1886 <a href="${basePath}help.htm?name=when" class="link_help" >hint</a></span>
            	<form:errors path="when"></span></form:errors>
            </td>
            <td>
            	What <span class="eg">e.g. Battle <a href="${basePath}help.htm?name=what" class="link_help">hint</a></span>
            </td>
            <td></td>
          </tr>
          <tr>
            <td><form:input path="where" size="22"/></td>
            <td><form:input path="when" size="35"/></td>
            <td><form:input path="what" size="22" htmlEscape="true"/></td>
            <td><input type="submit" name="Search" value="Search" /></td>
          </tr>
				  <c:choose>
				  	<c:when test="${param.showSearchOptions == 'true'}">
		          <tr>
		            <td>
		            	<span class="options">
		            		<form:checkbox path="limitWithinMapBounds"/>
		            		Search current map shown.
		            	</span>
		            </td>
		            <td colspan="2">
		              <span class="options">
		              	<form:checkbox path="excludeTimeRangeOverlaps"/>
		              	Find events that occurred only <em>within</em> the time frame specified.
		              </span>
		            </td>
		            <td></td>
		          </tr>
				  	</c:when>
				  	<c:otherwise>
				  		<tr>
		            <td style="padding-top:-5px">
				 					<span class="options">All fields are optional</span>
							 		<form:hidden path="limitWithinMapBounds"/> 
				 				</td>
				 			</tr>
				  	</c:otherwise>
				  </c:choose>
        </table>
      </div>
    </td>
  </tr>
  <tr>
  	<td style="vertical-align: bottom;">
			<c:if test="${param.showSearchOptions == 'true'}">
			  <div id="linkhere_bar" class="linkBox" style="margin:.3em;float:right">
					<a href="#" onclick="exportKml(); return false;">kml</a>
				</div>
			  <div id="linkhere_bar" class="linkBox" style="margin:.3em;float:right">
					<a id="link_linkhere" href="#linkhere">Link to here</a>
				</div>
			</c:if>
  	</td>
  </tr>
</table>
<c:if test="${fn:contains(roles, 'admin') && param.showAdminBar == 'true'}">
	<div class="adminBox">
	 	<span class="label">ADMIN</span>
	 	<span class="adminField">
	 		<form:radiobutton value="normal" path="show"/> <span class="label">Normal </span>
	 	</span>
 		<span class="adminAction">
 			<form:radiobutton value="hidden" path="show"/>
 			<span class="label">Hidden </span>
 		</span>
 		<span class="adminAction">
 			<form:radiobutton value="flagged" path="show"/>
 			<span class="label">Flagged </span>
 		</span>
  </div> 
</c:if>  

<jsp:include page="../../include/notificationbox.jsp"/>
<div class="clearfloat"></div>

