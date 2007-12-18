package com.tech4d.tsm.web.tags;

import info.bliki.wiki.model.WikiModel;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import com.tech4d.tsm.web.wikiText.WikiModelFactory;

/**
 * Custom taglib to render wikitext on a page
 * @author frank
 *
 */
public class WikiRender extends SimpleTagSupport {
	private String wikiText;

	public void setWikiText(String wikiText) {
		this.wikiText = wikiText;
	}

	@Override
	public void doTag() throws JspException, IOException {
		HttpServletRequest request = ((HttpServletRequest)((PageContext)this.getJspContext()).getRequest());

		WikiModel wikiModel = WikiModelFactory.newWikiModel(request);
		String rendered = wikiModel.render(wikiText);
		getJspContext().getOut().write(rendered);
	}

	
}
