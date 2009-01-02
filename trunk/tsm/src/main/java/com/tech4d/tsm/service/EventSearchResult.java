/*******************************************************************************
 * ***** BEGIN LICENSE BLOCK *****
 * Version: MPL 1.1
 * 
 * The contents of this file are subject to the Mozilla Public License Version 
 * 1.1 (the "License"); you may not use this file except in compliance with 
 * the License. You may obtain a copy of the License at 
 * http://www.mozilla.org/MPL/
 * 
 * Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 * for the specific language governing rights and limitations under the
 * License.
 * 
 * The Original Code is Concharto.
 * 
 * The Initial Developer of the Original Code is
 * Time Space Map, LLC
 * Portions created by the Initial Developer are Copyright (C) 2007 - 2009
 * the Initial Developer. All Rights Reserved.
 * 
 * Contributor(s):
 * Time Space Map, LLC
 * 
 * ***** END LICENSE BLOCK *****
 ******************************************************************************/
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
