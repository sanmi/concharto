package com.tech4d.tsm.dao;

import java.io.Serializable;
import java.util.Collection;

import org.hibernate.SessionFactory;
import org.springframework.transaction.annotation.Transactional;

import com.tech4d.tsm.model.Event;

@Transactional
public class EventDaoHib implements EventDao {
    private SessionFactory sessionFactory;

    /* (non-Javadoc)
     * @see com.tech4d.tsm.dao.EventDao#setSessionFactory(org.hibernate.SessionFactory)
     */
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    /* (non-Javadoc)
     * @see com.tech4d.tsm.dao.EventDao#getSessionFactory()
     */
    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    /* (non-Javadoc)
     * @see com.tech4d.tsm.dao.EventDao#findAll()
     */
    @SuppressWarnings("unchecked")
    public Collection<Event> findAll() {
        Collection<Event> events = this.sessionFactory.getCurrentSession().createQuery(
                "from Event event").list();
        return events;
    }

    /* (non-Javadoc)
     * @see com.tech4d.tsm.dao.EventDao#save(com.tech4d.tsm.model.Event)
     */
    public Serializable save(Event event) {
        return this.sessionFactory.getCurrentSession().save(event);
    }
    
    /* (non-Javadoc)
     * @see com.tech4d.tsm.dao.EventDao#saveOrUpdate(com.tech4d.tsm.model.Event)
     */
    public void saveOrUpdate(Event event) {
        this.sessionFactory.getCurrentSession().saveOrUpdate(event);
    }
    
    /* (non-Javadoc)
     * @see com.tech4d.tsm.dao.EventDao#delete(com.tech4d.tsm.model.Event)
     */
    public void delete(Event event) {
        this.sessionFactory.getCurrentSession().delete(event);
    }
    
    /* (non-Javadoc)
     * @see com.tech4d.tsm.dao.EventDao#delete(java.lang.Long)
     */
    public void delete(Long id) {
        this.sessionFactory.getCurrentSession().createQuery(
            "delete Event event where event.id = ?").setLong(0, id)
            .executeUpdate();
    }
    
    public void deleteAll() {
        this.sessionFactory.getCurrentSession().createQuery(
            "delete from Feature").executeUpdate();
    }

    /* (non-Javadoc)
     * @see com.tech4d.tsm.dao.EventDao#findById(java.lang.Long)
     */
    public Event findById(Long id) {
        return (Event) this.sessionFactory.getCurrentSession().get(
                Event.class, id);
    }
}
