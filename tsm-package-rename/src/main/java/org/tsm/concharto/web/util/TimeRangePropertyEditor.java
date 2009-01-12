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
