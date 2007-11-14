<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
			<div>
	     	<span style="position:absolute; left:250px; top:30px;" >
	        <table class="searchbar">
	          <tr>
	            <td class="labelcell">
				    		<form:errors path="where"><span class="errorLabel"></form:errors>
		  	          Where 
				    		<form:errors path="where"></span></form:errors>
                <small>e.g., "Gettysburg, PA" </small><br/>
                <form:input path="where" size="22"/>
	            </td>
	            <td class="labelcell">
	              <form:errors path="when"><span class="errorLabel"></form:errors>
	              	When
	              <form:errors path="when"></span></form:errors>
	              <small>
	                e.g. "1962", "Oct 14, 1066", "1880-1886" 
	              </small><br/>
	              <form:input path="when" size="35"/>
	            </td>
	            <td class="labelcell">
	            	What
	              <small>
	                e.g. "Battle" 
	              </small><br/>
	                <form:input path="what" size="22" htmlEscape="true"/>
	            </td>
	          </tr>
	        </table>
	        
	        <span class="action"><input type="submit" name="Search" value="Search" /></span>
	        <span class="action"><input type="button" name="add" value="Add to the Map!" onclick="editEvent('')"/></span>
	        <c:if test="${fn:contains(roles, 'admin')}">
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
	        </c:if>
	      </span>
		  </div>