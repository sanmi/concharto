
<script type="text/javascript">
//<![CDATA[

  <%-- BEGIN PRE FUNCTIONS (initialization) ============================= --%>

	function init() {
		initialize();
		
		setupHelpPanels();
		
		new Control.Modal('link_linkhere',{
				containerClassName: 'linkbox',
        opacity: 0.2,
        afterOpen: getLinkUrls
        });
	}

	<%-- create html for info bubbles --%>	
	function makeOverlayHtml(event) {
		var html = createInfoWindowHtml(event) +  
			'<div class="infolinkbar linkbar"><a class="links" href="#" onclick="editEvent(' + event.id + ')">edit</a>' +  
	    '<a class="links" href="/event/discuss.htm?id=' + event.id + '">discuss</a>'; 
		if (event.hasUnresolvedFlags == 'true') {
			html += '<span class="errorLabel"><em>This event has been <a class="errorlinks" href="${basePath}event/changehistory.htm?id=' + event.id + '">flagged!</a></em></span>';
		} else {
			html += '<a class="links" href="${basePath}event/changehistory.htm?id=' + event.id + '">changes</a>';
		}
		html += '<a class="links" href="/edit/flagevent.htm?id=' + event.id + '">flag</a>' +
			'<a class="links" href="#" onclick="zoomTo(' + event.id + ')">zoom in</a>';
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
	
	function selectThis(element) {
		element.focus();
		element.select();
	}
	
	function getLinkUrls() {
		$('linkhere_url').value = getLinkHereUrl();
		$('embedmap_html').value = getEmbedMapHtml();
	}
	
	function getEmbedMapHtml() {
		<%--
		<iframe id="embeddedmap" 
			src='${basePath}search/embeddedsearch.htm?_where=london, england&_what="kensington palace"&_zoom=17&_maptype=1'
		  height="330" width="450" frameborder="0" scrolling="no">
		 	This browser doesn't support embedding a map.
		</iframe>
		<small><a href="linkurl">View A Larger Map</a>

		<iframe width="425" height="350" frameborder="0" scrolling="no" marginheight="0" marginwidth="0" src="http://maps.google.com/maps?f=l&amp;hl=en&amp;geocode=&amp;time=&amp;date=&amp;ttype=&amp;q=auto&amp;near=Landenberg,+PA+19350&amp;ie=UTF8&amp;ll=39.769005,-75.799818&amp;spn=0.086441,0.323157&amp;om=1&amp;output=embed&amp;s=AARTsJrScP0D4GgxBDy5dQvOHJ0k2HSqLA"></iframe>
		<br />
		<small><a href="http://maps.google.com/maps?f=l&amp;hl=en&amp;geocode=&amp;time=&amp;date=&amp;ttype=&amp;q=auto&amp;near=Landenberg,+PA+19350&amp;ie=UTF8&amp;ll=39.769005,-75.799818&amp;spn=0.086441,0.323157&amp;om=1&amp;source=embed" style="color:#0000FF;text-align:left">View Larger Map</a></small>
		
		--%>
		<%-- substituted 'embeddedsearch.htm' for 'eventsearch.htm' --%>
		var urltext = getLinkHereUrl();
		embedUrltext = urltext.gsub('eventsearch.htm','embeddedsearch.htm');
		embedUrltext = append(embedUrltext, '_embed', 'true');
		embedUrltext = cleanUrl(embedUrltext);
		return '<iframe src=\'' + 
			embedUrltext +
			'\' height="330" width="450" frameborder="0" scrolling="no"></iframe> <br/>' + 
			'<small><a href=\'' + 
			cleanUrl(urltext) + 
			'\'>View A Larger Map</a></small>';
	}
	
	function cleanUrl(url) {
		url = url.gsub('\'',' ');  <%-- mysql free text doesn't pay attention to this anyway! --%> 
		url = url.escapeHTML();
		return url;
	}
	
	var hasQuery;
	function getLinkHereUrl() {
		hasQuery = false;
		var url = window.top.location;
		<%-- strip the out query string --%>
		var urltext = new String(url);
		var idx = urltext.indexOf('?');
		if (-1 != (idx)) {
			urltext = urltext.substring(0, idx);
		}
		<%-- construct the url --%>
		if (!isEmpty($('linkHereEventId').value)) {
			urltext = append(urltext, '_id', $('linkHereEventId').value);
		} else {
			urltext = appendIfNotEmpty(urltext, '_where', $('where').value);
			urltext = appendIfNotEmpty(urltext, '_when', $('when').value);
			urltext = appendIfNotEmpty(urltext, '_what', $('what').value);
			urltext = appendIfTrue(urltext, '_timeoverlaps', $('excludeTimeRangeOverlaps1').checked);

			if (	$('limitWithinMapBounds1').checked || !isEmpty($('where').value) ) {
				urltext = append(urltext, '_withinMap', 'true');
				var boundingBox = map.getBounds();
				urltext = append(urltext, '_sw',  formatLL(boundingBox.getSouthWest()));
				urltext = append(urltext, '_ne',  formatLL(boundingBox.getNorthEast()));
			}
		}
		
		<%-- Only override zoom and center if the user has
     changed the map since the page was rendered (e.g. initialize() was called) 
     OR we are limiting the map within the given bounds --%>
		if ( $('limitWithinMapBounds1').checked || hasChangedMap()) {
			urltext = append(urltext, '_zoom', map.getZoom());
			urltext = append(urltext, '_ll', formatLL(map.getCenter()) );
		}
		urltext = append(urltext, '_maptype', getMapTypeIndex());			
			
		<%-- get rid of spaces and '#' --%>
		urltext = urltext.gsub('#','');
		urltext = urltext.gsub(' ','+');
		return urltext;
	}
	
	function hasChangedMap() {
		return 	(_initialZoom != map.getZoom()) ||
			(_initialCenter.lat() != map.getCenter().lat()) ||
			(_initialCenter.lng() != map.getCenter().lng());
	}

	function formatLL(ll) {
		return ll.lat().toFixed(6) + ',' + ll.lng().toFixed(6);
	}
	
	function appendIfNotEmpty(urltext, query, value) {
		//alert(query + ', ' + value + ', ' + isEmpty(value));
		if (!isEmpty(value)) {
			urltext = append(urltext, query, value);
		}
		return urltext;
	}
	
	function appendIfTrue(urltext, query, value) {
		//alert(query + ', ' + value );
		if (value == true) {
			urltext = append(urltext, query, value);
		}
		return urltext;
	}
	
	function append(urltext, query, value) {
		if (!hasQuery) {
			urltext += '?';
			hasQuery = true;
		} else {
			urltext += '&';
		}
		urltext += query + '=' + value;
		return urltext;
	}
	
  <%-- END WHILEFUNCTIONS  ============================= --%>

  
//]]>
</script>
