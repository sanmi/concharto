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
package org.tsm.concharto.model;

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
