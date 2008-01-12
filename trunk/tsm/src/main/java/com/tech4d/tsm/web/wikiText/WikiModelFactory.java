package com.tech4d.tsm.web.wikiText;

import info.bliki.wiki.model.WikiModel;

import javax.servlet.http.HttpServletRequest;

import com.tech4d.tsm.web.util.UrlFormat;


public class WikiModelFactory {

	public static WikiModel newWikiModel(HttpServletRequest request) {
		String basePath = UrlFormat.getBasepath(request);
		
		return new TsmWikiModel(basePath, basePath + "images/${image}", basePath + "search/eventsearch.htm?_id=${title}");
	}

	

}
