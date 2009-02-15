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
package org.tsm.concharto.web.wiki;

import org.tsm.concharto.model.wiki.WikiText;

/**
 * Form object for editing WikiPages
 * @author frank
 *
 */
public class WikiTextForm {
	private WikiText wikiText;
	private boolean showPreview;
	private boolean isNewPage;
	
	public WikiTextForm() {
		super();
	}
	public WikiTextForm(WikiText wikiText, boolean isNewPage) {
		super();
		this.wikiText = wikiText;
		this.isNewPage = isNewPage;
	}
	public WikiText getWikiText() {
		return wikiText;
	}
	public void setWikiText(WikiText wikiText) {
		this.wikiText = wikiText;
	}
	public boolean getShowPreview() {
		return showPreview;
	}
	public void setShowPreview(boolean showPreview) {
		this.showPreview = showPreview;
	}
	public boolean getNewPage() {
		return isNewPage;
	}
	public void setNewPage(boolean isNewPage) {
		this.isNewPage = isNewPage;
	}
	
}
