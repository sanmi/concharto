package com.tech4d.tsm.dao;

import java.util.List;
import java.util.Set;

import org.hibernate.SessionFactory;

import com.tech4d.tsm.model.Event;

public interface EventTesterDao {

    public void setSessionFactory(SessionFactory sessionFactory);

    public SessionFactory getSessionFactory();

    /**
     * Just for testing.  Do not use this in production
     * TODO figure out how to remove this from the "production" dao interface
     */
    public void deleteAll();

    /*
     * (non-Javadoc)
     * 
     * @see com.tech4d.tsm.lab.EventDao#findAll()
     */
    @SuppressWarnings("unchecked")
    public List<Event> findAll();

    public void save(Set<Event> events);

}