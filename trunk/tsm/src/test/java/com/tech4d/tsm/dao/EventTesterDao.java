package com.tech4d.tsm.dao;

import java.util.List;
import java.util.Set;

import org.hibernate.SessionFactory;

import com.tech4d.tsm.model.Event;
import com.tech4d.tsm.model.user.Role;

public interface EventTesterDao {

    public void setSessionFactory(SessionFactory sessionFactory);

    public SessionFactory getSessionFactory();

    /**
     * Just for testing.  Do not use this in production
     * TODO figure out how to remove this from the "production" dao interface
     */
    public void deleteAll();
    public void deleteUsers();
    public void deleteEvents();

    /*
     * (non-Javadoc)
     * 
     * @see com.tech4d.tsm.lab.EventDao#findAll()
     */
    public List<Event> findAll();
    public List<Event> findRecent(int maxResults, int firstResult);
    public void save(Set<Event> events);
    
    public void save(Role role);
    /**
     * For verifying counts in the db
     */
    public Long getCount(Class<?> clazz);

}