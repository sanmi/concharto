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
