package com.tech4d.tsm.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import com.tech4d.tsm.model.geometry.Feature;

@Entity
public class Event extends BaseAuditableEntity {

    private Feature feature;
    private List<UserTag> userTags;
    private Votes votes;
    private List<ChangeGroup> history;
    private List<User> contributors;
    private String sourceUrl;
    private TsmSpace tsmSpace;  
    public enum TsmSpace {ENCYCLOPEDIA, ANECDOTAL, PERSONAL, CURRENT_EVENT};
    private boolean visible;
    private TsmFlag flag;
    public enum TsmFlag {FOR_DELETION, FOR_CONTENT};

    public String getSourceUrl() {
        return sourceUrl;
    }

    public void setSourceUrl(String sourceUrl) {
        this.sourceUrl = sourceUrl;
    }

    public TsmSpace getTsmSpace() {
        return tsmSpace;
    }

    public void setTsmSpace(TsmSpace tsmSpace) {
        this.tsmSpace = tsmSpace;
    }

    @OneToOne(cascade = { CascadeType.ALL })
    public Feature getFeature() {
        return feature;
    }

    public void setFeature(Feature feature) {
        this.feature = feature;
    }

    @OneToMany(cascade={CascadeType.ALL})
    public List<ChangeGroup> getHistory() {
        return history;
    }

    public void setHistory(List<ChangeGroup> history) {
        this.history = history;
    }

    @ManyToMany(cascade={CascadeType.ALL})
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
    public List<User> getContributors() {
        return contributors;
    }

    public void setContributors(List<User> participants) {
        this.contributors = participants;
    }

    public TsmFlag getFlag() {
        return flag;
    }

    public void setFlag(TsmFlag flag) {
        this.flag = flag;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

}