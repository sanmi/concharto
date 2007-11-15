package com.tech4d.tsm.dao;

import java.io.Serializable;
import java.util.List;

import com.tech4d.tsm.model.Event;

public interface EventDao {
    
    public abstract Serializable save(Event event);

    public void saveOrUpdate(Event event);

    public abstract void delete(Event event);

    public abstract void delete(Long id);

    public abstract List<Event> findRecent(int maxResults);

    public abstract Event findById(Long id);
    
    public Integer getTotalCount();
}
