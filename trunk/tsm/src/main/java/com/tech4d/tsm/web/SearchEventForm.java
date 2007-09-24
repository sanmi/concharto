package com.tech4d.tsm.web;

/**
 * Form data object for searching for events
 */
public class SearchEventForm {
    private String what;
    private String where;
    private String when;
    public String getWhat() {
        return what;
    }
    public void setWhat(String what) {
        this.what = what;
    }
    public String getWhen() {
        return when;
    }
    public void setWhen(String when) {
        this.when = when;
    }
    public String getWhere() {
        return where;
    }
    public void setWhere(String where) {
        this.where = where;
    }
}
