package com.tech4d.tsm.dao;

import java.io.Serializable;
import java.util.List;

import com.tech4d.tsm.model.geometry.Style;
import com.vividsolutions.jts.geom.Geometry;

public interface StyleDao {
    public abstract Serializable save(Style feature);

    public abstract void delete(Style Style);

    public abstract void deleteAll();

    public abstract void delete(Long id);

    public abstract List<Style> findAll();

    public abstract Style findById(Long id);

}
