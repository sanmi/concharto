package com.tech4d.tsm.web.edit;

import com.tech4d.tsm.model.time.TimeRange;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Point;

public class EventForm {
    //NOTE: the DB typically allows more chars than specified here. Done for 
    //upgrade flexibility
    public static final int SZ_TAGS = 256;
    public static final int SZ_WHERE = 128;
    public static final int SZ_SOURCE = 1024;
    public static final int SZ_SUMMARY = 80;
    public static final int SZ_DESCRIPTION = 1024;
    private String summary;
    private String description;
    private String tags;
    private Long eventId;
    private String where;
    private TimeRange when;
    private String source;
    private String geometryType;
    private Geometry geometry;
    private Integer zoomLevel;
    private Integer mapType;
    private Point mapCenter;
    private String searchResults;
    private Boolean showPreview;
    private String previewEvent; 
    private Boolean addEvent;

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long id) {
        this.eventId = id;
    }

    public EventForm() {
        super();
    }

    public EventForm(Long id, String summary, String description, String tags) {
        super();
        this.eventId = id;
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

    public String getGeometryType() {
        return geometryType;
    }

    public void setGeometryType(String geometryType) {
        this.geometryType = geometryType;
    }

    public Geometry getGeometry() {
        return geometry;
    }

    public void setGeometry(Geometry line) {
        this.geometry = line;
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

    public Integer getMapType() {
        return mapType;
    }

    public void setMapType(Integer mapType) {
        this.mapType = mapType;
    }

    public Point getMapCenter() {
        return mapCenter;
    }

    public void setMapCenter(Point mapCenter) {
        this.mapCenter = mapCenter;
    }

    /**
     * Search results in JSON format need to be passed as a hidden value
     * in the form so that the google maps API javascript functions can retrieve them
     * TODO refactor because this is also on the EventSearchForm 
     * @return JSON formatted array of Event objects
     */
    public String getSearchResults() {
        return searchResults;
    }

    public void setSearchResults(String searchResults) {
        this.searchResults = searchResults;
    }

    public Boolean getShowPreview() {
		return showPreview;
	}

	public void setShowPreview(Boolean showPreview) {
		this.showPreview = showPreview;
	}

	public String getPreviewEvent() {
		return previewEvent;
	}

	public void setPreviewEvent(String previewEvent) {
		this.previewEvent = previewEvent;
	}

	public Boolean getAddEvent() {
		return addEvent;
	}

	public void setAddEvent(Boolean addEvent) {
		this.addEvent = addEvent;
	}

	public int getSZ_DESCRIPTION() {
        return SZ_DESCRIPTION;
    }

    public int getSZ_SOURCE() {
        return SZ_SOURCE;
    }

    public int getSZ_SUMMARY() {
        return SZ_SUMMARY;
    }

    public int getSZ_TAGS() {
        return SZ_TAGS;
    }

    public int getSZ_WHERE() {
        return SZ_WHERE;
    }
    
}
