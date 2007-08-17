package com.tech4d.tsm.dao;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.tech4d.tsm.model.Event;

import java.util.Collection;

public class EventDao extends HibernateDaoSupport {

    public Collection<Event> loadAll() {
        return this.getHibernateTemplate().find("from Event event");
    }
}
