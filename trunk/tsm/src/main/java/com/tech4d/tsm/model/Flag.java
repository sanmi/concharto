package com.tech4d.tsm.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.ForeignKey;

import com.tech4d.tsm.model.user.User;
/**
 * Allows the user to flag an event to be moved or deleted
 * @author frank
 */
@Entity
public class Flag extends BaseAuditableEntity {
	public static final int SZ_DISPOSITION = 32;
	public static final int SZ_DISPOSITION_COMMENT = 512;
	public static final int SZ_REASON = 32;
	public static final int SZ_COMMENT = 512;
	public static final String[] REASON_CODES = {
		"toPersonal", "toFiction","isFake","isCopyrighted"
	};
	public static final String DISPOSITION_DELETED = "deleted";
	public static final String DISPOSITION_REMOVED = "removed";
	public static final String DISPOSITION_INVALID = "invalid";
	public static final String DISPOSITION_WONTFIX = "wontfix";
	public static final String DISPOSITION_FIXED = "fixed";
	public static final String[] DISPOSITION_CODES = {
		DISPOSITION_DELETED, DISPOSITION_REMOVED, DISPOSITION_INVALID, DISPOSITION_WONTFIX, DISPOSITION_FIXED
	};
	private String comment;
	private User user;
	private String reason;
	private String disposition;
	private String dispositionComment;
	private Event event;
	private String state;

	public Flag() {
	}
	
	public Flag(String comment, String reasonCode, User user, Event event) {
		super();
		this.comment = comment;
		this.reason = reasonCode;
		this.user = user;
		this.event = event;
	}
	
    @Column(length= SZ_COMMENT)
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	
	@ManyToOne
    @ForeignKey(name="FK_FLAG_USER")
	public User getUser() {
		return user;
	}
	public void setUser(User whoFlaggedIt) {
		this.user = whoFlaggedIt;
	}
    @Column(length= SZ_REASON)
    public String getReason() {
		return reason;
	}
	public void setReason(String reasonCode) {
		this.reason = reasonCode;
	}
    @Column(length= SZ_DISPOSITION_COMMENT)
	public String getDispositionComment() {
		return dispositionComment;
	}
	public void setDispositionComment(String finalDispositionComment) {
		this.dispositionComment = finalDispositionComment;
	}
	
	@ManyToOne
    @ForeignKey(name="FK_FLAG_EVENT")
	public Event getEvent() {
		return event;
	}
	public void setEvent(Event event) {
		this.event = event;
	}

    @Column(length= SZ_DISPOSITION)
	public String getDisposition() {
		return disposition;
	}

	public void setDisposition(String disposition) {
		this.disposition = disposition;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}
	
}
