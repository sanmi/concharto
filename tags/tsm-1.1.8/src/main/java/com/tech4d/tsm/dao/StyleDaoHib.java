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

import org.hibernate.SessionFactory;
import org.springframework.transaction.annotation.Transactional;

import com.tech4d.tsm.model.kml.Style;

@Transactional
public class StyleDaoHib implements StyleDao {
    private SessionFactory sessionFactory;

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.tech4d.tsm.lab.StyleDao#save(com.tech4d.tsm.model.lab.Style)
     */
    public Serializable save(Style style) {
        return this.sessionFactory.getCurrentSession().save(style);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.tech4d.tsm.lab.StyleDao#delete(com.tech4d.tsm.model.lab.Style)
     */
    public void delete(Style style) {
        this.sessionFactory.getCurrentSession().delete(style);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.tech4d.tsm.lab.StyleDao#delete(java.lang.Long)
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
     * @see com.tech4d.tsm.lab.StyleDao#findAll()
     */
    @SuppressWarnings("unchecked")
    public List<Style> findAll() {
        return this.sessionFactory.getCurrentSession().createQuery(
                "select style from Style style").list();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.tech4d.tsm.lab.StyleDao#findById(java.lang.Long)
     */
    public Style find(Long id) {
        return (Style) this.sessionFactory.getCurrentSession().get(
                Style.class, id);
    }
}
