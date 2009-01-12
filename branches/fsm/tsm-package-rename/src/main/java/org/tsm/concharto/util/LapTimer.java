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
package org.tsm.concharto.util;

import java.util.ArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Utility to accumulate times and displays them
 */
public class LapTimer {
    private Log log;
    private ArrayList<Long> times = new ArrayList<Long>();
    private ArrayList<String> labels = new ArrayList<String>();


    public LapTimer(Object obj) {
        this.log = LogFactory.getLog(obj.getClass());
        init();
    }

    public LapTimer(Log log) {
        this.log = log;
        init();
    }

    public void init() {
        times.clear();
        labels.clear();
        timeIt("start");
    }

    public LapTimer timeIt(String label) {
        times.add(new Long(System.currentTimeMillis()));
        labels.add(label);
        return this;
    }

    public void logDebugTime() {
        log.debug(this);
    }
    
    public void logInfoTime() {
    	log.info(this);
    }

    public void logDebugTime(String msg) {
        log.debug("," + msg + this);
    }

    public String toString() {
        StringBuffer strBuff = new StringBuffer();
        long total = 0;
        for (int i = 0; i < times.size(); i++) {
            if (i > 0) {
                long interval = ((Long) times.get(i)).longValue()
                        - ((Long) times.get(i - 1)).longValue();
                strBuff.append(labels.get(i)).append(",").append(interval).append(",");
                total += interval;
            }
        }
        strBuff.append("total,").append(total);
        return strBuff.toString();
    }
}
