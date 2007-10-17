package com.tech4d.tsm.model.kml;

import java.util.Map;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import org.hibernate.annotations.ForeignKey;

@Entity
@DiscriminatorValue("map")
public class StyleMap extends StyleSelector {
    private Map<String, com.tech4d.tsm.model.kml.StyleUrl> map;

    @org.hibernate.annotations.CollectionOfElements
    @ForeignKey(name="FK_STYLEMAP")
    public Map<String, com.tech4d.tsm.model.kml.StyleUrl> getMap() {
        return map;
    }

    public void setMap(Map<String, com.tech4d.tsm.model.kml.StyleUrl> map) {
        this.map = map;
    }
    
}
