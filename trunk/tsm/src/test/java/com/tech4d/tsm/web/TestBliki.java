package com.tech4d.tsm.web;

import static org.junit.Assert.*;
import info.bliki.wiki.model.WikiModel;

import org.apache.commons.lang.StringUtils;
import org.junit.Test;

public class TestBliki {
	
	private static final String WIKI_MARKUP = "[[List]]: \n#1 \n#2 \n#3";

	@Test public void render() {
		
		WikiModel wikiModel = new WikiModel("http://www.timespacemap.com/images/${image}", 
			"http://www.timespacemap.com/${title}");
		String results = wikiModel.render(WIKI_MARKUP);
		System.out.println(results);
		//there should be one url in the string
		assertTrue(-1 != results.indexOf("timespacemap"));
		//there should be three <li> items in the string
		assertEquals(4, StringUtils.splitByWholeSeparator(results,"<li>").length);

	}
}
