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
import org.tsm.concharto.model.kml.Style;


@Transactional
public class StyleDaoHib implements StyleDao {
    private SessionFactory sessionFactory;

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.tsm.concharto.lab.StyleDao#save(org.tsm.concharto.model.lab.Style)
     */
    public Serializable save(Style style) {
        return this.sessionFactory.getCurrentSession().save(style);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.tsm.concharto.lab.StyleDao#delete(org.tsm.concharto.model.lab.Style)
     */
    public void delete(Style style) {
        this.sessionFactory.getCurrentSession().delete(style);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.tsm.concharto.lab.StyleDao#delete(java.lang.Long)
     */
    public void delete(Long id) {
        this.sessionFactory
                .getCurrentSession()
                .createQuery(
                        "delete from TsGeometry where id = (select id from Style where id = ?)")
                .setLong(0, id).executeUpdate();
        this.sessionFactory.getCurrentSession().createQuery(
                "delete Style style where style.id = ?").setLong(0, id)
                .executeUpdate();
    }

    /**
     * Just for testing
     */
    public void deleteAll() {
        this.sessionFactory.getCurrentSession().createQuery(
                "delete from TsGeometry where id in (select id from Style)")
                .executeUpdate();
        this.sessionFactory.getCurrentSession().createQuery(
            "delete from Style").executeUpdate();
    }
     
    /*
     * (non-Javadoc)
     * 
     * @see org.tsm.concharto.lab.StyleDao#findAll()
     */
    @SuppressWarnings("unchecked")
    public List<Style> findAll() {
        return this.sessionFactory.getCurrentSession().createQuery(
                "select style from Style style").list();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.tsm.concharto.lab.StyleDao#findById(java.lang.Long)
     */
    public Style find(Long id) {
        return (Style) this.sessionFactory.getCurrentSession().get(
                Style.class, id);
    }
}
