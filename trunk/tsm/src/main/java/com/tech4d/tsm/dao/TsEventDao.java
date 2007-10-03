package com.tech4d.tsm.dao;

import java.io.Serializable;
import java.util.List;

import com.tech4d.tsm.model.TsEvent;

public interface TsEventDao {
    
    public abstract Serializable save(TsEvent tsEvent);

    public void saveOrUpdate(TsEvent tsEvent);

    public abstract void delete(TsEvent tsEvent);

    public abstract void delete(Long id);

    public abstract List<TsEvent> findAll(int maxResults);

    public abstract TsEvent findById(Long id);
}
