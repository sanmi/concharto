package com.tech4d.tsm.model.geometry;

import javax.persistence.Embeddable;

@Embeddable
public class StyleUrl {
    private String styleUrl;

    public String getStyleUrl() {
        return styleUrl;
    }

    public void setStyleUrl(String styleUrl) {
        this.styleUrl = styleUrl;
    }
}
