package com.tech4d.tsm.service;

import java.util.List;

import org.hibernate.SessionFactory;

import com.tech4d.tsm.model.TsEvent;
import com.tech4d.tsm.model.geometry.TimeRange;
import com.vividsolutions.jts.geom.Geometry;

public interface EventSearchService {

    public void setSessionFactory(SessionFactory sessionFactory);

    public SessionFactory getSessionFactory();

    public List<TsEvent> search(int maxResults, int firstResult, String textFilter, TimeRange timeRange,
            Geometry boundingBox);

    public Long getCount(String textFilter, TimeRange timeRange, Geometry boundingBox);
}