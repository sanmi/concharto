package com.tech4d.tsm.model.user;

import javax.persistence.Column;
import javax.persistence.Entity;

import com.tech4d.tsm.model.BaseEntity;

@Entity
public class UserNote extends BaseEntity {
    public static final int SZ_PASSWORD_RETRIEVAL_KEY = 128;
    public static final int SZ_REMEMBER_ME_KEY = 128;
    
    private String passwordRetrievalKey;
    private String rememberMeKey;
    
	public UserNote() {
		super();
	}

    public UserNote(String passwordRetrievalKey, String rememberMeKey) {
		super();
		this.passwordRetrievalKey = passwordRetrievalKey;
		this.rememberMeKey = rememberMeKey;
	}

	@Column(length=SZ_PASSWORD_RETRIEVAL_KEY)
	public String getPasswordRetrievalKey() {
		return passwordRetrievalKey;
	}

	public void setPasswordRetrievalKey(String passwordRetrievalKey) {
		this.passwordRetrievalKey = passwordRetrievalKey;
	}

	@Column(length=SZ_REMEMBER_ME_KEY)
	public String getRememberMeKey() {
		return rememberMeKey;
	}

	public void setRememberMeKey(String rememberMeKey) {
		this.rememberMeKey = rememberMeKey;
	}
	
}
