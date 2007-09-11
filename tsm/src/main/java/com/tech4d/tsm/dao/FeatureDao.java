package com.tech4d.tsm.dao;

import java.io.Serializable;
import java.util.List;

import com.tech4d.tsm.model.geometry.Feature;
import com.vividsolutions.jts.geom.Geometry;

public interface FeatureDao {
    public abstract Serializable save(Feature feature);

    public abstract void delete(Feature Feature);

    public abstract void deleteAll();

    public abstract void delete(Long id);

    public abstract List<Feature> findAll();

    public abstract Feature findById(Long id);

    public abstract List<Feature> findWithinGeometry(Geometry geometry);

}
