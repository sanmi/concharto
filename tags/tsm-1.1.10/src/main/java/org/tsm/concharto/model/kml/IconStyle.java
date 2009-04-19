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

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class IconStyle {
    private Float scale;

    private Float heading;

    private Icon icon;

    private org.tsm.concharto.model.kml.Vec2 hotSpot;

    public IconStyle() {
        super();
    }

    public IconStyle(Float scale, Float heading, Icon icon, org.tsm.concharto.model.kml.Vec2 hotSpot) {
        super();
        this.scale = scale;
        this.heading = heading;
        this.icon = icon;
        this.hotSpot = hotSpot;
    }

    public Float getHeading() {
        return heading;
    }

    public void setHeading(Float heading) {
        this.heading = heading;
    }

    @Column(name = "iconScale")
    public Float getScale() {
        return scale;
    }

    public void setScale(Float scale) {
        this.scale = scale;
    }

    public Icon getIcon() {
        return icon;
    }

    public void setIcon(Icon icon) {
        this.icon = icon;
    }

    public org.tsm.concharto.model.kml.Vec2 getHotSpot() {
        return hotSpot;
    }

    public void setHotSpot(org.tsm.concharto.model.kml.Vec2 hotSpot) {
        this.hotSpot = hotSpot;
    }

}
