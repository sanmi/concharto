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
var map; /* TODO refactor to remove map as a global variable */
var geocoder = null; /* TODO refactor to remove geocoder as a global variable */

var _overlayManager = new EventOverlayManager();
var _mapManager = new MapManager();

//variables
var _baseIcon = new GIcon();
var _basePath;

//Initialize global variables
function initializeMapVars() {
  _basePath = $('basePath').value;
  _baseIcon.shadow = _basePath+"images/icons/00shadow.png";
  _baseIcon.iconSize = new GSize(20, 32);
  _baseIcon.shadowSize = new GSize(40, 35);
  _baseIcon.iconAnchor = new GPoint(10, 32);
  _baseIcon.infoWindowAnchor = new GPoint(12, 3);
}

/** Manages creating, display and events for all points and overlays */
function EventOverlayManager() {
  //constants
  this.LINE_COLOR = '#FF0000';
  this.POLY_COLOR = '#f33f00';
  this.LINE_WEIGHT = 4;
  this.LINE_WEIGHT_HIGHLIGHT = 4;
  this.POLY_COLOR_HIGHLIGHT = '#17ACFD';
  this.LINE_COLOR_HIGHLIGHT = '#0000FF';
  this.INFO_WIDTH = 450;
  this.INFO_HEIGHT = 375;
  this.ALPHA_CHARS = 'ABCDEFGHIJKLMNOPQRSTUVWXYZ';
  
    /* global array and index for dealing with overlays */
  var _overlays = [];
  var _overlayIndex = 0;
  var _fitToPolygon = [];
  var _fitToPolygonIndex = 0;
  var _initialZoom;
  var _initialCenter;
  
  /** get the overlay array
    * @return an array containing overlayItem objects */ 
  this.getOverlays = function() {
    return _overlays; 
  };
  
  /** clear all overlay storage */
  this.clearOverlays = function() { 
    _overlays = [];
    _overlayIndex = 0;
    _fitToPolygon = [];
    _fitToPolygonIndex = 0;
  };
  
  /** get the fitToPolygon state */
  this.getFitToPolygon = function() {return _fitToPolygon; };

  //abstract - please implement
  this.highlightSidebarItem = function(overlayItem) { /*do nothing*/};
  
  //default implementation, displays a simple info window
  this.makeOverlayHtml = function(index, event, totalEvents) {
    return this.createInfoWindowHtml(index, event, 350, 150);
  }
  
  //default implementation, please override
  this.limitWithinMapBounds = function() {
    return false;
  };

  /** Main initialization function */  
  this.initialize = function() {
  
    this.clearOverlays();
  
    /* to unhighlight polygons if there are any */
    this.hideZoomedPolygons(3);

    this.infoWindowListener();    

    //listeners for hiding polygons when you are zoomed way in
    GEvent.addListener(map, "zoomend", function() {
      _overlayManager.hideZoomedPolygons(1);
    });
    GEvent.addListener(map, "moveend", function() {
      _overlayManager.hideZoomedPolygons(2);
    });
  }

  /** Listener to unhighlight overlays when the user clicks away from one 
   *  Override this for asynchronous behavior (e.g. maplets) 
   */
  this.infoWindowListener = function() {
    GEvent.addListener(map, "infowindowclose", function() {
      //only unhighlight if there are no windows open
      if (this.getInfoWindow().isHidden()) {
        _overlayManager.unhighlightOverlay();
      }
    });
  }
  
  this.createOverlays = function(events, excludeEventId) {
    for (var i =0; i<events.length; i++) {
      var event = events[i];
      if ((excludeEventId === null) || (event.id != excludeEventId)) {
        if (event.gtype == 'point') {
          this.createMarker(i, event, events.length);
        } else if ((event.gtype == 'line') || (event.gtype == 'polygon')) {
          this.createPoly(i, event, events.length);   
        }
      }
    } 
    this.hideZoomedPolygons(3);
  }

  /* Don't show polygons when we are zoomed so far in that we can't see them 
   * @param from, a variable for debugging event triggers.  Normally not used. 
   */
  this.hideZoomedPolygons = function(from) {
    _overlays.each( function(item, index) {
      bounds = map.getBounds();
      if (item.type == 'polygon') {
        var overlay = item.overlay;
        var hide = true;
        var vertexes = item.points;       
        var vertexCount = vertexes.length;
        for (var i = 0; i < vertexCount; i++) {
          var vertex = vertexes[i];
          //if vertex within the map OR
          //if a line between this vertex and the last intersects the map
          if ((bounds.contains(vertex)) ||
              ((i+1 < vertexCount) && _overlayManager.intersectsMap(bounds, vertex, vertexes[i+1]))) 
          {
            hide = false;
            break;
          } 
        }
        if (hide) {
          //ok, there are no vertexes within the map, so we should hide this overlay
          map.removeOverlay(overlay);
        } else {
          map.addOverlay(overlay);
        }
      }
    });
  }

  this.gLatLngToJSON = function( point ) {
    return '{"gtype":"point","lat":' + point.lat() + ',"lng":' + point.lng() + '}';
  }
  
  this.markersToJSON = function(markers, geometryType) {
    var str = '{"gtype":"' + geometryType + '","line":[';
    for (var i=0; i<markers.length; i++) {
      str += this.gLatLngToJSON( markers[i].getPoint());
      if (i != markers.length-1) {
        str += ',';
      }
    }
    str +=']}';
    return str;
  }

  this.polyToJSON = function(poly, geometryType) {
    var str = '{"gtype":"' + geometryType + '","line":[';
    for (var i=0; i<poly.getVertexCount(); i++) {
      str += this.gLatLngToJSON( poly.getVertex(i));
      if (i != poly.length-1) {
        str += ',';
      }
    }
    str +=']}';
    return str;
  }

  /* called by createOverlay */
  this.createPoly = function(index, event, totalEvents) {
    var points = [];
    var line = event.geom.line;
    
    for (i=0; i<line.length; i++) {
      var vertex = new GLatLng(line[i].lat, line[i].lng);
      points.push(vertex);
      /*if the user specified "where", then we should not try to fit the map to all
        polygons or lines in the search results, otherwise very large polys or lines could force
        the map to be zoomed way out (e.g. an explorer path that crosses the ocean, but passes 
        nearby New York City)
      */ 
      if (isEmpty($('where').value)) {
        this.updateFitToPolygon(vertex);
      } 
    }
    var poly = this.newPoly(points, event.geom.gtype);
    if (poly) {
      /* record so the user can click on the sidebar and see a popup in the map */
      var html = this.makeOverlayHtml(index, event, totalEvents);
      var overlayItem = this.recordOverlay(poly, html, event.gtype, event.id, points)
      this.addOverlayClickListener(overlayItem);
      //NOTE: if the overlay is a poly it will be added to the map when/if the hideZoomedPolygons() 
      //decides it is appropriate
      if (event.gtype == 'line') {
        map.addOverlay(poly);
      } 
    }
  }

  /* Add to a global polygon object that is used to fit the map to the
   * search results.   */
  this.updateFitToPolygon = function(gll) {
    if ((this.limitWithinMapBounds() == false) ) {
      _fitToPolygon[_fitToPolygonIndex] = gll;
      _fitToPolygonIndex++;
    }
  }

  /* construct the marker icon */
  this.getMarkerIcon = function() {
    /* Create a lettered icon for this point using our icon class */
    var letter = String.fromCharCode("A".charCodeAt(0) + _overlayIndex);
    var letteredIcon = new GIcon(_baseIcon);
    letteredIcon.image = _basePath+"images/icons/" + letter + ".png";
    return letteredIcon;
  }
  
  /* called by createOverlay */
  this.createMarker = function(index, event, totalEvents) { 
    this.updateFitToPolygon(new GLatLng(event.geom.lat, event.geom.lng));
    markerIcon = this.getMarkerIcon();
    
    /* Set up our GMarkerOptions object */
    var markerOptions = { icon:markerIcon };
    var marker = new GMarker(new GLatLng(event.geom.lat, event.geom.lng), markerOptions);
    var html = this.makeOverlayHtml(index, event, totalEvents);
    marker.bindInfoWindow(html);
    map.addOverlay(marker);
    
    /* record so the user can click on the sidebar and see a popup in the map */
    var overlayItem = this.recordOverlay( marker, html, "point", event.id);
    this.addOverlayClickListener(overlayItem);
  }  
  
  /* record overlay and html so we can pop up a window when the user clicks
     on info in the sidebar s*/
  this.recordOverlay = function( overlay, html, type, id, points) {
    var item = new overlayItem(overlay, html, type, id, points);
    _overlays[_overlayIndex] = item;
    _overlayIndex++;
    return item;
  }

  /* Listener for polys and lines */
  //FSM TODO event listeners probably should be in a different place because they need 
  //to access "_overlayManager." rather than "this."  Not sure what to do about it
  this.addOverlayClickListener = function(overlayItem) {
    //first turn stuff off
    GEvent.addListener(overlayItem.overlay, "click", function(point) {
      //for points, we only want to highlight the sidebar item. the
      //infowindow is handled elsewhere
      _overlayManager.unhighlightOverlay();
      if (overlayItem.type != 'point') {
        map.openInfoWindowHtml(point, overlayItem.html);
        //NOTE: order is important here.  The highlight must come AFTER the openInfoWindowHtml
        //otherwise, events will fire and the overlay will be hidden after we show it
        _overlayManager.highlightOverlay(overlayItem);
      }
      _overlayManager.highlightSidebarItem(overlayItem); 
    });

  }

  /* Highlight the overlay by changing its color */
  this.highlightOverlay = function(overlayItem) {
    var newOverlay = this.redrawOverlay(overlayItem, _overlayManager.LINE_WEIGHT_HIGHLIGHT, _overlayManager.LINE_COLOR_HIGHLIGHT, _overlayManager.POLY_COLOR_HIGHLIGHT);
    overlayItem.isHighlighted = true;
    overlayItem.overlay = newOverlay;
    this.addOverlayClickListener(overlayItem);
  }
  
  this.redrawOverlay = function(overlayItem, weight /* optional */, lineColor /* optional */, polyColor /* optional */) {
    var overlay = overlayItem.overlay;
    map.removeOverlay(overlay);
    var points = overlayItem.points;
    overlay = this.newPoly(points, overlayItem.type, weight, lineColor, polyColor);
    map.addOverlay(overlay);
    return overlay;
  }
  
  this.unhighlightOverlay = function() {
    for (var ov=0; ov<_overlays.length; ov++) {
      if (_overlays[ov].isHighlighted == true) {
        _overlays[ov].overlay = this.redrawOverlay(_overlays[ov], _overlayManager.LINE_WEIGHT, _overlayManager.LINE_COLOR, _overlayManager.POLY_COLOR);
        _overlays[ov].isHighlighted = false;
        this.addOverlayClickListener(_overlays[ov]);
      } 
    }
  }
  
  /* Create a new polygon */
  this.newPoly = function(points, geometryType, weight /* optional */, 
                    lineColor /* optional */, polyColor /* optional */) {
    if (weight == undefined) {
      weight = this.LINE_WEIGHT;
    }
    if (lineColor == undefined) {
      lineColor = this.LINE_COLOR;
    }
    if (polyColor == undefined) {
      polyColor = this.POLY_COLOR;
    }
    //adjust closure of the poly based on geomType
    points = this.setPolyClosure(geometryType, points);
    if (points != null) { 
      if ((geometryType == 'line')){
        var encodedPolyline = new GPolyline.fromEncoded({
            color: lineColor,
            weight: weight,
            points: this.encodePoly(points).points,
            levels: this.encodePoly(points).levels,
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

  /** if this is a poly, the last point should also be the first
    * if this is a line, the last point should not be the first */
  this.setPolyClosure = function(geometryType, points) {
    if ((geometryType == 'line')){
      if (this.GLatLngEquals(points[0], points[points.length-1])) {
        //remove the closure
        points.pop(points[points.length-1]);
      } 
    } else if (geometryType == 'polygon') {
      if (!this.GLatLngEquals(points[0], points[points.length-1])) {
        //add missing closure 
        points.push(points[0]);
      }
    }
    return points;
  }
  
  this.GLatLngEquals = function(g1, g2) {
    if ((g1 == null) || (g2 == null)) {
      return false;
    } else { 
      return ((g1.lat() == g2.lat()) && (g1.lng() == g1.lng()));
    } 
  }
  
  /* returns true if a line from v1 to v2 intersects the current map bounds at any point */
  this.intersectsMap = function(bounds, v0, v1) {
    var testLine = new Line(v0, v1);
    var sw = bounds.getSouthWest();
    var ne = bounds.getNorthEast()
    var nw = new GLatLng(ne.lat(), sw.lng());
    var se = new GLatLng(sw.lat(), ne.lng());
    var diag1 = new Line(sw, ne);
    var diag2 = new Line(nw, se);
    //if the line intersects either diagonal line, then it intersects the rectangle
    return this.intersectsLine(testLine, diag1) || this.intersectsLine(testLine, diag2);  
  }
  
  /* returns true if line1 intersects line2 
   * from: http://en.wikipedia.org/wiki/Line-line_intersection */
  this.intersectsLine = function(l0, l1) {
    var a1 = new Point2D(l0.v0.lng(), l0.v0.lat());  
    var a2 = new Point2D(l0.v1.lng(), l0.v1.lat());  
    var b1 = new Point2D(l1.v0.lng(), l1.v0.lat());  
    var b2 = new Point2D(l1.v1.lng(), l1.v1.lat());  

    var intersection = intersectLineLine(a1, a2, b1, b2);   

    return intersection.status == 'Intersection';
  }

  /* Fit the map center and zoom level to the search results */
  this.fitToResults = function() {
    var boundsPoly = new GPolyline(_fitToPolygon);
    var zoom; 
    if (_fitToPolygon.length >= 2){             
      zoom = map.getBoundsZoomLevel(boundsPoly.getBounds());
      /* if they specified a place name, then we only want to zoom out to fit,
           not zoom in (e.g. only one place matching the criteria in England, we still
           want to show England */
      if (!isEmpty($('where').value) && (zoom > map.getZoom())) {
        zoom = map.getZoom();
      }
    } else if (_fitToPolygon.length == 1) {
      zoom=12; //city level
    }
    if (_fitToPolygon.length >0) {
      map.setZoom(zoom);
      map.setCenter(this.getBoundsCenter(boundsPoly));
    }
  }

  /* return the center of the given GOverlay object */
  this.getBoundsCenter = function(boundsPoly) {
    /* if there is only one point, we don't do a fit, we just zoom to the point 
       TODO - fix this hack */    
    if (_fitToPolygon == 1) {
      return _fitToPolygon[0];
    } else {
      return boundsPoly.getBounds().getCenter();
    }
  }
  /* Get the index to the _overlays array for a given event id */ 
  this.getOverlaysIndex = function(id) {
   var i;
   for (i=0; i<_overlays.length; i++) {
     if (_overlays[i].id == id) {
       break;
     }
   }
   return i;
  }

  /** Open an event marker window.  If incr is specificied, then we show the event 
    * with an event[id + incr] */
  this.showEvent = function(id, incr) {
   var i = this.getOverlaysIndex(id);
   var next = i + incr;
   if ((next >= 0) && (next < _overlays.length)) {
     this.openMarker(next); 
   }
  }

   this.openMarker = function(index) {  
    if (_overlays[index].type == "point") {
      _overlays[index].overlay.openInfoWindowHtml(_overlays[index].html);
    } else {
      var overlay = _overlays[index].overlay;
      _overlayManager.unhighlightOverlay();
      var point = this.findClosestVertex(map.getCenter(), overlay);
      map.openInfoWindow(point, _overlays[index].html);
      _overlayManager.highlightOverlay(_overlays[index]);
    }
    this.highlightSidebarItem(_overlays[index]); 
  }
    
  /* Main function for info windows */  
  this.createInfoWindowHtml = function(index, event, width /* optional */, height /* optional */) {
    if (width==null || width=='null' || width=='') { width = this.INFO_WIDTH};
    if (height==null || height=='null' || height=='') {
      height = 'max-height:' + this.INFO_HEIGHT + 'px;';
    } else {
      height = 'height:' + height + 'px;'
    }
    var html = '<div class="result inforesult wikitext" style="width:' + width +'px;'+ height +'margin-bottom:10px">';
    if (event.flagged) {
      html += '<a target="_top" class="errorLabel" href="/event/changehistory.htm?id=' + event.id +'">Flagged! </a>'; 
    }
    //if there is an index, then put the marker icon in the bubble
    if (index != null) {
         var letter = this.ALPHA_CHARS.substring(index,index+1);
         html += '<img alt="marker" style="margin-right:4px;" src="' + _basePath + 'images/icons/' + letter + '_label.png' + '"/>';
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
        taglink[index] = '<a target="_top" href="" onclick="_mapManager.goToTag(\'' + encodedtag + '\'); return false;">' + tag +'</a>';
      } else {
        //this is embedded
        taglink[index] = '<a target="_top" href="'
          + '/search/eventsearch.htm?_tag='+ encodedtag + '&_maptype=' + _mapManager.getMapTypeIndex()
          + '">'+ tag +'</a>';
      }
    });
        html += '<div class="usertags"><b>Tags: </b>' + taglink.join(', ') + '</div>';   
    html += '<span class="source"><b>Source: </b>' + event.source + '</span>';
    html += '</div>';
        
    return html;
  } 
  
  /* was GooglePack - from  http://www.polyarc.us/google/packer.js */   
  this.encodePoly = function(poly)
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

  this.fitToPoly = function(poly, force /* optional */) {
    if (force == null) force = false;
    var bounds = poly.getBounds();
    var zoom = map.getBoundsZoomLevel(bounds);
    if ((zoom > map.getZoom()) || (force == true)) {
      map.setZoom(zoom);
    }
  }

  /* Find the closest vertex on the overlay to the given point */          
  this.findClosestVertex = function(point, overlay) {
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

}  //end EventOverlayManager

//-------------------------------------------------------------------------------
/** Object for initializing and handling the map, map size and associated controls,
  * and settings related to the DOM */
function MapManager() {
  this.ACCURACY_TO_ZOOM = [4, 5, 7, 10, 11, 12, 13, 14, 15];
  this.ZOOM_BOX_THRESHOLD = 10;
  this.ZOOM_COUNTRY = 5;
  this.ZOOM_USA = 4;
  this.ZOOM_WORLD = 2;
  this.MAP_TYPES = [G_NORMAL_MAP, G_SATELLITE_MAP, G_HYBRID_MAP, G_PHYSICAL_MAP];
  var m_diagonal = new GPolyline([
      new GLatLng(86,-180),
      new GLatLng(-86,180)
    ]);
    
  //initialize
  this.initializeMap = function(control) {
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
      GEvent.addDomListener(map.getContainer(), "DOMMouseScroll", this.wheelevent);
      map.getContainer().onmousewheel = this.wheelevent; 
      
      //map.setCenter(new GLatLng(40.879721,-76.998322),10);  //la la land, PA
      this.setMapExtent();
      map.checkResize(); //tell the map that we have resized it
      //TODO DEBUG FSM this.showDefault(); 
    }
  }
  
  /* default map coordinates */
  this.showDefault = function() {    
    if (null == map.getCenter()) {
      var bounds = m_diagonal.getBounds();
      map.setCenter(bounds.getCenter());  
      map.setZoom(map.getBoundsZoomLevel(bounds));
    }
  }
  
  this.setMapExtent = function() {
      var top = $("map").offsetTop;
      var height = this.getHeight() - top - 28;
      $("map").style.height=height+"px";
  }
  
  this.getHeight = function() {
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
    
  this.getWidth = function() {
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

  /* go to the tag link, but use the current map type */
  this.goToTag = function(tag) {
    // UGH.  Some tags have single quotes which messes up the call when the HTML uses onclick.  However 
    // we need those single quotes for the search, since it is a valid code in the URL and the server doesn't
    // decode %27 unescape single quotes
    tag = tag.gsub('%27', '\'');
    var tag = encodeURIComponent(tag);
    document.location = '/search/eventsearch.htm?_tag='+ tag + '&_maptype=' + _mapManager.getMapTypeIndex();
  }
  
  this.getMapTypeIndex = function() {
    var mapTypeIndex = 0;
    for (i=0; i<this.MAP_TYPES.length; i++) {
      if (this.MAP_TYPES[i].getName() == map.getCurrentMapType().getName()) {
        mapTypeIndex = i;
      }
    }
    return mapTypeIndex;
  }
      
  ///prevent page scroll
  this.wheelevent = function(e)
  {
      if (!e){
        e = window.event
      }
      if (e.preventDefault){
        e.preventDefault()
      }
      e.returnValue = false;
  }

  //abstract, please implement
  this.adjustSidebar = function(){};
  
} //end MapManager


//---------------------------------------------------------------
  /* BEGIN DATA OBJECT DEFINITIONS ============================= */
  function overlayItem(overlay, html, type, id, points ) {
    this.overlay = overlay;
    this.html = html;
    this.type = type;
    this.id = id;
    this.isHighlighted = false;
    this.points = points;
  }
  
  /* line object */
  function Line(vertex0, vertex1) {
    this.v0 = vertex0;
    this.v1 = vertex1;
  } 

  function Point2D(x, y) {
    this.x = x;
    this.y = y;
  }

  function Intersection(status) {
    this.status = status;
    this.points = new Array();
  }

  
  /* END OBJECT DEFINITIONS ============================= */


  /*****
  *   intersectLineLine.  From http://www.kevlindev.com/gui/math/intersection/
  *****/
  intersectLineLine = function(a1, a2, b1, b2) {
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
  } 
  
  function isEmpty(value) {
		return ((null == value) || ('' == value));
	}    
