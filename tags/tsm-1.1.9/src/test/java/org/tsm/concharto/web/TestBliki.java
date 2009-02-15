/*******************************************************************************
 * Copyright 2009 Time Space Map, LLC
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package org.tsm.concharto.web;

import static org.junit.Assert.*;
import info.bliki.wiki.model.WikiModel;

import org.apache.commons.lang.StringUtils;
import org.junit.Test;

public class TestBliki {
	
	private static final String WIKI_MARKUP = "[[List]]: \n#1 \n#2 \n#3";

	@Test public void render() {
		
		WikiModel wikiModel = new WikiModel("http://www.concharto.com/images/${image}", 
			"http://www.concharto.com/${title}");
		String results = wikiModel.render(WIKI_MARKUP);
		System.out.println(results);
		//there should be one url in the string
		assertTrue(-1 != results.indexOf("concharto"));
		//there should be three <li> items in the string
		assertEquals(4, StringUtils.splitByWholeSeparator(results,"<li>").length);

	}
}
