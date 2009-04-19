/*******************************************************************************
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
