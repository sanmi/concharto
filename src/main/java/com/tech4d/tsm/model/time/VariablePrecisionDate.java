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
package com.tech4d.tsm.model.time;

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

    @Type(type = "com.tech4d.tsm.model.time.UtcDateTimeType")
	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
}
