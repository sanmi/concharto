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
package org.tsm.concharto.model.time;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;

import org.hibernate.annotations.Type;

@Entity
public class TimeStamp extends TimePrimitive {
    private Date when;

    
    public TimeStamp() {
    }

    public TimeStamp(Date date) {
        this.setWhen(date);
    }

    @Column (name="time")
    @Type(type = "org.tsm.concharto.model.time.UtcDateTimeType")
    public Date getWhen() {
        return when;
    }

    public void setWhen(Date when) {
        this.when = when;
    }    
}
