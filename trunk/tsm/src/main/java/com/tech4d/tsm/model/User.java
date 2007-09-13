package com.tech4d.tsm.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

@Entity
public class User extends BaseEntity {

    private String username;

    private String password;

    private String email;

    private List<EventSummary> eventSummaries;

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
    public List<EventSummary> getEventSummaries() {
        return eventSummaries;
    }

    public void setEventSummaries(List<EventSummary> eventSummaries) {
        this.eventSummaries = eventSummaries;
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
    public List<UserTag> getUserTags() {
        return userTags;
    }

    public void setUserTags(List<UserTag> userTags) {
        this.userTags = userTags;
    }

}