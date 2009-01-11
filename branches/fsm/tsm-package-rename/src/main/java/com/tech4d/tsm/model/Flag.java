/*******************************************************************************
 * ***** BEGIN LICENSE BLOCK *****
 * Version: MPL 1.1
 * 
 * The contents of this file are subject to the Mozilla Public License Version 
 * 1.1 (the "License"); you may not use this file except in compliance with 
 * the License. You may obtain a copy of the License at 
 * http://www.mozilla.org/MPL/
 * 
 * Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 * for the specific language governing rights and limitations under the
 * License.
 * 
 * The Original Code is Concharto.
 * 
 * The Initial Developer of the Original Code is
 * Time Space Map, LLC
 * Portions created by the Initial Developer are Copyright (C) 2007 - 2009
 * the Initial Developer. All Rights Reserved.
 * 
 * Contributor(s):
 * Time Space Map, LLC
 * 
 * ***** END LICENSE BLOCK *****
 ******************************************************************************/
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
