package com.tech4d.tsm.lab;

import java.io.Serializable;
import java.util.List;

import com.tech4d.tsm.model.lab.Address;
import com.vividsolutions.jts.geom.Geometry;

public interface AddressDao {

    public abstract Serializable save(Address address);

    public abstract void delete(Address Address);

    public abstract void deleteAll();

    public abstract void delete(Long id);

    public abstract List<Address> findAll();

    public abstract Address findById(Long id);

    public abstract List<Address> findWithinGeometry(Geometry geometry);

    List<Address> findGeomWithinGeometry(Geometry geometry);

}