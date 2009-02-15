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

import javax.persistence.Column;
import javax.persistence.Entity;

import org.tsm.concharto.model.BaseEntity;


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
