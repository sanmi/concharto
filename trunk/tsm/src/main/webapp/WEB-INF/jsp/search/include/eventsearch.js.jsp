
<script type="text/javascript">
//<![CDATA[

  <%-- BEGIN PRE FUNCTIONS (initialization) ============================= --%>

	<%-- create html for info bubbles --%>	
	function makeOverlayHtml(event) {
		var html = createInfoWindowHtml(event) +  
			'<br/><a class="links" href="#" onclick="editEvent(' + event.id + ')">edit</a> &nbsp;' +  
			'<a class="links" href="/edit/flagevent.htm?id=' + event.id + '">flag</a> &nbsp;' +
			'<a class="links" href="#" onclick="zoomTo(' + event.id + ')">zoom in</a> &nbsp;';
		if (event.hasUnresolvedFlags == 'true') {
			html += '<span class="errorLabel"><em>This event has been <a class="errorlinks" href="${basePath}edit/eventdetails.htm?id=' + event.id + '">flagged!</a></em></span>';
		} else {
			html += '<a class="links" href="${basePath}edit/eventdetails.htm?id=' + event.id + '">details</a>';
		}
		html += '<br/></div>';
		return html;
	}
	
	
  <%-- END PRE FUNCTIONS (initialization) ============================= --%>

  <%-- BEGIN WHILE FUNCTIONS  ============================= --%>
	
	function adjustSidebarIE() {
		<%-- adjust the map --%>
		setMapExtent();
    	var top = document.getElementById("map").offsetTop;
    	var height = getHeight() - top - 44;
    	document.getElementById("results").style.height=height+"px";
    	<%-- DEBUG the following is a Kludge! for an IE 6 rendering problem argh!--%>
    	document.getElementById("results").style.width = "320px"; 
	}
  <%-- END WHILEFUNCTIONS  ============================= --%>

  
//]]>
</script>
