package com.tech4d.tsm.model;

import com.tech4d.tsm.model.geometry.StyleSelector;
import com.tech4d.tsm.model.geometry.TimePrimitive;
import com.tech4d.tsm.model.geometry.TsGeometry;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.util.List;

@Entity
public class TsEvent extends BaseAuditableEntity {
    
    private String summary;
    private String address;
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
    private TsmSpace tsmSpace;
    public enum TsmSpace {ENCYCLOPEDIA, ANECDOTAL, PERSONAL, CURRENT_EVENT}
    private boolean visible;
    private TsmFlag flag;
    public enum TsmFlag {FOR_DELETION, FOR_CONTENT}

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @OneToOne(cascade = { CascadeType.ALL })
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
    }

    @ManyToOne(cascade = { CascadeType.ALL })
    @Cascade(org.hibernate.annotations.CascadeType.DELETE)
    public TimePrimitive getTimePrimitive() {
        return timePrimitive;
    }

    public void setTimePrimitive(TimePrimitive timePrimative) {
        this.timePrimitive = timePrimative;
    }

    @ManyToOne(cascade = { CascadeType.REFRESH, CascadeType.MERGE })
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

    public TsmSpace getTsmSpace() {
        return tsmSpace;
    }

    public void setTsmSpace(TsmSpace tsmSpace) {
        this.tsmSpace = tsmSpace;
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
