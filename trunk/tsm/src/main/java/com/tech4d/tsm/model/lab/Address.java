package com.tech4d.tsm.model.lab;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import com.vividsolutions.jts.geom.Point;

@Entity
@Table(name="ADDRESS")
@org.hibernate.annotations.Table(comment="ENGINE : MyISAM", appliesTo = "ADDRESS")
public class Address {
    private String address;
    private Point addressLocation;
    private Long id;
    
    @Id 
    @GeneratedValue(strategy=GenerationType.AUTO)
    public Long getId() {
        return id;
    }

    @SuppressWarnings("unused")
    private void setId(Long id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }
    
    public void setAddress(String address) {
        this.address = address;
    }

    @Type(type="com.tech4d.tsm.lab.GeometryUserType")
    @Column(name="addressLocation", columnDefinition="POINT")
    public Point getAddress_loc() {
        return addressLocation;
    }
    
    public void setAddress_loc(Point address_loc) {
        this.addressLocation = address_loc;
    }
}
