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

import com.tech4d.tsm.model.Auditable;
import com.tech4d.tsm.model.Event;
import com.tech4d.tsm.model.PositionalAccuracy;
import com.tech4d.tsm.model.wiki.WikiText;

public interface EventDao {
    
    public abstract Serializable save(Event event);

    public void saveOrUpdate(Event event);
    
    public void saveOrUpdate(WikiText text); 
    
    public abstract void delete(Event event);

    public abstract void delete(Long id);

    public abstract List<Event> findRecent(int maxResults, int firstResult);

    public abstract List<Event> findRecent(String catalog, int maxResults, int firstResult);

    public abstract Event findById(Long id);
    
    public Auditable findById(Class<?> clazz, Long id);
    
    public Integer getTotalCount();

    public Integer getTotalCount(String catalog);
    
    public WikiText getDiscussion(Long eventId);

	public Serializable saveAuditable(Auditable auditable);

	public void saveOrUpdateAuditable(Auditable auditable);
	
	public List<PositionalAccuracy> getPositionalAccuracies();
	
	public Serializable save(PositionalAccuracy positionalAccuracy);
	
	public PositionalAccuracy getPositionalAccuracy(Long id);
	
}
