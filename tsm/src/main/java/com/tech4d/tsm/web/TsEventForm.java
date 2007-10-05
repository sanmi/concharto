package com.tech4d.tsm.web;

import com.tech4d.tsm.model.geometry.TimeRange;

public class TsEventForm {
    private String summary;
    private String description;
    private String tags;
    private Long id;
    private String where;
    private TimeRange when;
    private String source;
    private Double lat;
    private Double lng;
    private Integer zoomLevel;
    private String searchResults;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TsEventForm() {
        super();
    }

    public TsEventForm(Long id, String summary, String description, String tags) {
        super();
        this.id = id;
        this.summary = summary;
        this.description = description;
        this.tags = tags;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    public TimeRange getWhen() {
        return when;
    }

    public void setWhen(TimeRange when) {
        this.when = when;
    }

    public String getWhere() {
        return where;
    }

    public void setWhere(String where) {
        this.where = where;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public Integer getZoomLevel() {
        return zoomLevel;
    }

    public void setZoomLevel(Integer zoomLevel) {
        this.zoomLevel = zoomLevel;
    }

    /**
     * Search results in JSON format need to be passed as a hidden value
     * in the form so that the google maps API javascript functions can retrieve them
     * TODO refactor because this is also on the EventSearchForm 
     * @return resutls JSON formatted array of TsEvent objects
     */
    public String getSearchResults() {
        return searchResults;
    }

    public void setSearchResults(String searchResults) {
        this.searchResults = searchResults;
    }

}
