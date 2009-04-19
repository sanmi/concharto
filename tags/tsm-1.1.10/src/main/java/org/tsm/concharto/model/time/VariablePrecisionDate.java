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

import javax.persistence.Embeddable;

import org.hibernate.annotations.Type;

@Embeddable
public class VariablePrecisionDate {
    public static final int PRECISION_SECOND = 0;
    public static final int PRECISION_MINUTE = 1;
    public static final int PRECISION_HOUR = 2;
    public static final int PRECISION_DAY = 3;
    public static final int PRECISION_MONTH = 4;
    public static final int PRECISION_YEAR = 5;
    public static final int MAX_PRECISIONS = 6;

	Integer precision = null;
	Date date;

	public VariablePrecisionDate() {
		super();
	}

	public VariablePrecisionDate(Date date) {
		this.date = date;
	}
	
	public VariablePrecisionDate(Date date, Integer precision) {
		this.date = date;
		this.precision = precision;
	}

	public Integer getPrecision() {
		return precision;
	}

	public void setPrecision(Integer precision) {
		this.precision = precision;
	}

    @Type(type = "org.tsm.concharto.model.time.UtcDateTimeType")
	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
}
