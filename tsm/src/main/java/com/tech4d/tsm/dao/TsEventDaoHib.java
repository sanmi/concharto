package com.tech4d.tsm.dao;

import java.io.Serializable;
import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.transaction.annotation.Transactional;

import com.tech4d.tsm.model.EventSearchText;
import com.tech4d.tsm.model.TsEvent;

@Transactional
public class TsEventDaoHib implements TsEventDao {
    private SessionFactory sessionFactory;

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.tech4d.tsm.lab.TsEventDao#save(com.tech4d.tsm.model.lab.TsEvent)
     */
    public Serializable save(TsEvent tsEvent) {
        //TODO First save a copy of the text to the MyISAM table.  Note MyISAM doesn't support 
        //transactions, so if this fails and the second succeeds, we may have a duplicate 
        //record later on 
        tsEvent.setEventSearchText(new EventSearchText(tsEvent));
        //now we can save
        return this.sessionFactory.getCurrentSession().save(tsEvent);
    }

    public void saveOrUpdate(TsEvent tsEvent) {
        //TODO First update the search text object (see note above)
        if (tsEvent.getEventSearchText() != null) {
            tsEvent.getEventSearchText().copyFrom(tsEvent);
        } else {
            tsEvent.setEventSearchText(new EventSearchText(tsEvent));
        }
        this.sessionFactory.getCurrentSession().saveOrUpdate(tsEvent);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.tech4d.tsm.lab.TsEventDao#delete(com.tech4d.tsm.model.lab.TsEvent)
     */
    public void delete(TsEvent tsEvent) {
        this.sessionFactory.getCurrentSession().delete(tsEvent);
    }

    /*
     * 
     * @see com.tech4d.tsm.lab.TsEventDao#delete(java.lang.Long)
     */
    public void delete(Long id) {
        //This may look goofy, but it is really ugly if you try to do it from an hql query 
        //because of all of the foreign keys.  This way, hibernate handles it for us.
        TsEvent event = new TsEvent();
        event.setId(id);
        this.sessionFactory.getCurrentSession().delete(event);
    }

    /**
     * Just for testing.  Do not use this in production
     * TODO figure out how to remove this from the "production" dao interface
     */
    public void deleteAll() {
        //TODO -  this is ugly, but it just for testing.  There don't
        //seem to be simpler cascade options using HQL delete
        List<TsEvent> tsEvents = this.findAll(100); //TODO
        for (TsEvent event : tsEvents) {
            this.delete(event);
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.tech4d.tsm.lab.TsEventDao#findAll()
     */
    @SuppressWarnings("unchecked")
    public List<TsEvent> findAll(int maxResults) {
        return this.sessionFactory.getCurrentSession().createQuery(
                "select tsEvent from TsEvent tsEvent").setMaxResults(maxResults).list();
    }

    public TsEvent findById(Long id) {
        return (TsEvent) this.sessionFactory.getCurrentSession().get(
                TsEvent.class, id);
    }

}
