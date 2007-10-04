<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1"%>

<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

  <div id="head">
		<table>
    	<tr>
      	<td width="125">
			    <img src="<%=path%>/images/logo.png" alt="" />
        </td>
        <td>
			    <img src="<%=path%>/images/banner-timeline.png" alt="" width="506" height="63" />
        	<span style="position:relative;  top:-50px;" ></span>
        </td>
      </tr>
    </table>    	
  </div>
