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
package org.tsm.concharto.web.util;

import java.beans.PropertyEditorSupport;
import java.text.ParseException;

import org.tsm.concharto.util.TimeRangeFormat;


/**
 * Converts string to TimeRange and vica versa. Supports many human readable
 * formats. TODO INCOMPLETE 9-25-07
 */
public class TimeRangePropertyEditor extends PropertyEditorSupport {

    private org.tsm.concharto.model.time.SimpleTimeRange timeRange;
    @Override
    public void setValue(Object value) {
        this.timeRange = (org.tsm.concharto.model.time.SimpleTimeRange) value;
    }

    @Override
    public Object getValue() {
        return this.timeRange;
    }

    @Override
    public String getAsText() {
        // TODO debug incomplete!
        if (this.timeRange == null) {
            return null;
        } else {
            return TimeRangeFormat.format(this.timeRange);
        }
        
    }

    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        
        try {
            this.timeRange = TimeRangeFormat.parse(text);
        } catch (ParseException e) {
            throw new IllegalArgumentException();
        }
    }
}
