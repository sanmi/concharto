package com.tech4d.tsm.service;

import java.util.List;

import com.tech4d.tsm.model.Event;

/**
 * Data object for holding search results
 * @author frank
 */
public class EventSearchResult {
    private List<Event> events;
    private int maxReturnedRecords;
    private int maxPossibleSearchResults;
    private int startRecord;
    private int endRecord;

    public int getEndRecord() {
        return endRecord;
    }
    public void setEndRecord(int endRecord) {
        this.endRecord = endRecord;
    }
    public int getMaxPossibleSearchResults() {
        return maxPossibleSearchResults;
    }
    public void setMaxPossibleSearchResults(int maxPossibleSearchResults) {
        this.maxPossibleSearchResults = maxPossibleSearchResults;
    }
    public int getMaxReturnedRecords() {
        return maxReturnedRecords;
    }
    public void setMaxReturnedRecords(int maxReturnedRecords) {
        this.maxReturnedRecords = maxReturnedRecords;
    }
    public int getStartRecord() {
        return startRecord;
    }
    public void setStartRecord(int startRecord) {
        this.startRecord = startRecord;
    }
    public List<Event> getEvents() {
        return events;
    }
    public void setEvents(List<Event> events) {
        this.events = events;
    }
    
}
