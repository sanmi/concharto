<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tsm"%>


<tsm:page title="Feedback">
	<jsp:attribute name="bodyattr">onload="document.getElementById('feedbackForm').name.focus()"</jsp:attribute>
	<jsp:attribute name="stylesheet">textpanel.css,header.css</jsp:attribute>

	<jsp:body>
	  <form:form id="feedbackForm" name="feedback" commandName="feedback"> 
	  	<div class="textpanel">
				<h2>Feedback</h2>
				<p>Please let us know what you think about Concharto.  Use this form to ask a question, report bugs, make suggestions or leave a comment.</p>

				<div class="inputcell">
		        <span class="inputlabel">Name</span> 
		        <span class="errorLabel"><form:errors path="name" element="div"/></span>
		        <br/>
		        <form:input cssClass="textInput" path="name"/>
   		   </div>
				<div class="inputcell">
		        <span class="inputlabel">Email</span> 
		        <span class="errorLabel"><form:errors path="email" element="div"/></span>
		        <br/>
		        <form:input cssClass="textInput" path="email"/>
   		   </div>
				<div class="inputcell">
		        <span class="inputlabel">Subject</span> 
		        <span class="errorLabel"><form:errors path="subject" element="div"/></span>
		        <br/>
		        <form:select path="subject">
	            <form:option value="Question"/>
	            <form:option value="Suggestion"/>
	            <form:option value="Bug"/>
	            <form:option value="Comment"/>
		        </form:select>
   		   </div>
				<div class="inputcell">
		        <span class="inputlabel">Comment</span> 
		        <span class="errorLabel"><form:errors path="body" element="div"/></span>
		        <br/>
		        <form:textarea cssClass="textArea" rows="5" path="body"/>
   		   </div>
					
				<div class="inputcell">
				  <input type="submit" value="Submit"/>
				</div>
		  </div>
	  </form:form>
	  
	</jsp:body>
</tsm:page>

