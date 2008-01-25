package com.tech4d.tsm.web.wikiText;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

import com.tech4d.tsm.auth.AuthHelper;
import com.tech4d.tsm.web.util.UrlFormat;

public class SubstitutionMacro {
	private static final String USER_TAGS = "~~~~";

	public static String postSignature(HttpServletRequest request, String rendered) {
		return postSignature(UrlFormat.getBasepath(request), rendered);
	}
	
	public static String postSignature(String basePath, String rendered) {
		//implement ~~~ substitution for username
		String username = AuthHelper.getUsername();
		SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy hh:mm a z");
		StringBuffer replaceWith = new StringBuffer()
			.append("<a href='")
			.append(basePath)
			.append("contributions.htm?user=")
			.append(username).append("'>").append(username).append("</a> ")
			.append(sdf.format(new Date()));
		rendered = StringUtils.replace(rendered, USER_TAGS, replaceWith.toString());
		return rendered;
	}

}
