package com.tech4d.tsm.model.user;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import org.hibernate.annotations.ForeignKey;

import com.tech4d.tsm.model.BaseAuditableEntity;
import com.tech4d.tsm.model.EventSummary;

@Entity
public class User extends BaseAuditableEntity {

    public static final int SZ_USERNAME = 32;
    public static final int SZ_PASSWORD = 64;
    
    private String username;
    private String password;
    private String email;
    private List<EventSummary> eventSummaries;
    private List<Role> roles;
    private UserNote userNote;

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
    public List<EventSummary> getEventSummaries() {
        return eventSummaries;
    }

    public void setEventSummaries(List<EventSummary> eventSummaries) {
        this.eventSummaries = eventSummaries;
    }

    @Column(length=SZ_PASSWORD)
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    @Column (unique=true, length=SZ_USERNAME)
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @ManyToMany(cascade={CascadeType.ALL})
    @ForeignKey(name="FK_USER_ROLE", inverseName = "FK_ROLE_USER")
    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

	@OneToOne (cascade = CascadeType.ALL)
    @ForeignKey(name="FK_USER_USERNOTE")
	public UserNote getUserNote() {
		return userNote;
	}

	public void setUserNote(UserNote userNote) {
		this.userNote = userNote;
	}
    
    

}