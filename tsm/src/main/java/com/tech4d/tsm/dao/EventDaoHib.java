package com.tech4d.tsm.dao;

import java.io.Serializable;
import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.transaction.annotation.Transactional;

import com.tech4d.tsm.model.EventSearchText;
import com.tech4d.tsm.model.Event;

@Transactional
public class EventDaoHib implements EventDao {
    private SessionFactory sessionFactory;

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.tech4d.tsm.lab.EventDao#save(com.tech4d.tsm.model.lab.Event)
     */
    public Serializable save(Event event) {
        //TODO First save a copy of the text to the MyISAM table.  Note MyISAM doesn't support 
        //transactions, so if this fails and the second succeeds, we may have a duplicate 
        //record later on 
        event.setEventSearchText(new EventSearchText(event));
        //now we can save
        return this.sessionFactory.getCurrentSession().save(event);
    }

    public void saveOrUpdate(Event event) {
        //TODO First update the search text object (see note above)
        if (event.getEventSearchText() != null) {
            event.getEventSearchText().copyFrom(event);
        } else {
            event.setEventSearchText(new EventSearchText(event));
        }
        this.sessionFactory.getCurrentSession().saveOrUpdate(event);
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
     * @see com.tech4d.tsm.lab.EventDao#findAll()
     */
    @SuppressWarnings("unchecked")
    public List<Event> findAll(int maxResults) {
        return this.sessionFactory.getCurrentSession().createQuery(
                "select event from Event event").setMaxResults(maxResults).list();
    }

    public Event findById(Long id) {
        return (Event) this.sessionFactory.getCurrentSession().get(
                Event.class, id);
    }

}
