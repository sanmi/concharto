package com.tech4d.tsm.service;

/**
 * A single entry in the tag cloud
 */
public class TagCloudEntry {
    
    public TagCloudEntry() {
        super();
    }
    public TagCloudEntry(String tag, Integer fontSize) {
        super();
        this.tag = tag;
        this.fontSize = fontSize;
    }
    String tag;
    Integer fontSize;
    public String getTag() {
        return tag;
    }
    public void setTag(String tag) {
        this.tag = tag;
    }
    public Integer getFontSize() {
        return fontSize;
    }
    public void setFontSize(Integer fontSize) {
        this.fontSize = fontSize;
    }
}
