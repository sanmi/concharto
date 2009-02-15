/*******************************************************************************
 * Copyright 2009 Time Space Map, LLC
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package org.tsm.concharto.model.user;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import org.hibernate.annotations.ForeignKey;
import org.tsm.concharto.model.BaseAuditableEntity;
import org.tsm.concharto.model.EventSummary;


@Entity
public class User extends BaseAuditableEntity {

    public static final int SZ_USERNAME = 32;
    public static final int SZ_PASSWORD = 64;
    public static final int SZ_EMAIL = 128;
    
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
