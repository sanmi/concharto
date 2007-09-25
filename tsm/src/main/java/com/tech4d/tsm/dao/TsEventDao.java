package com.tech4d.tsm.dao;

import java.io.Serializable;
import java.util.List;

import org.hibernate.SessionFactory;

import com.tech4d.tsm.model.TsEvent;
import com.tech4d.tsm.model.geometry.TimeRange;
import com.vividsolutions.jts.geom.Geometry;

public interface TsEventDao {
    public abstract SessionFactory getSessionFactory();
    
    public abstract Serializable save(TsEvent tsEvent);

    public void saveOrUpdate(TsEvent tsEvent);

    public abstract void delete(TsEvent tsEvent);

    public abstract void deleteAll();

    public abstract void delete(Long id);

    public abstract List<TsEvent> findAll();

    public abstract TsEvent findById(Long id);

    public abstract List<TsEvent> findWithinGeometry(Geometry geometry);

    public List<TsEvent> search(String textFilter, TimeRange timeRange, Geometry boundingBox);
}
