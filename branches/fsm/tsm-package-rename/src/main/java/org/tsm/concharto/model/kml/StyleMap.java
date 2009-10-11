/*******************************************************************************
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
 ******************************************************************************/
package org.tsm.concharto.model.kml;

import java.util.Map;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import org.hibernate.annotations.ForeignKey;

@Entity
@DiscriminatorValue("map")
public class StyleMap extends StyleSelector {
    private Map<String, org.tsm.concharto.model.kml.StyleUrl> map;

    @org.hibernate.annotations.CollectionOfElements
    @ForeignKey(name="FK_STYLEMAP")
    public Map<String, org.tsm.concharto.model.kml.StyleUrl> getMap() {
        return map;
    }

    public void setMap(Map<String, org.tsm.concharto.model.kml.StyleUrl> map) {
        this.map = map;
    }
    
}