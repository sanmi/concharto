package com.tech4d.tsm.web.wiki;

import info.bliki.wiki.model.Configuration;
import info.bliki.wiki.model.WikiModel;

import org.htmlcleaner.ContentToken;
import org.htmlcleaner.TagNode;
import org.htmlcleaner.Utils;

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

	/**
	 * TODO FIXME This is a hack because I don't know a better way to customize the link.  We 
	 * need a target="_top" to support embedding in iframes, so I cut this function from the
	 * bliki source 3.0.1 and pasted it here.!!! 
	 */
	@Override
	public void appendExternalLink(String link, String linkName, boolean withoutSquareBrackets) {
		// is it an image?
		link = Utils.escapeXml(link, true, false, false);
		int indx = link.lastIndexOf(".");
		if (indx > 0 && indx < (link.length() - 3)) {
			String ext = link.substring(indx + 1);
			if (ext.equalsIgnoreCase("gif") || ext.equalsIgnoreCase("png") || ext.equalsIgnoreCase("jpg") || ext.equalsIgnoreCase("jpeg")
					|| ext.equalsIgnoreCase("bmp")) {
				appendExternalImageLink(link, linkName);
				return;
			}
		}
		TagNode aTagNode = new TagNode("a");
		append(aTagNode);
		aTagNode.addAttribute("href", link);
		aTagNode.addAttribute("class", "externallink");
		aTagNode.addAttribute("title", link);
		aTagNode.addAttribute("rel", "nofollow");
		aTagNode.addAttribute("target", "_top");
		aTagNode.addChild(new ContentToken(linkName));
	}


}
