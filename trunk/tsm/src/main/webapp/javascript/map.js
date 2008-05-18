	
	//some of the globals
	var map;
	var geocoder = null;
	var ACCURACY_TO_ZOOM = [4, 5, 7, 10, 11, 12, 13, 14, 15];
	var ZOOM_BOX_THRESHOLD = 10;
	var ZOOM_COUNTRY = 5;
	var ZOOM_USA = 4;
	var ZOOM_WORLD = 2;
	var LINE_COLOR = '#FF0000';
	var POLY_COLOR = '#f33f00';
	var LINE_WEIGHT = 4;
	var LINE_WEIGHT_HIGHLIGHT = 4;
	var POLY_COLOR_HIGHLIGHT = '#17ACFD';
	var LINE_COLOR_HIGHLIGHT = '#0000FF';
	var INFO_WIDTH = 450;
	var INFO_HEIGHT = 375;
  var MAP_TYPES = [G_NORMAL_MAP, G_SATELLITE_MAP, G_HYBRID_MAP, G_PHYSICAL_MAP];

  /* Create a base icon for all of our markers that specifies the
       shadow, icon dimensions, etc. */
  var _baseIcon = new GIcon();
  var _basePath;


	/** Objects ---------------- */
  function Point2D(x, y) {
  	this.x = x;
  	this.y = y;
  }

	function Intersection(status) {
    this.status = status;
    this.points = new Array();
 	}
	
	
	/* Main map initialization. Set up controls, scrolling and size
	 */
	function initializeMap(control) {
	
		if (GBrowserIsCompatible()) {
		  initializeMapVars();
			// map and its equipment
			map = new GMap2(document.getElementById("map"));
			//map.enableContinuousZoom();
			map.addControl(new GMapTypeControl(1));
			map.addMapType(G_PHYSICAL_MAP);
			if ((control == null) || (control == 'null')) {
				control = new GLargeMapControl();
			}
			map.addControl(control);
			
			map.enableDoubleClickZoom();
			map.enableScrollWheelZoom();
			geocoder = new GClientGeocoder();
	
			//prevent scrolling the window when the mouse is inside of the map
			GEvent.addDomListener(map.getContainer(), "DOMMouseScroll", wheelevent);
			map.getContainer().onmousewheel = wheelevent; 
			
			//map.setCenter(new GLatLng(40.879721,-76.998322),10);  //la la land, PA
			setMapExtent();
			map.checkResize(); //tell the map that we have resized it
			showDefault(); 
		}
	}

  //Initialize global variables
  function initializeMapVars() {
    _basePath = $('basePath').value;
    _baseIcon.shadow = _basePath+"images/icons/00shadow.png";
    _baseIcon.iconSize = new GSize(20, 32);
    _baseIcon.shadowSize = new GSize(40, 35);
    _baseIcon.iconAnchor = new GPoint(10, 32);
    _baseIcon.infoWindowAnchor = new GPoint(12, 3);

  }
   
	
	/* default map coordinates */
	function showDefault() {		
		diagonal = new GPolyline([
  		new GLatLng(86,-180),
  		new GLatLng(-86,180)
		]);
		var bounds = diagonal.getBounds();
		map.setCenter(bounds.getCenter());  
		map.setZoom(map.getBoundsZoomLevel(bounds));
	}
	
	function setMapExtent() {
    	var top = document.getElementById("map").offsetTop;
    	var height = getHeight() - top - 28;
    	document.getElementById("map").style.height=height+"px";
	}
	
	function getHeight() {
      var myWidth = 0, myHeight = 0;
      if( typeof( window.innerWidth ) == 'number' ) {
        //Non-IE
        myWidth = window.innerWidth;
        myHeight = window.innerHeight;
      } else if( document.documentElement && ( document.documentElement.clientWidth || document.documentElement.clientHeight ) ) {
        //IE 6+ in 'standards compliant mode'
        myWidth = document.documentElement.clientWidth;
        myHeight = document.documentElement.clientHeight;
      } else if( document.body && ( document.body.clientWidth || document.body.clientHeight ) ) {
        //IE 4 compatible
        myWidth = document.body.clientWidth;
        myHeight = document.body.clientHeight;
      }
      return myHeight;
    }
    
	function getWidth() {
      var myWidth = 0;
      if( typeof( window.innerWidth ) == 'number' ) {
        //Non-IE
        myWidth = window.innerWidth;
      } else if( document.documentElement && ( document.documentElement.clientWidth || document.documentElement.clientHeight ) ) {
        //IE 6+ in 'standards compliant mode'
        myWidth = document.documentElement.clientWidth;
      } else if( document.body && ( document.body.clientWidth || document.body.clientHeight ) ) {
        //IE 4 compatible
        myWidth = document.body.clientWidth;
      }
      return myWidth;
    }
	
	function createInfoWindowHtml(event, width /* optional */, height /* optional */) {
		if (width==null || width=='null' || width=='') { width = INFO_WIDTH};
		if (height==null || height=='null' || height=='') {
			height = 'max-height:' + INFO_HEIGHT + ';';
		} else {
			height = 'height:' + height + 'px;'
		}
	  var html = '<div class="result inforesult wikitext" style="width:' + width +'px;'+ height +'margin-bottom:10px">';
				if (event.flagged) {
					html += '<a target="_top" class="errorLabel" href="/event/changehistory.htm?id=' + event.id +'">Flagged! </a>'; 
				}
	  		html += '<span class="summary">' + event.summary.escapeHTML() +'</span><br/>' + 
	   		'<span class="when">' + event.when + '</span><br/>' + 
				'<span class="where">' + event.where.escapeHTML();
				if (!isEmpty(event.accy)) {
					 html += ' (Accuracy: ' + _msg_accy[event.accy] + ')'; 
				}
				html += '</span><br/>' + event.description;
				
				var tags = event.tags.split( "," );
				var taglink = new Array();
				tags.each( function(tag, index){
				  //encode ant apostrophes in the tag because that will mess up the call from HTML
				  //we will have to decode them later in goToTag()
				  var encodedtag = tag.gsub('\'', '%27');
	        //embedded search is wierd with following tags using document.location so we will use a regular href
	        if (-1 == document.location.pathname.indexOf('embedded')) {
	          taglink[index] = '<a target="_top" href="" onclick="goToTag(\'' + encodedtag + '\'); return false;">' + tag +'</a>';
	        } else {
	          //this is embedded
	          taglink[index] = '<a target="_top" href="'
	            + '/search/eventsearch.htm?_tag='+ encodedtag + '&_maptype=' + getMapTypeIndex()
	            + '">'+ tag +'</a>';
	        }
				});
        html += '<div class="usertags"><b>Tags: </b>' + taglink.join(', ') + '</div>';   
				html += '<span class="source"><b>Source: </b>' + event.source + '</span>';
				html += '</div>';
				
	   return html;
	}	
	
	/* go to the tag link, but use the current map type */
	function goToTag(tag) {
    // UGH.  Some tags have single quotes which messes up the call when the HTML uses onclick.  However 
    // we need those single quotes for the search, since it is a valid code in the URL and the server doesn't
    // decode %27 unescape single quotes
    tag = tag.gsub('%27', '\'');
    var tag = encodeURIComponent(tag);
	  document.location = '/search/eventsearch.htm?_tag='+ tag + '&_maptype=' + getMapTypeIndex();
	}
	
	function getMapTypeIndex() {
		var mapTypeIndex = 0;
				
		for (i=0; i<MAP_TYPES.length; i++) {
			if (MAP_TYPES[i].getName() == map.getCurrentMapType().getName()) {
				mapTypeIndex = i;
			}
		}
		return mapTypeIndex;
	}
		
  function gLatLngToJSON( point ) {
		return '{"gtype":"point","lat":' + point.lat() + ',"lng":' + point.lng() + '}';
	}
	
	function markersToJSON(markers, geometryType) {
		var str = '{"gtype":"' + geometryType + '","line":[';
		for (var i=0; i<markers.length; i++) {
			str += gLatLngToJSON( markers[i].getPoint());
			if (i != markers.length-1) {
				str += ',';
			}
		}
		str +=']}';
		return str;
	}
	
	///prevent page scroll
	function wheelevent(e)
	{
			if (!e){
				e = window.event
			}
			if (e.preventDefault){
				e.preventDefault()
			}
			e.returnValue = false;
	}
				
	function createOverlays(events, excludeEventId) {
		for (var i =0; i<events.length; i++) {
			var event = events[i];
			if ((excludeEventId === null) || (event.id != excludeEventId)) {
				if (event.gtype == 'point') {
				  createMarker(event, events.length);
				} else if ((event.gtype == 'line') || (event.gtype == 'polygon')) {
					createPoly(event, events.length);					
				}
			}
		} 
	}
	
	function fitToPoly(poly, force /* optional */) {
	  if (force == null) force = false;
		var bounds = poly.getBounds();
		var zoom = map.getBoundsZoomLevel(bounds);
		if ((zoom > map.getZoom()) || (force == true)) {
			map.setZoom(zoom);
		}
	}
	
	function newPoly(points, geometryType, weight /* optional */, 
										lineColor /* optional */, polyColor /* optional */) {
		if (weight == undefined) {
			weight = LINE_WEIGHT;
		}
		if (lineColor == undefined) {
			lineColor = LINE_COLOR;
		}
		if (polyColor == undefined) {
			polyColor = POLY_COLOR;
		}
		if (points.length > 0) {
			if ((geometryType == 'line')){
				var encodedPolyline = new GPolyline.fromEncoded({
				    color: lineColor,
				    weight: weight,
				    points: encodePoly(points).points,
				    levels: encodePoly(points).levels,
				    zoomFactor: 32,
				    numLevels: 4
				});
				return encodedPolyline;
			} else if (geometryType == 'polygon') {
				return new GPolygon(points, polyColor, weight, .8, lineColor, .25);
			}
		}
		return null;
	}
	
	function return2br(dataStr) {
		return dataStr.gsub('(\r\n|[\r\n])', '<br/>');
  }
  
  function findClosestVertex(point, overlay) {
		var minDistance = 9999999;
		var closest = 0;
		for (var i=0; i<overlay.getVertexCount(); i++) {
			distance = point.distanceFrom(overlay.getVertex(i));
			if (distance < minDistance) {
				closest = i;
				minDistance = distance;
			}
		}
		return overlay.getVertex(closest);
	}
		
  
  function isEmpty(value) {
		return ((null == value) || ('' == value));
	}

/* was GooglePack - from  http://www.polyarc.us/google/packer.js */ 	
function encodePoly(poly)
{
	var i,j,k,l,u,v,w,z;

	w=[];
	z=[];

	u={x:0,y:0};
	v={x:0,y:0};

	for (i=0;poly[i];i++)
	{
		w[i]="B";

		u.x=(poly[i].x*1e5)<<1;
		u.y=(poly[i].y*1e5)<<1;

		v.x=u.x-v.x;
		v.y=u.y-v.y;

		v.x^=v.x>>31;
		v.y^=v.y>>31;

		z.push(v.y.toString(32));
		z.push(v.x.toString(32));

		v.x=u.x;
		v.y=u.y;
	}

	for (i=0;z[i];i++)
	{
		k=[];

		for (j=0;z[i].charAt(j);j++)
		{
		k[j]=String.fromCharCode(parseInt(z[i].charAt(j),32)+(j ? 95 : 63));
		}

		z[i]=k.reverse().join("");
	}

	w=w.join("");
	z=z.join("");

	return {levels:w,points:z};
}

/*****
*   intersectLineLine.  From http://www.kevlindev.com/gui/math/intersection/
*****/
function intersectLineLine(a1, a2, b1, b2) {
    var result;
    
    var ua_t = (b2.x - b1.x) * (a1.y - b1.y) - (b2.y - b1.y) * (a1.x - b1.x);
    var ub_t = (a2.x - a1.x) * (a1.y - b1.y) - (a2.y - a1.y) * (a1.x - b1.x);
    var u_b  = (b2.y - b1.y) * (a2.x - a1.x) - (b2.x - b1.x) * (a2.y - a1.y);

    if ( u_b != 0 ) {
        var ua = ua_t / u_b;
        var ub = ub_t / u_b;

        if ( 0 <= ua && ua <= 1 && 0 <= ub && ub <= 1 ) {
            result = new Intersection("Intersection");
            result.points.push(
                new Point2D(
                    a1.x + ua * (a2.x - a1.x),
                    a1.y + ua * (a2.y - a1.y)
                )
            );
        } else {
            result = new Intersection("No Intersection");
        }
    } else {
        if ( ua_t == 0 || ub_t == 0 ) {
            result = new Intersection("Coincident");
        } else {
            result = new Intersection("Parallel");
        }
    }

    return result;
};	
