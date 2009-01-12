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
