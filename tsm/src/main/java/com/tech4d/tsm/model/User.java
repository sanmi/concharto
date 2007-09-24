package com.tech4d.tsm.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

import org.hibernate.annotations.ForeignKey;

@Entity
public class User extends BaseAuditableEntity {

    private String username;

    private String password;

    private String email;

    private List<TsEventSummary> tsEventSummaries;

    private List<UserTag> userTags;

    public User(String username, String password, String email) {
        super();
        this.username = username;
        this.password = password;
        this.email = email;
    }

    public User() {
        super();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @OneToMany(cascade={CascadeType.ALL})
    @ForeignKey(name="FK_USER_EVENTSUMMARY", inverseName = "FK_EVENTSUMMARY_USER")
    public List<TsEventSummary> getEventSummaries() {
        return tsEventSummaries;
    }

    public void setEventSummaries(List<TsEventSummary> tsEventSummaries) {
        this.tsEventSummaries = tsEventSummaries;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @OneToMany(cascade={CascadeType.ALL})
    @ForeignKey(name="FK_USER_USERTAG", inverseName = "FK_USERTAG_USER")
    public List<UserTag> getUserTags() {
        return userTags;
    }

    public void setUserTags(List<UserTag> userTags) {
        this.userTags = userTags;
    }

}