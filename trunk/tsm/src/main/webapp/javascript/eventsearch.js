/*
 * Copyright 2009 Time Space Map, LLC
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
  /* BEGIN PRE FUNCTIONS (initialization) ============================= */

  //constants
  var RESULTS_WIDTH = 320;
  var RESULTS_NUDGE = 53;
  var SIDEBAR_WIDTH = RESULTS_WIDTH;
  var HEADERBAR_HEIGHT = 147;
  var SEARCHBOX_TOP = 68;
    
  //global variables
  var _currResultsWidth = RESULTS_WIDTH;
  var _currResultsNudge = RESULTS_NUDGE;
  
	function init() {
		setupHelpPanels();
		_overlayManager = new SearchEventOverlayManager(new EventOverlayManager);
		_overlayManager.initialize();
		//initialize();
		new Control.Modal('link_linkhere', {
				className: 'linkbox',
				position: 'center',
        overlayOpacity: 0.9,
        afterOpen: getLinkUrls
        });
	}

  /* END PRE FUNCTIONS (initialization) ============================= */

  /* BEGIN WHILE FUNCTIONS  ============================= */
	
	
	function selectThis(element) {
		element.focus();
		element.select();
	}
	
	/** construct a URL like the linkhere url for getting the kml for this
	  * search */
	function exportKml() {
    var urltext = getLinkHereUrl();
    var kmlUrl = urltext.gsub('eventsearch.htm','conchartosearch.kml');
    document.location = kmlUrl + '&_kml=1';
	}
	
	//Populate the linkhere dialog box
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
		var embedUrltext = urltext.gsub('eventsearch.htm','embeddedsearch.htm');
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
		querystring = append(querystring, '_maptype', _mapManager.getMapTypeIndex());			
			
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
		if (!isEmpty(value)) {
			urltext = append(urltext, query, value);
		}
		return urltext;
	}
	
	function appendIfTrue(urltext, query, value) {
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
    urltext = urltext.gsub('%20','+');  /* I favor '+' instead of %20 for spaces */ 
		return urltext;
	}
	
	function hideSidebar() {
	  $('results').style.visibility = 'hidden';
	  $('sidebar').style.width = '10px';
	  _currResultsWidth = 10;

    //shorten the search bar
	  $('headerbar').style.background = 'none';
	  $('headerbar').style.height = '87px';
	  $('addbox').hide();
	  $('search_tagline').hide();
	  $('searchbox').style.top = '15px';
    $('searchpage').style.background = 'url(../images/short-backg.png) repeat-x';
    _currResultsNudge = RESULTS_NUDGE;

    //todo, add a logo to the right (doesn't work in IE 6)
		if (typeof document.body.style.maxHeight != "undefined") {
      new Insertion.Top('headerbar', '<a href="/"><img id="tmp-logo" alt="logo" src="../images/concharto-logo-sm-crop.png"/></a>');
		}
    

	  //adjust the map
    _overlayManager.adjustSidebar();
    map.checkResize();

	  //remove the "hide"
	  $('sidebarSize').childElements().each(function(e) {
	    e.remove();
	  });
	  
	  //show the "show"
	  new Insertion.Top('sidebarSize', '<a href="#" onclick="showSidebar(); return false;">show</a>');
	  
	  return false;
	}
	
	
	function showSidebar() {
    $('results').style.visibility = 'visible';
    $('sidebar').style.width = SIDEBAR_WIDTH + 'px';
    _currResultsWidth = RESULTS_WIDTH;

    //put the the search bar back
    $('headerbar').style.background = 'url(../images/title-search.png) no-repeat';
    $('headerbar').style.height = HEADERBAR_HEIGHT + 'px';
    $('addbox').show();
    $('search_tagline').show();
    $('searchbox').style.top = SEARCHBOX_TOP + 'px';
    $('searchpage').style.background = 'url(../images/search-backg.png) repeat-x';
    _currResultsNudge = RESULTS_NUDGE;

    //Remove a logo to the right (if we added it, it won't be added for IE6)
    if (null != $('tmp-logo')) {
      $('tmp-logo').remove();
    } 
    
    //adjust the map
    _overlayManager.adjustSidebar();
    map.checkResize();
    
    //remove the "show"
    $('sidebarSize').childElements().each(function(e) {
      e.remove();
    });
    
    //show the "hide"
    new Insertion.Top('sidebarSize', '<a href="#" onclick="hideSidebar(); return false;">hide sidebar</a>');
	}
  /* END WHILEFUNCTIONS  ============================= */
