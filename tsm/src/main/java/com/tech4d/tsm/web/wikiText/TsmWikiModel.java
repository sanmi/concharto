package com.tech4d.tsm.web.wikiText;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang.StringUtils;

import com.tech4d.tsm.auth.AuthHelper;

import info.bliki.wiki.model.WikiModel;

public class TsmWikiModel extends WikiModel {
	private static final String USER_TAGS = "~~~~";
	private String contextPath;
	
	public TsmWikiModel(String contextPath, String imageBaseURL, String linkBaseURL) {
		super(imageBaseURL, linkBaseURL);
		this.contextPath = contextPath;
	}

	@Override
	public String render(String rawWikiText) {
		// TODO Auto-generated method stub
		String rendered = super.render(rawWikiText);
		//implement ~~~ substitution for username
		String username = AuthHelper.getUsername();
		SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy hh:mm a z");
		StringBuffer replaceWith = new StringBuffer()
			.append("<a href='")
			.append(this.contextPath)
			.append("member/contributions.htm?user=")
			.append(username).append("'>").append(username).append("</a> ")
			.append(sdf.format(new Date()));
		rendered = StringUtils.replace(rendered, USER_TAGS, replaceWith.toString());
		return rendered;
	}
	
	

}
