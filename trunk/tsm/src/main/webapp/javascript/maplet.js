  //Setup a special map manager for maplets
  function MapletMapManager() {
    //initialize
    this.initializeMap = function(control) {
      initializeMapVars();
      // map and its equipment
      map = new GMap2();

      geocoder = new GClientGeocoder();
      //FSM DBEUG this.showDefault(); 
    }
    
    /* default map coordinates */
    this.showDefault = function() {    
      this.m_diagonal.getBoundsAsync(function(bounds) {
        map.setCenter(bounds.getCenter());  
        map.setZoom(map.getBoundsZoomLevel(bounds));
      });
    }
    
  }
  MapletMapManager.prototype = new MapManager();  //inherit with override
  _mapManager = new MapletMapManager();

