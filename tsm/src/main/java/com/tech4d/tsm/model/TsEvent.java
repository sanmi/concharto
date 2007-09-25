package com.tech4d.tsm.model;

import static org.apache.commons.lang.StringUtils.join;
import static org.apache.commons.lang.StringUtils.split;

import com.tech4d.tsm.model.geometry.StyleSelector;
import com.tech4d.tsm.model.geometry.TimePrimitive;
import com.tech4d.tsm.model.geometry.TsGeometry;
import com.tech4d.tsm.model.geometry.KmlFeature;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.ForeignKey;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright 2007, Time Space Map
 *
 * This is the main class for storing events in time and space.  It contains enough information to
 * be serialized to KML and can be searched using a spatial query.
 */
@Entity
public class TsEvent extends BaseAuditableEntity implements KmlFeature {
    
    private String summary;
    private String streetAddress;
    private String snippet;
    private String description;
    private TimePrimitive timePrimitive;
    private StyleSelector styleSelector;
    private TsGeometry tsGeometry;
    private List<UserTag> userTags;
    private Votes votes;
    private List<ChangeGroup> history;
    private List<User> contributors;
    private String sourceUrl;
    private Catalog catalog;
    public enum Catalog {ENCYCLOPEDIA, ANECDOTAL, PERSONAL, CURRENT_EVENT}
    private boolean visible;
    private Flag flag;
    public enum Flag {FOR_DELETION, FOR_CONTENT}
    private EventSearchText eventSearchText;

    /**
     * @see com.tech4d.tsm.model.geometry.KmlFeature
     */
    public String getStreetAddress() {
        return streetAddress;
    }

    public void setStreetAddress(String address) {
        this.streetAddress = address;
    }

    @OneToOne(cascade = { CascadeType.ALL })
    @ForeignKey(name="FK_EVENT_GEOM")
    public TsGeometry getTsGeometry() {
        return tsGeometry;
    }

    public void setTsGeometry(TsGeometry geometry) {
        this.tsGeometry = geometry;
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
        this.snippet = snippet;
    }

    @ManyToOne(cascade = { CascadeType.ALL })
    @ForeignKey(name="FK_EVENT_TIMEPR")
    @Cascade(org.hibernate.annotations.CascadeType.DELETE)
    public TimePrimitive getTimePrimitive() {
        return timePrimitive;
    }

    public void setTimePrimitive(TimePrimitive timePrimative) {
        this.timePrimitive = timePrimative;
    }

    @ManyToOne(cascade = { CascadeType.REFRESH, CascadeType.MERGE })
    @ForeignKey(name="FK_EVENT_STYLE")
    public StyleSelector getStyleSelector() {
        return styleSelector;
    }

    public void setStyleSelector(StyleSelector styleSelector) {
        this.styleSelector = styleSelector;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }
    
    public String getSourceUrl() {
        return sourceUrl;
    }

    public void setSourceUrl(String sourceUrl) {
        this.sourceUrl = sourceUrl;
    }

    public Catalog getCatalog() {
        return catalog;
    }

    public void setCatalog(Catalog catalog) {
        this.catalog = catalog;
    }

    @OneToMany(cascade={CascadeType.ALL})
    @ForeignKey(name="FK_EVENT_HISTORY", inverseName = "FK_HISTORY_EVENT")
    public List<ChangeGroup> getHistory() {
        return history;
    }

    public void setHistory(List<ChangeGroup> history) {
        this.history = history;
    }

    @ManyToMany(cascade={CascadeType.ALL})
    @ForeignKey(name="FK_EVENT_USERTAG", inverseName = "FK_USERTAG_EVENT")
    public List<UserTag> getUserTags() {
        return userTags;
    }

    public void setUserTags(List<UserTag> userTags) {
        this.userTags = userTags;
    }

    public Votes getVotes() {
        return votes;
    }

    public void setVotes(Votes votes) {
        this.votes = votes;
    }

    @ManyToMany(cascade={CascadeType.ALL})
    @ForeignKey(name="FK_EVENT_CONTRIB", inverseName = "FK_CONTRIB_EVENT")
    public List<User> getContributors() {
        return contributors;
    }

    public void setContributors(List<User> participants) {
        this.contributors = participants;
    }

    public Flag getFlag() {
        return flag;
    }

    public void setFlag(Flag flag) {
        this.flag = flag;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    /**
     * Returns a comma separated list of tags
     * @return a comma separated list of tags
     */
    @Transient
    public String getUserTagsAsString() {
        return join(userTags, ',');
    }
    
    /**
     * Populates the tag list from a comma separated list
     * @param tagList a comma separated list of tags
     */
    @Transient
    public void setUserTagsAsString(String tagList) {
        // a dirty check so we don't have to save each time
        String originalTags = getUserTagsAsString();
        boolean dirty = false;
        if (originalTags == null) {
            if (tagList != null) {
                dirty = true;
            }
        } else {
            if (!originalTags.equals(tagList)) {
                dirty = true;
            }
        }
        if (dirty) {
            String[] tags = split(tagList, ",");
            List<UserTag> userTags = new ArrayList<UserTag>();
            for (String tag : tags) {
                userTags.add(new UserTag(tag));
            }
            this.setUserTags(userTags);
        }
    }

    @OneToOne(cascade = { CascadeType.ALL })
    @ForeignKey(name="FK_EVENT_EVENTSEARCHTEXT")
    public EventSearchText getEventSearchText() {
        return eventSearchText;
    }

    public void setEventSearchText(EventSearchText eventSearchText) {
        this.eventSearchText = eventSearchText;
    }

}
