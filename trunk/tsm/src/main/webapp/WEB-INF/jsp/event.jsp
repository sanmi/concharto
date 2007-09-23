<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tsm"%>

<tsm:page title="Event">
	<jsp:attribute name="head">
		<script
			src="http://maps.google.com/maps?file=api&amp;v=2.x&amp;key=ABQIAAAA1DZDDhaKApTfIDHGfvo13hQHaMf-gMmgKgj1cacwLLvRJWUPcRTWzCG3PTSVLKG0PgyzHQthDg5BUw"
			type="text/javascript">
		</script>		
	</jsp:attribute>
	<jsp:attribute name="script">addevent.js</jsp:attribute>
	<jsp:attribute name="bodyattr">onload="initialize()" onunload="GUnload();" class="mapedit"</jsp:attribute>

	<jsp:body>
	   <div id="sidebar">
	     <div id="novel">
	       <form:form name="event" id="eventForm" commandName="event" method="post">
			    <form:hidden path="id"/>
	       	<input type="hidden" name="lat"/>
	       	<input type="hidden" name="lng"/>
	
	         <table>
	           <tr>
	           <tr>
	             <td class="labelcell">Summary <br/>
	             <input name="summary" size="50"/></td>
	           </tr>
	           <tr>
	             <td class="labelcell">Where
	                 <small>e.g., "gettysburg, pa" </small><br/>
	                 <input  name="addr" size="50"/>
	                 <br/>
	                 <input  type="button" name="Find" value="Add to map" onclick="showAddress(document.event.addr.value); return false"/>             
	                 <small id="tip"><b>Tip:</b> drag and drop the lollypop!</small>
	             </td>
	           </tr>
	           <tr>
	             <td class="labelcell">
	               When
	               <small>
	                 e.g. "1962" or "March, 1064" or "1880 - 1886" <a href="#">hints</a>
	               </small><br/>
	               <input name="timerange" size="50"/>
	             </td>
	           </tr>
	           <tr>
	             <td class="labelcell">Description<br/>
	             <textarea rows="5" cols="38"></textarea></td>
	           </tr>
	           <tr>
	             <td class="labelcell">Tags<br/>
	             <input name="tags" size="50"/></td>
	           </tr>
	           <tr>
	             <td class="labelcell">Source 
	             <small><a id="selectedMiniTab" href="#">URL</a><a id="unselectedMiniTab" href="#">Publication</a><a id="unselectedMiniTab" href="#">Other</a></small><br/>
	             <input name="source" size="50"/> 
	             
	           </td> 
	             
	           </tr>
	         </table>
	         <input type="button" name="Save" value="Save This Event" onclick="saveEvent(); return false"/>
	       </form:form>
	       
				<form:form commandName="event" method="post">
			    <form:hidden path="id"/>
			    <table>
			        <tr>
			            <td>Summary:</td>
			            <td><form:input path="summary"/></td>
			            <td></td>
			        </tr>
			        <tr>
			            <td>Description:</td>
			            <td><form:input path="description"/></td>
			            <td></td>
			        </tr>
			        <tr>
			            <td>Tags:</td>
			            <td><form:input path="tags"/></td>
			            <td></td>
			        </tr>
			        <tr>
			            <td colspan="3">
			                <input type="submit" value="Save Changes"/>
			            </td>
			        </tr>
			    </table>
				</form:form>
	     </div>

	   <div id="map"  style="position:absolute; height:90%;width:80%">
	     Map coming...
	     <noscript>
	       <p>
	         JavaScript must be enabled to get the map.
	       </p>
	     </noscript>
	   </div>
	 </div>
	
	</jsp:body>
</tsm:page>

