  /* BEGIN PRE FUNCTIONS (initialization) ============================= */

	function init() {
		setupHelpPanels();
		initialize();
		

		new Control.Modal('link_linkhere',{
				containerClassName: 'linkbox',
        opacity: 0.2,
        afterOpen: getLinkUrls
        });

	}

	/* create html for info bubbles */	
	function makeOverlayHtml(event) {
		var html = createInfoWindowHtml(event) +  
			'<div class="infolinkbar linkbar"><a class="links" href="#" onclick="editEvent(' + event.id + ')" title="' + _msg_edit + '">edit</a>';  
	  if (event.hasDiscuss) {
			html += '<a class="links" href="'+ _basePath + 'event/discuss.htm?id=' + event.id + '" title="' + _msg_discuss + '">discuss</a>';
	  } else {
	  	html += '<span class="new_links"><a href="'+ _basePath + 'edit/discussedit.htm?id=' + event.id + '" title="' + _msg_newdiscuss + '">discuss</a></span>';
	  }
		html += '<a class="links" href="' + _basePath + 'event/changehistory.htm?id=' + event.id + '" title="' + _msg_changes + '">changes</a>';
		html += '<a class="links" href="' + _basePath + 'edit/flagevent.htm?id=' + event.id + '" title="' + _msg_flag + '">flag</a>' +
			'<a class="links" href="#" onclick="zoomTo(' + event.id + ')">zoom in</a>';
		html += '<br/></div>';
		return html;
	}
	
	
  /* END PRE FUNCTIONS (initialization) ============================= */

  /* BEGIN WHILE FUNCTIONS  ============================= */
	
	function adjustSidebarIE() {
		/* adjust the map */
		setMapExtent();
   	var top = document.getElementById("map").offsetTop;
   	var height = getHeight() - top - 44;
   	document.getElementById("results").style.height=height+"px";
   	/* DEBUG the following is a Kludge! for an IE 6 rendering problem argh!*/
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
		/*
		<iframe id="embeddedmap" 
			src='${basePath}search/embeddedsearch.htm?_where=london, england&_what="kensington palace"&_zoom=17&_maptype=1'
		  height="330" width="450" frameborder="0" scrolling="no">
		 	This browser doesn't support embedding a map.
		</iframe>
		<small><a href="linkurl">View A Larger Map</a>

		<iframe width="425" height="350" frameborder="0" scrolling="no" marginheight="0" marginwidth="0" src="http://maps.google.com/maps?f=l&amp;hl=en&amp;geocode=&amp;time=&amp;date=&amp;ttype=&amp;q=auto&amp;near=Landenberg,+PA+19350&amp;ie=UTF8&amp;ll=39.769005,-75.799818&amp;spn=0.086441,0.323157&amp;om=1&amp;output=embed&amp;s=AARTsJrScP0D4GgxBDy5dQvOHJ0k2HSqLA"></iframe>
		<br />
		<small><a href="http://maps.google.com/maps?f=l&amp;hl=en&amp;geocode=&amp;time=&amp;date=&amp;ttype=&amp;q=auto&amp;near=Landenberg,+PA+19350&amp;ie=UTF8&amp;ll=39.769005,-75.799818&amp;spn=0.086441,0.323157&amp;om=1&amp;source=embed" style="color:#0000FF;text-align:left">View Larger Map</a></small>
		
		*/
		/* substituted 'embeddedsearch.htm' for 'eventsearch.htm' */
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
		url = url.gsub('\'',' ');  /* mysql free text doesn't pay attention to this anyway! */ 
		url = url.escapeHTML();
		return url;
	}
	
	
	var hasQuery;
	function getLinkHereUrl() {
		hasQuery = false;
		var url = window.top.location;
		/* strip the out query string */
		var urlbase = new String(url);
		var idx = urlbase.indexOf('?');
		if (-1 != (idx)) {
			urlbase = urlbase.substring(0, idx);
		}
		var querystring = "";
		/* construct the url */
		if (!isEmpty($('linkHereEventId').value)) {
			querystring = append(querystring, '_id', $('linkHereEventId').value);
		} else {
			querystring = appendIfNotEmpty(querystring, '_where', $('where').value);
			querystring = appendIfNotEmpty(querystring, '_when', $('when').value);
			querystring = appendIfNotEmpty(querystring, '_what', $('what').value);
			querystring = appendIfNotEmpty(querystring, '_tag', $('userTag').value);
			querystring = appendIfTrue(querystring, '_timeoverlaps', $('excludeTimeRangeOverlaps1').checked);

			if (	$('limitWithinMapBounds1').checked || !isEmpty($('where').value) ) {
				querystring = append(querystring, '_withinMap', 'true');
				var boundingBox = map.getBounds();
				querystring = append(querystring, '_sw',  formatLL(boundingBox.getSouthWest()));
				querystring = append(querystring, '_ne',  formatLL(boundingBox.getNorthEast()));
			}
		}
		
		/* Only override zoom and center if the user has
     changed the map since the page was rendered (e.g. initialize() was called) 
     OR we are limiting the map within the given bounds */
		if ( $('limitWithinMapBounds1').checked || hasChangedMap()) {
			querystring = append(querystring, '_zoom', map.getZoom());
			querystring = append(querystring, '_ll', formatLL(map.getCenter()) );
		}
		querystring = append(querystring, '_maptype', getMapTypeIndex());			
			
		return urlbase + querystring;
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
		urltext += query + '=' + encodeURIComponent(value);
		return urltext;
	}
	
  /* END WHILEFUNCTIONS  ============================= */
