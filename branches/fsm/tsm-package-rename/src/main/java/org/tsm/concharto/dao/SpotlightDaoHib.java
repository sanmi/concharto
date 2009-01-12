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
package org.tsm.concharto.dao;

import java.io.Serializable;
import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.transaction.annotation.Transactional;
import org.tsm.concharto.model.Spotlight;


@Transactional
public class SpotlightDaoHib implements SpotlightDao {
    private static final String FIELD_CATALOG = "catalog";
    private static final String SQL_PRE = "select spotlight from Spotlight spotlight ";
    private static final String SQL_CATALOG = " where catalog = :catalog ";
    private static final String SQL_SELECT_ALL = 
    	SQL_PRE + " order by id asc";
    private static final String SQL_SELECT_VISIBLE = 
    	SQL_PRE + " where spotlight.visible = true order by id asc";
    private static final String SQL_SELECT_ALL_CATALOG = 
    	SQL_PRE + SQL_CATALOG + " order by id asc";
    private static final String SQL_SELECT_VISIBLE_CATALOG = 
    	SQL_PRE + SQL_CATALOG + " and spotlight.visible = true order by id asc";
	private SessionFactory sessionFactory;

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
    
	public void delete(Spotlight spotlight) {
		this.sessionFactory.getCurrentSession().delete(spotlight);
	}

	public void delete(Long id) {
		Spotlight spotlight = new Spotlight();
		spotlight.setId(id);
		delete(spotlight);
	}

	public Spotlight find(Long id) {
		return (Spotlight) this.sessionFactory.getCurrentSession().get(
				Spotlight.class, id);
	}

	public List<Spotlight> find(int maxResults, int firstResult) {
		return find(SQL_SELECT_ALL, maxResults, firstResult);
	}

	@SuppressWarnings("unchecked")
	private List<Spotlight> find(String sql, int maxResults, int firstResult) {
		return this.sessionFactory.getCurrentSession().createQuery(
				sql).setMaxResults(maxResults)
				.setFirstResult(firstResult).list();
	}

	@SuppressWarnings("unchecked")
	public List<Spotlight> findAll() {
		return this.sessionFactory.getCurrentSession().createQuery(
        SQL_SELECT_ALL).list();
	}

	@SuppressWarnings("unchecked")
	public List<Spotlight> findAll(String catalog) {
		return this.sessionFactory.getCurrentSession().createQuery(
				SQL_SELECT_ALL_CATALOG)
				.setString(FIELD_CATALOG, catalog)
				.list();
	}
	
	public Serializable save(Spotlight spotlight) {
		 return this.sessionFactory.getCurrentSession().save(spotlight);
	}

	public Spotlight getVisible(int position) {
		List<Spotlight> spotlights = find(SQL_SELECT_VISIBLE, 1, position);
		return (Spotlight) DaoHelper.getOnlyFirst(spotlights);
	}

	@SuppressWarnings("unchecked")
	public Integer getTotalVisible() {
		List results = this.sessionFactory.getCurrentSession()
    		.createQuery("select count(spotlight) from Spotlight spotlight where spotlight.visible = true")
    		.list();
    	Long count = (Long) results.get(0);
    	//cast to Integer.  It aint never going to be bigger!
    	return Math.round(count);	
    	}

	@SuppressWarnings("unchecked")
	public Integer getTotalVisible(String catalog) {
		List results = this.sessionFactory.getCurrentSession()
		.createQuery("select count(spotlight) from Spotlight spotlight where catalog = :catalog spotlight.visible = true")
		.setString(FIELD_CATALOG, catalog)
		.list();
		Long count = (Long) results.get(0);
		//cast to Integer.  It aint never going to be bigger!
		return Math.round(count);	
	}
	
	@SuppressWarnings("unchecked")
	public List<Spotlight> findVisible() {
		return this.sessionFactory.getCurrentSession().createQuery(
		        SQL_SELECT_VISIBLE).list();
	}

	@SuppressWarnings("unchecked")
	public List<Spotlight> findVisible(String catalog) {
		return this.sessionFactory.getCurrentSession().createQuery(
				SQL_SELECT_VISIBLE_CATALOG).setString(FIELD_CATALOG, catalog).list();
	}
	
}
