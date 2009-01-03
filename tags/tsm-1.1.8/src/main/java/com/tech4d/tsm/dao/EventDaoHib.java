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

import com.tech4d.tsm.model.Event;
import com.tech4d.tsm.model.Auditable;
import com.tech4d.tsm.model.EventSearchText;
import com.tech4d.tsm.model.PositionalAccuracy;
import com.tech4d.tsm.model.wiki.WikiText;

@Transactional
public class EventDaoHib implements EventDao {
    private SessionFactory sessionFactory;
    private static final String HQL_VISIBLE_CLAUSE = " where ((event.visible = null) or (event.visible = true)) ";

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.tech4d.tsm.lab.EventDao#save(com.tech4d.tsm.model.lab.Event)
     */
    public Serializable save(Event event) {
    	return saveAuditable(event);
    }
    
    public Serializable saveAuditable(Auditable auditable) {
    	//-----------------------------------------------------------
    	//TODO -- make an adapter for saving auditables rather than hard coding it here
    	//-----------------------------------------------------------
    	if (auditable instanceof Event) {
            //TODO First save a copy of the text to the MyISAM table.  Note MyISAM doesn't support 
            //transactions, so if this fails and the second succeeds, we may have a duplicate 
            //record later on 
            ((Event)auditable).setEventSearchText(new EventSearchText((Event) auditable));
    	}
        //now we can save
        return this.sessionFactory.getCurrentSession().save(auditable);   	
    }

    public void saveOrUpdate(Event event) {
    	saveOrUpdateAuditable(event);
    }
    
    public void saveOrUpdateAuditable(Auditable auditable) {
    	//-----------------------------------------------------------
    	//TODO -- make an adapter for saving auditables rather than hard coding it here
    	//-----------------------------------------------------------
        //TODO First update the search text object (see note above)
    	if (auditable instanceof Event) {
            if (((Event) auditable).getEventSearchText() != null) {
            	((Event) auditable).getEventSearchText().copyFrom((Event) auditable);
            } else {
            	((Event) auditable).setEventSearchText(new EventSearchText((Event) auditable));
            }    		 
    	}
        this.sessionFactory.getCurrentSession().saveOrUpdate(auditable);
    	
    }

    /**
     * TODO probably belongs in a different dao?
     */
    public void saveOrUpdate(WikiText text) {
        this.sessionFactory.getCurrentSession().saveOrUpdate(text);
    	
    }


    /*
     * (non-Javadoc)
     * 
     * @see com.tech4d.tsm.lab.EventDao#delete(com.tech4d.tsm.model.lab.Event)
     */
    public void delete(Event event) {
        this.sessionFactory.getCurrentSession().delete(event);
    }

    /*
     * 
     * @see com.tech4d.tsm.lab.EventDao#delete(java.lang.Long)
     */
    public void delete(Long id) {
        //This may look goofy, but it is really ugly if you try to do it from an hql query 
        //because of all of the foreign keys.  This way, hibernate handles it for us.
        Event event = new Event();
        event.setId(id);
        this.sessionFactory.getCurrentSession().delete(event);
    }
   
    /*
     * (non-Javadoc)
     * 
     * @see com.tech4d.tsm.lab.EventDao#findRecent()
     */
    @SuppressWarnings("unchecked")
    public List<Event> findRecent(int maxResults, int firstResult) {
        return this.sessionFactory.getCurrentSession().createQuery(
                "select event from Event event " + HQL_VISIBLE_CLAUSE + "order by created desc")
                .setMaxResults(maxResults)
                .setFirstResult(firstResult)
                .list();
    }

    @SuppressWarnings("unchecked")
	public List<Event> findRecent(String catalog, int maxResults, int firstResult) {
        return this.sessionFactory.getCurrentSession().createQuery(
                "select event from Event event " + HQL_VISIBLE_CLAUSE 
                	+ " and catalog = :catalog order by created desc")
                .setString("catalog", catalog)
                .setMaxResults(maxResults)
                .setFirstResult(firstResult)
                .list();
    }

    public Event findById(Long id) {
        return (Event)findById(Event.class, id);
    }

    public Auditable findById(Class<?> clazz, Long id) {
    	return (Auditable) this.sessionFactory.getCurrentSession().get(
    			clazz, id);
    }
    
    @SuppressWarnings("unchecked")
	public Integer getTotalCount() {
    	List results = this.sessionFactory.getCurrentSession()
    	.createQuery("select count(event) from Event event " + HQL_VISIBLE_CLAUSE)
    	.list();
    	Long count = (Long) results.get(0);
    	//cast to Integer.  It aint never going to be bigger!
    	return Math.round(count);
    }

    @SuppressWarnings("unchecked")
	public Integer getTotalCount(String catalog) {
    	List results = this.sessionFactory.getCurrentSession()
    	.createQuery("select count(event) from Event event " + HQL_VISIBLE_CLAUSE 
    			+ " and catalog = :catalog")
    	.setString("catalog", catalog)
    	.list();
    	Long count = (Long) results.get(0);
    	//cast to Integer.  It aint never going to be bigger!
    	return Math.round(count);
    }
    
	@SuppressWarnings("unchecked")
	public WikiText getDiscussion(Long eventId) {
    	List results = this.sessionFactory.getCurrentSession()
    	.createQuery("select event.discussion from Event event where event.id = ?)")
    	.setLong(0, eventId)
    	.list();
    	if (0 == results.size()) {
    		return null;
    	} else {
    		return (WikiText) results.get(0);
    	}
	}

	@SuppressWarnings("unchecked")
	public List<PositionalAccuracy> getPositionalAccuracies() {
		
		return this.sessionFactory.getCurrentSession().createQuery("select pa from PositionalAccuracy pa order by id asc").list();
	}
	
    public Serializable save(PositionalAccuracy positionalAccuracy) {
    	return this.sessionFactory.getCurrentSession().save(positionalAccuracy);
    }
	 
	public PositionalAccuracy getPositionalAccuracy(Long id) {
		return (PositionalAccuracy) this.sessionFactory.getCurrentSession().load(PositionalAccuracy.class, id);
	}

}
