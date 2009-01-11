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
package com.tech4d.tsm.dao;

import java.io.Serializable;
import java.util.List;

import com.tech4d.tsm.model.Spotlight;

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
