<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>

<%
	//Redirect to the main page
  //NOTE: we are not using <c:redirect url="/home.htm" /> here because this tag also
  //encodes the URL which puts the ;jsessionid= into the URL.  Ugh.
	response.sendRedirect("/home.htm"); 
%>