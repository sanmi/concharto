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
package org.tsm.concharto.model;

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
    

    public EventSearchText(Event event) {
        copyFrom(event);
    }
    
    public void copyFrom(Event event) {
        this.setSummary(event.getSummary());
        this.setDescription(event.getDescription());
        this.setUserTags(event.getUserTagsAsString());
        this.setSource(event.getSource());
        this.setWhere(event.getWhere());
    }
    
    public EventSearchText() {
        super();
    }

    @Column(length= Event.SZ_DESCRIPTION)
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    @Column(length= Event.SZ_SOURCE)
    public String getSource() {
        return source;
    }
    public void setSource(String source) {
        this.source = source;
    }
    @Column(length= Event.SZ_SUMMARY)
    public String getSummary() {
        return summary;
    }
    public void setSummary(String summary) {
        this.summary = summary;
    }
    @Column(length= Event.SZ_USERTAGS)
    public String getUserTags() {
        return userTags;
    }
    public void setUserTags(String tags) {
        this.userTags = tags;
    }
    @Column(name = "_where", length= Event.SZ_WHERE)  //'where' is a sql reserved word
    public String getWhere() {
        return where;
    }
    public void setWhere(String where) {
        this.where = where;
    }

}
