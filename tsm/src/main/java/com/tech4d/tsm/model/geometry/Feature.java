package com.tech4d.tsm.model.geometry;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import org.hibernate.annotations.Cascade;

import com.tech4d.tsm.model.BaseAuditableEntity;

@Entity
@org.hibernate.annotations.Table(comment = "ENGINE : MyISAM", appliesTo = "FEATURE")
public class Feature extends BaseAuditableEntity {
    private String address;

    private TsGeometry tsGeometry;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @OneToOne(cascade = { CascadeType.ALL })
    public TsGeometry getTsGeometry() {
        return tsGeometry;
    }

    public void setTsGeometry(TsGeometry geometry) {
        this.tsGeometry = geometry;
    }

}
