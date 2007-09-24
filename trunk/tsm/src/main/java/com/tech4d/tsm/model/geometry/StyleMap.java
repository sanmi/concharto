package com.tech4d.tsm.model.geometry;

import java.util.Map;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import org.hibernate.annotations.ForeignKey;

@Entity
@DiscriminatorValue("map")
public class StyleMap extends StyleSelector{
    private Map<String, StyleUrl> map;

    @org.hibernate.annotations.CollectionOfElements
    @ForeignKey(name="FK_STYLEMAP")
    public Map<String, StyleUrl> getMap() {
        return map;
    }

    public void setMap(Map<String, StyleUrl> map) {
        this.map = map;
    }
    
}
