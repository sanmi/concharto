package com.tech4d.tsm.model;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * 
 * For using MySQL's Free-text search engine.  We copy this information from the event class
 * becasue MySQL only supports freetext indexes on MyISAM files, but MyISAM files don't support
 * things like foreign keys.
 */
@Entity
@org.hibernate.annotations.Table(comment = "ENGINE : MyISAM", appliesTo = "EventSearchText")
public class EventSearchText extends BaseEntity {
    private String summary;
    private String description;
    private String userTags;
    private String source;  
    private String where;
    

    public EventSearchText(TsEvent tsEvent) {
        copyFrom(tsEvent);
    }
    
    public void copyFrom(TsEvent tsEvent) {
        this.setSummary(tsEvent.getSummary());
        this.setDescription(tsEvent.getDescription());
        this.setUserTags(tsEvent.getUserTagsAsString());
        this.setSource(tsEvent.getSourceUrl());
        this.setWhere(tsEvent.getWhere());
    }
    
    public EventSearchText() {
        super();
    }

    @Column(length=TsEvent.SZ_DESCRIPTION)
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    @Column(length=TsEvent.SZ_SOURCEURL)
    public String getSource() {
        return source;
    }
    public void setSource(String source) {
        this.source = source;
    }
    @Column(length=TsEvent.SZ_SUMMARY)
    public String getSummary() {
        return summary;
    }
    public void setSummary(String summary) {
        this.summary = summary;
    }
    @Column(length=TsEvent.SZ_USERTAGS)
    public String getUserTags() {
        return userTags;
    }
    public void setUserTags(String tags) {
        this.userTags = tags;
    }
    @Column(name = "_where", length=TsEvent.SZ_WHERE)  //'where' is a sql reserved word 
    public String getWhere() {
        return where;
    }
    public void setWhere(String where) {
        this.where = where;
    }

}
