package com.tech4d.tsm.model;

import javax.persistence.Entity;

import org.hibernate.annotations.Type;

import com.vividsolutions.jts.geom.Point;

@Entity
public class TsEventSummary extends BaseAuditableEntity {

    private String summary;

    private String snippet;

    private String description;

    private Point centroid;

    public TsEventSummary() {
        super();
    }

    public TsEventSummary(String summary, String snippet, String description,
            Point centroid) {
        super();
        this.summary = summary;
        this.snippet = snippet;
        this.description = description;
        this.centroid = centroid;
    }

    @Type(type = "com.tech4d.tsm.model.GeometryUserType")
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