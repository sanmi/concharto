package com.tech4d.tsm.model.geometry;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Transient;

import org.hibernate.annotations.Type;

import com.tech4d.tsm.model.BaseEntity;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryCollection;
import com.vividsolutions.jts.geom.GeometryFactory;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "geometrytype", discriminatorType = DiscriminatorType.STRING)
@org.hibernate.annotations.Table(comment = "ENGINE : MyISAM", appliesTo = "TsGeometry")
public class TsGeometry extends BaseEntity {

    private GeometryCollection geometryCollection;

    public TsGeometry() {
        super();
    }

    public TsGeometry(Geometry geometry) {
        super();
        this.setGeometry(geometry);
    }

    @Type(type = "com.tech4d.tsm.model.geometry.GeometryUserType")
    @Column(name = "geometryCollection", columnDefinition = "GEOMETRYCOLLECTION", nullable = false)
    private GeometryCollection getGeom() {
        return geometryCollection;
    }

    private void setGeom(GeometryCollection geom) {
        this.geometryCollection = geom;
    }

    @Transient
    public Geometry getGeometry() {
        // We assume zero or one geometries in the GeometryCollection
        if ((geometryCollection == null)
                || (geometryCollection.getNumGeometries() == 0)) {
            return null;
        } else {
            return getGeom().getGeometryN(0);
        }
    }

    public void setGeometry(Geometry geometry) {
        // We put zero or one geometries in the GeometryCollection
        Geometry[] geometries = new Geometry[1];
        geometries[0] = geometry;
        setGeom(new GeometryCollection(geometries, new GeometryFactory()));
    }

}
