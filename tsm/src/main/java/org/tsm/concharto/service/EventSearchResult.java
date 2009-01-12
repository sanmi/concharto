/*******************************************************************************
 * Copyright 2009 Time Space Map, LLC
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package org.tsm.concharto.service;

import java.util.List;

import org.tsm.concharto.model.Event;


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
