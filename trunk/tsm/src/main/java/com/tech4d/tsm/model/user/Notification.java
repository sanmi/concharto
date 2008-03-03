package com.tech4d.tsm.model.user;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Lob;
import javax.persistence.OneToOne;

import org.hibernate.annotations.ForeignKey;

import com.tech4d.tsm.model.BaseAuditableEntity;

/**
 * For notifying users based on system events
 */
@Entity
public class Notification extends BaseAuditableEntity{
	public static final int SZ_TITLE=512;
	private User toUser;
	private User fromUser;
	private String title;
	private String description;
	public enum NotificationType {TALK, WATCH};
	private NotificationType type;
	
	@OneToOne (cascade = CascadeType.ALL)
    @ForeignKey(name="FK_NOTIF_TOUSER")
	public User getToUser() {
		return toUser;
	}
	public void setToUser(User toUser) {
		this.toUser = toUser;
	}
	
	@OneToOne (cascade = CascadeType.ALL)
    @ForeignKey(name="FK_NOTIF_FROMUSER")
	public User getFromUser() {
		return fromUser;
	}
	public void setFromUser(User fromUser) {
		this.fromUser = fromUser;
	}

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
	
}
