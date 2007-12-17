package com.tech4d.tsm.web.discuss;

import com.tech4d.tsm.model.WikiText;

public class WikiTextForm {
	private WikiText wikiText;
	private boolean showPreview;
	

	public boolean getShowPreview() {
		return showPreview;
	}
	public void setShowPreview(boolean showPreview) {
		this.showPreview = showPreview;
	}
	public WikiTextForm(WikiText wikiText) {
		super();
		this.wikiText = wikiText;
	}
	public WikiTextForm() {
		wikiText = new WikiText();
	}
	public WikiText getWikiText() {
		return wikiText;
	}
	public void setWikiText(WikiText wikiText) {
		this.wikiText = wikiText;
	}

}
