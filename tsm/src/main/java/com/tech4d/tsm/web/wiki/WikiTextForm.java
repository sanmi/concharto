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
