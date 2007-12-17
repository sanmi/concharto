package com.tech4d.tsm.model;

import javax.persistence.Entity;
import javax.persistence.Lob;

@Entity
public class WikiText extends BaseEntity {
	private String text;

	@Lob
	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
}
