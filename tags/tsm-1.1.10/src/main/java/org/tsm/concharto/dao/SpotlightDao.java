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
import java.util.List;

import org.tsm.concharto.model.Spotlight;


public interface SpotlightDao {
	public Serializable save(Spotlight spotlight);
	public void delete(Spotlight spotlight);
	public void delete(Long id);
	public Spotlight find(Long id);
	public List<Spotlight> find(int maxResults, int firstResult);
	public List<Spotlight> findAll();
	public List<Spotlight> findAll(String catalog);
	public Spotlight getVisible(int position);
	public Integer getTotalVisible();
	public Integer getTotalVisible(String catalog);
	public List<Spotlight> findVisible();
	public List<Spotlight> findVisible(String catalog);
}
