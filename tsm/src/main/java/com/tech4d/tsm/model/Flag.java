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
		"isDuplicate", "toPersonal", "toFiction","isFake","isCopyrighted"
	};
	public static final String DISPOSITION_DELETED = "deleted";
	public static final String DISPOSITION_REMOVED = "removed";
	public static final String DISPOSITION_INVALID = "invalid";
	public static final String DISPOSITION_FLAG_SPAM = "flagspam";
	public static final String DISPOSITION_WONTFIX = "wontfix";
	public static final String DISPOSITION_FIXED = "fixed";
	//TODO - remove DISPOSITION_DELETED for now 6-15-08
	public static final String[] DISPOSITION_CODES = {
		DISPOSITION_REMOVED, DISPOSITION_INVALID, DISPOSITION_FLAG_SPAM, DISPOSITION_WONTFIX, DISPOSITION_FIXED
	};
	private String comment;
	private String username;
	private String reason;
	private String disposition;
	private String dispositionComment;
	private Event event;
	private String state;

	public Flag() {
	}
	
	public Flag(String comment, String reasonCode, String username, Event event) {
		super();
		this.comment = comment;
		this.reason = reasonCode;
		this.username = username;
		this.event = event;
	}
	
    @Column(length= SZ_COMMENT)
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}

		
    @Column(length= User.SZ_USERNAME)
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
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
