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
package org.tsm.concharto.web.tags;

import info.bliki.wiki.model.WikiModel;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import org.tsm.concharto.web.wiki.WikiModelFactory;


/**
 * Custom taglib to render wikitext on a page
 * @author frank
 *
 */
public class WikiRender extends SimpleTagSupport {
	private String wikiText;

	public void setWikiText(String wikiText) {
		this.wikiText = wikiText;
	}

	@Override
	public void doTag() throws JspException, IOException {
		HttpServletRequest request = ((HttpServletRequest)((PageContext)this.getJspContext()).getRequest());

		WikiModel wikiModel = WikiModelFactory.newWikiModel(request);
		String rendered = wikiModel.render(wikiText);
		getJspContext().getOut().write(rendered);
	}

	
}
