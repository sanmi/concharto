package com.tech4d.tsm.dao;

import java.util.Collection;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.tech4d.tsm.model.Event;

public class EventDao extends HibernateDaoSupport {

    public Collection<Event> loadAll() {
        return this.getHibernateTemplate().find("from Event event");
    }
}
