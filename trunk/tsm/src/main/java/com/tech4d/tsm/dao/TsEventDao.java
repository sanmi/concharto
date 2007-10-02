package com.tech4d.tsm.dao;

import com.tech4d.tsm.model.TsEvent;
import org.hibernate.SessionFactory;

import java.io.Serializable;
import java.util.List;

public interface TsEventDao {
    public abstract SessionFactory getSessionFactory();
    
    public abstract Serializable save(TsEvent tsEvent);

    public void saveOrUpdate(TsEvent tsEvent);

    public abstract void delete(TsEvent tsEvent);

    public abstract void delete(Long id);

    public abstract List<TsEvent> findAll(int maxResults);

    public abstract TsEvent findById(Long id);
}
