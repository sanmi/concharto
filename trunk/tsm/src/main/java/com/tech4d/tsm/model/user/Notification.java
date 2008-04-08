package com.tech4d.tsm.model.user;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Lob;

import com.tech4d.tsm.model.BaseAuditableEntity;

/**
 * For notifying users based on system events
 */
@Entity
public class Notification extends BaseAuditableEntity{
	public static final int SZ_TITLE=512;
	private String toUsername;
	private String fromUsername;
	private String title;
	private String description;
	public enum NotificationType {TALK, WATCH};
	private NotificationType type;
	
	@Column(length=SZ_TITLE)
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
	@Lob //in mysql, this is type TEXT
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	@Enumerated(EnumType.STRING)
	public NotificationType getType() {
		return type;
	}
	public void setType(NotificationType type) {
		this.type = type;
	}
	@Column(length=User.SZ_USERNAME)
	public String getToUsername() {
		return toUsername;
	}
	public void setToUsername(String toUsername) {
		this.toUsername = toUsername;
	}
	@Column(length=User.SZ_USERNAME)
	public String getFromUsername() {
		return fromUsername;
	}
	public void setFromUsername(String fromUsername) {
		this.fromUsername = fromUsername;
	}
	
}
