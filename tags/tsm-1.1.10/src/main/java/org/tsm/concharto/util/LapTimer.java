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
