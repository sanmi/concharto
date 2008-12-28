package com.tech4d.tsm.model;

import javax.persistence.Entity;

/**
 * Describes the accuracy of a geographic feature.  E.g. "neighborhood", "city", 
 * "pinpoint", etc.
 */
@Entity
public class PositionalAccuracy extends BaseEntity {
	private String name;
	private Boolean visible;
	
	public PositionalAccuracy() {
		super();
	}
	public PositionalAccuracy(String name) {
		super();
		this.name = name;
	}
	public PositionalAccuracy(String name, Boolean visible) {
		super();
		this.name = name;
		this.visible = visible;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Boolean getVisible() {
		return visible;
	}
	public void setVisible(Boolean visible) {
		this.visible = visible;
	}
}
