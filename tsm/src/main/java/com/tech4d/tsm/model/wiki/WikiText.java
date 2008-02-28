package com.tech4d.tsm.model.wiki;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;

import com.tech4d.tsm.model.BaseAuditableEntity;

@Entity
public class WikiText extends BaseAuditableEntity {
    public static final int SZ_TITLE = 512;
	private String text;
	String title;

	@Lob
	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

    @Column(length=SZ_TITLE)
    public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
}
