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
package com.tech4d.tsm.web.wiki;

import com.tech4d.tsm.model.wiki.WikiText;

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
