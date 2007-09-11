package com.tech4d.tsm.model.geometry;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import org.hibernate.annotations.Cascade;

import com.tech4d.tsm.model.BaseAuditableEntity;

@Entity
@org.hibernate.annotations.Table(comment = "ENGINE : MyISAM", appliesTo = "FEATURE")
public class Feature extends BaseAuditableEntity {
    private String address;
    private String snippet;
    private String description;
    private TimePrimitive timePrimitive;
    private StyleSelector styleSelector;

    private TsGeometry tsGeometry;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @OneToOne(cascade = { CascadeType.ALL })
//   TODO experiment with this @SQLDelete( sql="delete from TsGeometry where id = ?")
    public TsGeometry getTsGeometry() {
        return tsGeometry;
    }

    public void setTsGeometry(TsGeometry geometry) {
        this.tsGeometry = geometry;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSnippet() {
        return snippet;
    }

    public void setSnippet(String snippet) {
        this.snippet = snippet;
    }

    @ManyToOne(cascade = { CascadeType.ALL })
    @Cascade(org.hibernate.annotations.CascadeType.DELETE)
    public TimePrimitive getTimePrimitive() {
        return timePrimitive;
    }

    public void setTimePrimitive(TimePrimitive timePrimative) {
        this.timePrimitive = timePrimative;
    }

    @ManyToOne(cascade = {CascadeType.REFRESH, CascadeType.MERGE})
    public StyleSelector getStyleSelector() {
        return styleSelector;
    }

    public void setStyleSelector(StyleSelector styleSelector) {
        this.styleSelector = styleSelector;
    }

}
