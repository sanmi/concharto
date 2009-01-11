/*******************************************************************************
 * ***** BEGIN LICENSE BLOCK *****
 * Version: MPL 1.1
 * 
 * The contents of this file are subject to the Mozilla Public License Version 
 * 1.1 (the "License"); you may not use this file except in compliance with 
 * the License. You may obtain a copy of the License at 
 * http://www.mozilla.org/MPL/
 * 
 * Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 * for the specific language governing rights and limitations under the
 * License.
 * 
 * The Original Code is Concharto.
 * 
 * The Initial Developer of the Original Code is
 * Time Space Map, LLC
 * Portions created by the Initial Developer are Copyright (C) 2007 - 2009
 * the Initial Developer. All Rights Reserved.
 * 
 * Contributor(s):
 * Time Space Map, LLC
 * 
 * ***** END LICENSE BLOCK *****
 ******************************************************************************/
package com.tech4d.tsm.web.wiki;

import info.bliki.wiki.model.Configuration;
import info.bliki.wiki.model.WikiModel;

import org.htmlcleaner.ContentToken;
import org.htmlcleaner.TagNode;
import org.htmlcleaner.Utils;

/**
 * Renders wikipedia markup to HTML.  
 * NOTE: There may be some cross site scripting vulnerabilities in doing this.  
 * See note on TagNode.java and http://openmya.hacker.jp/hasegawa/security/expression.txt
 * 
 * In order to avoid XSS all together, we can simply add the following:
 * <code>
 * static {	
 *   config.getTokenMap().clear();
 * }
 * </code>
 * But that would exclude all additional HTML tags from the content
 * 
 * @author frank
 *
 */
public class TsmWikiModel extends WikiModel  {
	private String basePath; //full base url plus port number
	private static final Configuration config = Configuration.DEFAULT_CONFIGURATION;
	static {	
		//don't allow <a href>.  Google kml and maplets don't allow it.
		config.getTokenMap().remove("a"); 
	}
	public TsmWikiModel(String basePath, String imageBaseURL, String linkBaseURL) {
		super(imageBaseURL, linkBaseURL);
		this.basePath = basePath;
	}

	@Override
	public String render(String rawWikiText) {
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
		aTagNode.addAttribute("href", link, true);
		aTagNode.addAttribute("class", "externallink", true);
		aTagNode.addAttribute("title", link, true);
		aTagNode.addAttribute("rel", "nofollow", true);
		aTagNode.addAttribute("target", "_top", true);
		aTagNode.addChild(new ContentToken(linkName));
	}
}
