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

import org.tsm.concharto.model.Auditable;
import org.tsm.concharto.model.Event;
import org.tsm.concharto.model.PositionalAccuracy;
import org.tsm.concharto.model.wiki.WikiText;


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
