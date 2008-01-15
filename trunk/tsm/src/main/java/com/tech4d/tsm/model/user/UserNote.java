package com.tech4d.tsm.model.user;

import javax.persistence.Column;
import javax.persistence.Entity;

import com.tech4d.tsm.model.BaseEntity;

@Entity
public class UserNote extends BaseEntity {
    public static final int SZ_PASSWORD_RETRIEVAL_KEY = 128;
    
    private String passwordRetrievalKey;
    
    public UserNote() {
		super();
	}

	public UserNote(String passwordRetrievalKey) {
		super();
		this.passwordRetrievalKey = passwordRetrievalKey;
	}

	@Column(length=SZ_PASSWORD_RETRIEVAL_KEY)
	public String getPasswordRetrievalKey() {
		return passwordRetrievalKey;
	}

	public void setPasswordRetrievalKey(String passwordRetrievalKey) {
		this.passwordRetrievalKey = passwordRetrievalKey;
	}
}
