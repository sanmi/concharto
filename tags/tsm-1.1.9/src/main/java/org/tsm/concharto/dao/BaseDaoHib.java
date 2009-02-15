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

import org.hibernate.SessionFactory;
import org.springframework.transaction.annotation.Transactional;
import org.tsm.concharto.model.BaseEntity;
import org.tsm.concharto.model.Flag;


@Transactional
public class BaseDaoHib <T> implements BaseDao <T> {
    private SessionFactory sessionFactory;
    protected Class<T> clazz; 
    
    protected BaseDaoHib (Class<T> clazz) { 
        this.clazz = clazz;              
    }
    
    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    /* (non-Javadoc)
     * @see org.tsm.concharto.dao.BaseDao#setSessionFactory(org.hibernate.SessionFactory)
     */
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    /* (non-Javadoc)
     * @see org.tsm.concharto.dao.BaseDao#save(org.tsm.concharto.model.BaseEntity)
     */
    public Serializable save(T entity) {
        return this.sessionFactory.getCurrentSession().save(entity);
    }

    /* (non-Javadoc)
     * @see org.tsm.concharto.dao.BaseDao#saveOrUpdate(org.tsm.concharto.model.BaseEntity)
     */
    public void saveOrUpdate(T entity) {
        this.sessionFactory.getCurrentSession().saveOrUpdate(entity);
    }
    
    /* (non-Javadoc)
     * @see org.tsm.concharto.dao.BaseDao#delete(java.lang.Long)
     */
    public void delete(Long id) {
        BaseEntity entity = new Flag();
        entity.setId(id);
        this.sessionFactory.getCurrentSession().delete(entity);
    }

    /* (non-Javadoc)
     * @see org.tsm.concharto.dao.BaseDao#delete(org.tsm.concharto.model.BaseEntity)
     */
    public void delete(T entity) {
        this.sessionFactory.getCurrentSession().delete(entity);
    }

    @SuppressWarnings("unchecked")
    public T find(Long id) {
        return (T) this.sessionFactory.getCurrentSession().get(clazz, id);
    }

    @SuppressWarnings("unchecked")
    public List<T> findAll(int maxResults) {
        List<T> list = this.sessionFactory.getCurrentSession().
            createCriteria(clazz).
            setMaxResults(maxResults).
            list();
        return list;
    }
}
