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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.transaction.annotation.Transactional;
import org.tsm.concharto.model.Event;
import org.tsm.concharto.model.EventSearchText;
import org.tsm.concharto.model.user.Role;


@Transactional
public class EventTesterDaoHib implements EventTesterDao {
    private SessionFactory sessionFactory;
    protected final Log logger = LogFactory.getLog(this.getClass());

    /* (non-Javadoc)
     * @see org.tsm.concharto.lab.EventTesterDao#setSessionFactory(org.hibernate.SessionFactory)
     */
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
    /* (non-Javadoc)
     * @see org.tsm.concharto.lab.EventTesterDao#getSessionFactory()
     */
    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    /* (non-Javadoc)
     * @see org.tsm.concharto.lab.EventTesterDao#deleteAll()
     */
    public void deleteAll() {
        //TODO -  this is ugly, but it offers the best performance.  There don't
        //seem to be simpler cascade options using HQL delete
        deleteEvents();
        deleteUsers();
        this.sessionFactory.getCurrentSession().createSQLQuery("delete from Role").executeUpdate();
    }
    
    public void deleteUsers() {
        this.sessionFactory.getCurrentSession().createSQLQuery("delete from User_Role").executeUpdate();
        this.sessionFactory.getCurrentSession().createSQLQuery("delete from User").executeUpdate();
    }
    
    public void deleteEvents() {
        this.sessionFactory.getCurrentSession().createSQLQuery("delete from TsGeometry").executeUpdate();
        this.sessionFactory.getCurrentSession().createSQLQuery("delete from Event_UserTag").executeUpdate();
        this.sessionFactory.getCurrentSession().createSQLQuery("delete from UserTag").executeUpdate();
        this.sessionFactory.getCurrentSession().createSQLQuery("delete from User_Role").executeUpdate();
        this.sessionFactory.getCurrentSession().createSQLQuery("delete from Spotlight").executeUpdate();
        this.sessionFactory.getCurrentSession().createSQLQuery("delete from Notification").executeUpdate();
        this.sessionFactory.getCurrentSession().createSQLQuery("delete from User").executeUpdate();
        this.sessionFactory.getCurrentSession().createSQLQuery("delete from UserNote").executeUpdate();
        this.sessionFactory.getCurrentSession().createSQLQuery("delete from Flag").executeUpdate();
        this.sessionFactory.getCurrentSession().createSQLQuery("delete from Event").executeUpdate();
        this.sessionFactory.getCurrentSession().createSQLQuery("delete from WikiText").executeUpdate();
        this.sessionFactory.getCurrentSession().createSQLQuery("delete from EventSearchText").executeUpdate();
        this.sessionFactory.getCurrentSession().createSQLQuery("delete from TimePrimitive").executeUpdate();
        this.sessionFactory.getCurrentSession().createSQLQuery("delete from AuditFieldChange").executeUpdate();
        this.sessionFactory.getCurrentSession().createSQLQuery("delete from AuditEntry").executeUpdate();
        this.sessionFactory.getCurrentSession().createSQLQuery("delete from PositionalAccuracy").executeUpdate();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.tsm.concharto.lab.EventDao#findAll()
     */
    /* (non-Javadoc)
     * @see org.tsm.concharto.lab.EventTesterDao#findAll()
     */
    @SuppressWarnings("unchecked")
    public List<Event> findAll() {
        return this.sessionFactory.getCurrentSession().createQuery(
                "select event from Event event").list();
    }

    @SuppressWarnings("unchecked")
    public List<Event> findRecent(int maxResults, int firstResult) {
        return this.sessionFactory.getCurrentSession().createQuery(
                "select event from Event event order by created desc")
                .setMaxResults(maxResults)
                .setFirstResult(firstResult)
                .list();
    }

    /* (non-Javadoc)
     * @see org.tsm.concharto.lab.EventTesterDao#save(java.util.Set)
     */
    public void save(Set<Event> events) {
        
        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();

        int i=0;
        for (Event event : events) {
            i++;
            event.setEventSearchText(new EventSearchText(event));
            session.save(event);
            if ( i % 1000 == 0 ) { //1000, same as the JDBC batch size
                //flush a batch of inserts and release memory:
                session.flush();
                session.clear();
                //log.debug("saving " + i);
            }
        }
        tx.commit();
        session.close();    
    }

    /**
     * Just for testing.  Normally, roles are loaded by hand
     * @param role object to save
     */
    public void save(Role role) {
        this.sessionFactory.getCurrentSession().save(role);
    }
    
    /**
     * For verifying counts in the db
     */
    @SuppressWarnings("unchecked")
	public Long getCount(Class<?> clazz) {
    	List results = this.sessionFactory.getCurrentSession().createQuery(
    			"select count(*) from " + clazz.getSimpleName()
    		).list();
    	return (Long) results.get(0);
    }
}
