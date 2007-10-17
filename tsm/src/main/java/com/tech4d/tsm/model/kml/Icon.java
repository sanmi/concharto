package com.tech4d.tsm.model.kml;

import javax.persistence.Embeddable;

@Embeddable
public class Icon {
    private String url;

    private String httpQuery;

    public Icon() {
        super();
    }

    public Icon(String url, String httpQuery) {
        super();
        this.url = url;
        this.httpQuery = httpQuery;
    }

    public String getHttpQuery() {
        return httpQuery;
    }

    public void setHttpQuery(String httpQuery) {
        this.httpQuery = httpQuery;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}
