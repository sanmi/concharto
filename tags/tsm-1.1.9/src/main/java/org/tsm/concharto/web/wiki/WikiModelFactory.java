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
package org.tsm.concharto.web.wiki;

import info.bliki.wiki.model.WikiModel;

import javax.servlet.http.HttpServletRequest;

import org.tsm.concharto.web.util.UrlFormat;


/**
 * Create a wikimodel given the request
 * @author frank
 *
 */
public class WikiModelFactory {

	public static WikiModel newWikiModel(HttpServletRequest request) {
		String basePath = UrlFormat.getBasepath(request);
		
		return new TsmWikiModel(basePath, basePath + "images/${image}", basePath + "page.htm?page=${title}");
	}

	

}
