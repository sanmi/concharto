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
import javax.persistence.OneToOne;

import org.hibernate.annotations.ForeignKey;

import com.tech4d.tsm.model.user.User;

@Entity
public class Spotlight extends BaseAuditableEntity {
	public static final int SZ_FIELDS = 2000;
    public static final int SZ_CATALOG = 64;
	private String label;
	private String link;
	private Boolean visible;
	private User addedByUser;
	private String catalog;
	
	@Column(length=SZ_FIELDS)
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	@Column(length=SZ_FIELDS)
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}
	public Boolean getVisible() {
		return visible;
	}
	public void setVisible(Boolean visible) {
		this.visible = visible;
	}
	@OneToOne 
    @ForeignKey(name="FK_SPOTLIGHT_USER")
	public User getAddedByUser() {
		return addedByUser;
	}
	public void setAddedByUser(User addedByUser) {
		this.addedByUser = addedByUser;
	}
	@Column(length=SZ_CATALOG)
	public String getCatalog() {
		return catalog;
	}
	public void setCatalog(String catalog) {
		this.catalog = catalog;
	}
}
