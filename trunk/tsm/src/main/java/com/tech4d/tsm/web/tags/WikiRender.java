package com.tech4d.tsm.web.tags;

import info.bliki.wiki.model.WikiModel;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import org.apache.commons.lang.StringUtils;

import com.tech4d.tsm.auth.AuthHelper;

/**
 * Custom taglib to render wikitext on a page
 * @author frank
 *
 */
public class WikiRender extends SimpleTagSupport {
	private static final String USER_TAGS = "~~~~";
	private String wikiText;

	public void setWikiText(String wikiText) {
		this.wikiText = wikiText;
	}

	@Override
	public void doTag() throws JspException, IOException {
		//todo
		//String path = ((PageContext)this.getJspContext()).getServletContext().getRealPath(path)
		HttpServletRequest request = ((HttpServletRequest)((PageContext)this.getJspContext()).getRequest());
		String path = request.getContextPath();
		String basePath = request.getScheme()+"://"+request.getServerName()+path;
		int port = request.getServerPort();
		if (port != 80) {
			basePath += ":" + port;
		}
		basePath += "/";

		WikiModel wikiModel = new WikiModel(basePath + "images/${image}", basePath + "search/eventsearch.htm?_id=${title}");
		
		String rendered = wikiModel.render(wikiText);
		//implement ~~~ substitution for username
		String username = AuthHelper.getUsername();
		SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy hh:mm a z");
		StringBuffer replaceWith = new StringBuffer()
			.append("<a href='")
			.append(path)
			.append("member/contributions.htm?user=")
			.append(username).append("'>").append(username).append("</a> ")
			.append(sdf.format(new Date()));
		rendered = StringUtils.replace(rendered, USER_TAGS, replaceWith.toString());
		getJspContext().getOut().write(rendered);
	}

	
}
