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
package org.tsm.concharto.dao;

import java.io.Serializable;
import java.util.Map;

import org.tsm.concharto.model.wiki.WikiText;


public interface WikiTextDao {
	public WikiText get(Long id);
	public WikiText find(String title);
	public Boolean exists(String title);
	public Map<String,Long> exists(String[] titles);
	public Serializable save(WikiText wikiText);
	public void saveOrUpdate(WikiText wikiText);
	public void delete(Long id);
}
