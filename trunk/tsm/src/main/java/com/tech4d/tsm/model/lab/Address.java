package com.tech4d.tsm.model.lab;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Type;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryCollection;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;

@Entity
@Table(name = "ADDRESS")
@org.hibernate.annotations.Table(comment = "LAB Table - not for production.  ENGINE : MyISAM", appliesTo = "ADDRESS")
public class Address {
    private Long id;

    private String address;

    private Point addressLocation;

    private GeometryCollection geom;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Type(type = "com.tech4d.tsm.model.GeometryUserType")
    @Column(name = "addressLocation", columnDefinition = "POINT", nullable = false)
    public Point getAddress_loc() {
        return addressLocation;
    }

    public void setAddress_loc(Point address_loc) {
        this.addressLocation = address_loc;
    }

    @Type(type = "com.tech4d.tsm.model.GeometryUserType")
    @Column(name = "geom", columnDefinition = "GEOMETRYCOLLECTION", nullable = false)
    private GeometryCollection getGeom() {
        return geom;
    }

    private void setGeom(GeometryCollection geom) {
        this.geom = geom;
    }

    @Transient
    public Geometry getGeometry() {
        // We assume zero or one geometries in the GeometryCollection
        if ((geom == null) || (geom.getNumGeometries() == 0)) {
            return null;
        } else {
            return geom.getGeometryN(0);
        }
    }

    public void setGeometry(Geometry geometry) {
        // We put zero or one geometries in the GeometryCollection
        Geometry[] geometries = new Geometry[1];
        geometries[0] = geometry;
        geom = new GeometryCollection(geometries, new GeometryFactory());
    }

}
