package com.tech4d.tsm.web;

public class EventForm {
    private String summary;

    private String description;

    private String tags;
    
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public EventForm() {
        super();
    }

    public EventForm(Long id, String summary, String description, String tags) {
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

}
