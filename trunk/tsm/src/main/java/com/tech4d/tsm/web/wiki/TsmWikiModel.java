package com.tech4d.tsm.web.wiki;

import info.bliki.wiki.model.Configuration;
import info.bliki.wiki.model.WikiModel;

public class TsmWikiModel extends WikiModel {
	private String basePath; //full base url plus port number
	private static final Configuration config = Configuration.DEFAULT_CONFIGURATION;
	static {
		
		config.getTokenMap().clear();
	}
	
	public TsmWikiModel(String basePath, String imageBaseURL, String linkBaseURL) {
		super(config, imageBaseURL, linkBaseURL);
		this.basePath = basePath;
	}

	@Override
	public String render(String rawWikiText) {
		// TODO Auto-generated method stub
		String substituted = SubstitutionMacro.postSignature(basePath, rawWikiText);
		String rendered = super.render(substituted);
		return rendered;
	}
	
	

}
