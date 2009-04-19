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
package org.tsm.concharto.model;

import javax.persistence.Entity;

import org.hibernate.annotations.Type;

import com.vividsolutions.jts.geom.Point;

@Entity
public class EventSummary extends BaseAuditableEntity {

    private String summary;

    private String snippet;

    private String description;

    private Point centroid;

    public EventSummary() {
        super();
    }

    public EventSummary(String summary, String snippet, String description,
            Point centroid) {
        super();
        this.summary = summary;
        this.snippet = snippet;
        this.description = description;
        this.centroid = centroid;
    }

    @Type(type = "org.tsm.concharto.model.geometry.GeometryUserType")
    public Point getCentroid() {
        return centroid;
    }

    public void setCentroid(Point centroid) {
        this.centroid = centroid;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSnippet() {
        return snippet;
    }

    public void setSnippet(String snippet) {
        this.snippet = snippet;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

}
