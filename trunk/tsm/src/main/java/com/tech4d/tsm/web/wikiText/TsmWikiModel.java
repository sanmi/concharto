package com.tech4d.tsm.web.wikiText;

import info.bliki.wiki.model.WikiModel;

public class TsmWikiModel extends WikiModel {
	private String basePath; //full base url plus port number
	
	public TsmWikiModel(String basePath, String imageBaseURL, String linkBaseURL) {
		super(imageBaseURL, linkBaseURL);
		this.basePath = basePath;
	}

	@Override
	public String render(String rawWikiText) {
		// TODO Auto-generated method stub
		String rendered = super.render(rawWikiText);
		rendered = SubstitutionMacro.postSignature(basePath, rendered);
		return rendered;
	}
	
	

}
