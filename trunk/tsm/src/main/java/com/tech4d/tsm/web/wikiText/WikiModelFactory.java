package com.tech4d.tsm.web.wikiText;

import info.bliki.wiki.model.WikiModel;

import javax.servlet.http.HttpServletRequest;


public class WikiModelFactory {

	public static WikiModel newWikiModel(HttpServletRequest request) {
		String contextPath = request.getContextPath();
		StringBuffer basePathsb = new StringBuffer()
			.append(request.getScheme())
			.append("://")
			.append(request.getServerName());
		int port = request.getServerPort();
		if (port != 80) {
			basePathsb.append(":").append(port);
		}
		basePathsb.append('/').append(contextPath);
		String basePath = basePathsb.toString();

		return new TsmWikiModel(basePath, basePath + "images/${image}", basePath + "search/eventsearch.htm?_id=${title}");
	}

	

}
