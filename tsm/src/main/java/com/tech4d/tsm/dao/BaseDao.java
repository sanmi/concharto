package com.tech4d.tsm.dao;

import java.io.Serializable;
import java.util.List;

import org.hibernate.SessionFactory;

/**
 * Based dao class with standard methods
 */
public interface BaseDao <T>{

    public abstract SessionFactory getSessionFactory();
    
    public abstract void setSessionFactory(SessionFactory sessionFactory);

    public abstract Serializable save(T entity);
    
    public abstract void saveOrUpdate(T entity);

    public abstract void delete(Long id);

    public abstract void delete(T entity);
    
    public abstract T find(Long id);
    
    public abstract List<T> findAll(int maxResults);

}