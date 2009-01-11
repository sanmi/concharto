/*
 * ***** BEGIN LICENSE BLOCK *****
 * Version: MPL 1.1
 * 
 * The contents of this file are subject to the Mozilla Public License Version 
 * 1.1 (the "License"); you may not use this file except in compliance with 
 * the License. You may obtain a copy of the License at 
 * http://www.mozilla.org/MPL/
 * 
 * Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 * for the specific language governing rights and limitations under the
 * License.
 * 
 * The Original Code is Concharto.
 * 
 * The Initial Developer of the Original Code is
 * Time Space Map, LLC
 * Portions created by the Initial Developer are Copyright (C) 2007 - 2009
 * the Initial Developer. All Rights Reserved.
 * 
 * Contributor(s):
 * Time Space Map, LLC
 * 
 * ***** END LICENSE BLOCK *****
 */
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

