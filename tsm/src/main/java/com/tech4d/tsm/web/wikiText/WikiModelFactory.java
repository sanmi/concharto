package com.tech4d.tsm.web.wikiText;

import info.bliki.wiki.model.WikiModel;

import javax.servlet.http.HttpServletRequest;


public class WikiModelFactory {

	public static WikiModel newWikiModel(HttpServletRequest request) {
		String contextPath = request.getContextPath();
		String basePath = request.getScheme()+"://"+request.getServerName()+contextPath;
		int port = request.getServerPort();
		if (port != 80) {
			basePath += ":" + port;
		}
		basePath += "/";

		return new TsmWikiModel(contextPath, basePath + "images/${image}", basePath + "search/eventsearch.htm?_id=${title}");
	}

	

}
