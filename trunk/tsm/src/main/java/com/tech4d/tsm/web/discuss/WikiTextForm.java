package com.tech4d.tsm.web.discuss;

import com.tech4d.tsm.model.Event;

public class WikiTextForm {
	private boolean showPreview;
	private Event event;
	
	public WikiTextForm(Event event) {
		super();
		this.event = event;
	}
	public boolean getShowPreview() {
		return showPreview;
	}
	public void setShowPreview(boolean showPreview) {
		this.showPreview = showPreview;
	}
	public Event getEvent() {
		return event;
	}
	public void setEvent(Event event) {
		this.event = event;
	}
}
