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

import javax.persistence.Embeddable;

@Embeddable
public class PolyStyle {
    private Boolean fill;

    private Boolean outline;

    public PolyStyle() {
        super();
    }

    public PolyStyle(Boolean fill, Boolean outline) {
        super();
        this.fill = fill;
        this.outline = outline;
    }

    public Boolean getFill() {
        return fill;
    }

    public void setFill(Boolean fill) {
        this.fill = fill;
    }

    public Boolean getOutline() {
        return outline;
    }

    public void setOutline(Boolean outline) {
        this.outline = outline;
    }

}
