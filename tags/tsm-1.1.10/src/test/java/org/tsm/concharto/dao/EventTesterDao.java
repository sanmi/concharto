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

import java.util.List;
import java.util.Set;

import org.hibernate.SessionFactory;
import org.tsm.concharto.model.Event;
import org.tsm.concharto.model.user.Role;


public interface EventTesterDao {

    public void setSessionFactory(SessionFactory sessionFactory);

    public SessionFactory getSessionFactory();

    /**
     * Just for testing.  Do not use this in production
     * TODO figure out how to remove this from the "production" dao interface
     */
    public void deleteAll();
    public void deleteUsers();
    public void deleteEvents();

    /*
     * (non-Javadoc)
     * 
     * @see org.tsm.concharto.lab.EventDao#findAll()
     */
    public List<Event> findAll();
    public List<Event> findRecent(int maxResults, int firstResult);
    public void save(Set<Event> events);
    
    public void save(Role role);
    /**
     * For verifying counts in the db
     */
    public Long getCount(Class<?> clazz);

}
