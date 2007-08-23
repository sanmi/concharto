package com.tech4d.tsm.dao;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.tech4d.tsm.model.Event;

public class EventDao extends HibernateDaoSupport {

    public Collection<Event> findAll() {
        return this.getHibernateTemplate().find("from Event event");
    }

    public Serializable save(Event event) {
        return this.getHibernateTemplate().save(event);
    }
    
    public void saveOrUpdate(Event event) {
        this.getHibernateTemplate().saveOrUpdate(event);
    }
    
    public void delete(Event event) {
        this.getHibernateTemplate().delete(event);
    }
    
    public void delete(Long id) {
        List events = this.getHibernateTemplate().find("from Event event where event.id = ?", id);
        this.getHibernateTemplate().deleteAll(events);
    }
    
    protected void deleteAll() {
        List events = this.getHibernateTemplate().loadAll(Event.class);
        this.getHibernateTemplate().deleteAll(events);
    }

    public Event findById(Long id) {
        return (Event) this.getHibernateTemplate().get(Event.class, id);
    }
}
